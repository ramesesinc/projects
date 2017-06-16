package com.rameses.gov.etracs.rpt.ledger.compromise;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.reports.*

public class RPTCompromiseModel 
{
    @Binding 
    def binding
    
    @Service('RPTCompromiseService')
    def svc 
    
    @Service("ReportParameterService")
    def svcParams
    
    @Service('DateService')
    def dateSvc 
    
    @Service('OrgService')
    def orgSvc;
    
    def mode = 'init' 
    def entity 
    def ledger 
    def nationalityList
    def civilStatusList
    def genderList
    def downpayment = 0.0
    def type;
    def orgtype;
    
    String entityName = 'rptcompromise';
    
    @FormTitle
    @FormId
    String getTitle(){
        if (entity.txnno)
            return 'Compromise Agreement : ' + entity.txnno;
        return 'Compromise Agreement (New)';
    }
    

    /*------------------------------------------------------------------------
    * Init Page Support
    ------------------------------------------------------------------------ */
    def doNext() {
        entity.downpayment = downpayment;
        entity.putAll(svc.initCompromise( entity ))
        mode = 'create'    
        return 'default' 
    }
	
    def onselectLedger = { xledger ->
        if (xledger.state == 'PENDING') 
            throw new Exception('Ledger is still pending.');
        if (xledger.state == 'CANCELLED') 
            throw new Exception('Ledger has already been cancelled.');
        
        if( xledger.undercompromised == null ) xledger.undercompromised = 0
        if( xledger.undercompromised == 1 || xledger.undercompromised == true ) {
            throw new Exception('Ledger has an active compromised agreement.')
        }
        def parseddate = dateSvc.parseCurrentDate()
        if( xledger.lastyearpaid == parseddate.year ) {
            throw new Exception('Ledger has no arrears.')
        }
        ledger                  = xledger 
        entity.rptledgerid      = ledger.objid 
        entity.faasid           = ledger.faasid 
        entity.bill = svc.validateAndGetDelinquentAmount(ledger, entity.txndate);
        delinquentamt = entity.bill.delinquentamt;
        binding.refresh('delinquentamt');
    }
    
    
    def delinquentamt = 0.0;
    
    def getDelinquentAmount(){
        return delinquentamt;
    }

    def getLookupLedger() {
        return InvokerUtil.lookupOpener('rptledger:lookup', [onselect:onselectLedger])
    }
	
    def gotoInit() {
        mode = 'init'
        return 'init'
    }
    
    /*------------------------------------------------------------------------
    * FormActions  Support
    ------------------------------------------------------------------------ */
       
    def create() {
        loadComboItemSources()
        entity = [
            txndate         : dateSvc.getServerDate(),
            term            : 12, 
            numofinstallment: 4, 
            downpaymentrequired : false,
            cypaymentrequired   : false,
            downpayment     : 20.0, 
            downpaymentrate : 0.0, 
            signatories     : [],
            manualdiff      : 0.0,
        ]
        mode = 'init' 
        type = 'online'
        return 'init' 
    }
    
    def createCapture(){
        create();
        type = 'capture';
        return 'init';
    }
    
    def open() {
        loadComboItemSources()
        entity = svc.open( entity.objid )
        mode = 'view' 
        orgtype = orgSvc.getRoot().orgclass;
        return 'default' 
    }
    
    void refresh(){
        open();
        binding.refresh();
    }
	
    void save() {
        if( mode == 'create' ) {
                entity = svc.create( entity )
        }
        else {
                entity = svc.update( entity )
        }
        mode = 'view' 
    }
    
    void edit() {
        mode = 'edit'
    }
    
    def delete() {
        if( MsgBox.confirm('Delete compromise agreement?') ) {
                svc.delete( entity.objid, entity.rptledgerid )
                return '_close'
        }
        return null 
    }
    
    void submit() {
        def msg = 'Submit compromise agreement for approval?'
        if( entity.cypaymentrequired || entity.downpaymentrequired ) {
                msg = 'Submit compromise agreement for payment?'
        }
        if( MsgBox.confirm( msg ) ) {
                entity = svc.submit( entity )
        }
    }
    
    void submitForApproval() {
        if( MsgBox.confirm( 'Submit compromise agreement for approval?' ) ) {
            try{
                entity = svc.submitForApproval( entity )
            }
            catch(e){
                refresh();
                throw e;
            }
        }
    }
    
    void approve() {
        if( MsgBox.confirm( 'Approve compromise agreement for approval?' ) ) {
                entity = svc.approve( entity )
        }
    }
    
    def report = [
        getReportName : { return 'rptcompromise' },
        getReportData : { return entity },
        getParameters : { return svcParams.getStandardParameter() }
    ] as ReportModel;
    
    def preview() {
        report.viewReport()
        return 'preview' 
    }
    
    void print() {
        report.viewReport()
        ReportUtil.print( report.report, true )
    }

    /*------------------------------------------------------------------------
    * Installment Tab Support
    ------------------------------------------------------------------------ */
    def installmentListHandler = [
        fetchList  : { return entity._installments},
        getRows    : { return 50 },
    ] as EditorListModel
    
    /*------------------------------------------------------------------------
    * Credit Tab Support
    ------------------------------------------------------------------------ */
    def creditListHandler = [
        fetchList  : { return entity._credits },
        getRows    : { return 50 },
        getColumns : { return [
            new Column(name:'remarks', caption:'Particulars'),
            new Column(name:'paidby', caption:'Paid By'),
            new Column(name:'collectorname', caption:'Collected By'),
            new Column(name:'orno', caption:'O.R. No.'),
            new Column(name:'ordate', caption:'O.R. Date', type:'date'),
            new Column(name:'amount', caption:'Amount', type:'decimal', format:'#,##0.00'),
        ]},
    ] as BasicListModel
    
    
    def onaddCreditHandler = { 
        open();
        creditListHandler.load()
        installmentListHandler.load()
    }
    
    def capturePayment() {
        return InvokerUtil.lookupOpener('rptcompromise:captureinstallment', [entity:entity, onadd:onaddCreditHandler])
    }
    
    
    
    def onaddDownpaymentHandler = { pmt -> 
        svc.postCapturedDownpayment(entity, pmt);
        open();
        creditListHandler.load();
        binding.refresh('.*')
    }
    
    void checkRequiredPaymentMade(paytype){
        def params = [objid:entity.objid, paytype:paytype]
        svc.checkRequiredPaymentMade(params)
    }
    
    def captureDownpayment() {
        checkRequiredPaymentMade([type:'downpayment', caption:'Downpayment'])
        return InvokerUtil.lookupOpener('rptcompromise:capturedownpayment', [entity:entity, onadd:onaddDownpaymentHandler])
    }
    
    
    
    def onaddCYPaymentHandler = { pmt -> 
        svc.postCapturedCurrentYearPayment(entity, pmt);
        open();
        creditListHandler.load();
        binding.refresh('.*')
    }
    
    def captureCYPayment() {
        checkRequiredPaymentMade([type:'cypayment', caption:'Current Year payment'])
        return InvokerUtil.lookupOpener('rptcompromise:capturecypayment', [onadd:onaddCYPaymentHandler])
    }
    
    
    
    /*------------------------------------------------------------------------
    * Agreement Information Tab Support
    ------------------------------------------------------------------------ */
    void loadComboItemSources() {
        nationalityList = LOV.NATIONALITIES*.value
        civilStatusList = LOV.CIVIL_STATUS*.value
        genderList = LOV.GENDER*.key
    }
    
    /*------------------------------------------------------------------------
    * Witness Tab Support
    ------------------------------------------------------------------------ */
    def selectedWitness 
    
    def witnessListHandler = [
        fetchList     : { return entity.signatories },
        createItem  : {return [:] },
        getColumns    : { return [
            new Column(name:'name', caption:'Name *', editable:true),
            new Column(name:'address', caption:'Address', editable:true),
        ]},
        onAddItem     : { item -> 
            if( ! entity.signatories.contains( item ) ) {
                entity.signatories.add( item )
            }
        },
        onRemoveItem  : { item ->
            if( MsgBox.confirm('Remove selected witness?') ) {
                entity.signatories.remove( item )
                return true;
            }
            return false 
        },
        validate  : { li -> 
            required( 'Name', li.item.name )
        },
    ] as EditorListModel
    
    boolean existsWitness( witness ) {
        return entity.signatories.find{ it.name == witness.name } != null 
    }
    
    void required( field, value ) {
        if( ! value )  throw new Exception( field + ' is required.')
    }
    
    /*------------------------------------------------------------------------
    * Delinquent Tab Support
    ------------------------------------------------------------------------ */
    def delinquentListHandler = [
        getRows       :  { return entity._items.size() + 1 },
        fetchList     : { return entity._items },
        getColumns    : { return [
            new Column(name:'tdno', caption:'TD No'),
            new Column(name:'year', caption:'Year', type:'integer', format:'####' ),
            new Column(name:'qtr', caption:'Qtr', type:'integer'),
            new Column(name:'classcode', caption:'Class'),
            new Column(name:'basic', caption:'Basic', type:'decimal'),
            new Column(name:'basicpaid', caption:'Basic Paid', type:'decimal'),
            new Column(name:'basicint', caption:'Penalty', type:'decimal'),
            new Column(name:'basicintpaid', caption:'Penalty Paid', type:'decimal'),
            new Column(name:'basicidle', caption:'Idle Land', type:'decimal'),
            new Column(name:'basicidlepaid', caption:'Idle Land Paid', type:'decimal'),
            new Column(name:'basicidleint', caption:'Idle Penalty', type:'decimal'),
            new Column(name:'basicidleintpaid', caption:'Penalty Paid', type:'decimal'),
            new Column(name:'sef', caption:'SEF', type:'decimal'),
            new Column(name:'sefpaid', caption:'SEF Paid', type:'decimal'),
            new Column(name:'sefint', caption:'Penalty', type:'decimal'),
            new Column(name:'sefintpaid', caption:'Penalty Paid', type:'decimal'),
            new Column(name:'total', caption:'Total', type:'decimal'),
            new Column(name:'payment', caption:'Payment', type:'decimal'),
            new Column(name:'fullypaid', caption:'Paid?', type:'boolean'),
        ]},
    ] as BasicListModel
    
    
    void closeDefaultedCompromise(){
        def msg = 'Make this agreement as "defaulted"?\n\n'
        msg += 'When an agreement is defaulted, its outstanding balance\n'
        msg += ' will be recomputed as of the current date.\n'
        
        if (MsgBox.confirm(msg) ){
            def o = svc.closeDefaultedCompromise(entity);
            entity.state = o.state ;
        }
    }
    
    
    void modifyInstallmentDate(){
        if ( !entity.firstinstallmentdate )
            throw new Exception('First Installment Date is required.');
        def interval = (int)(entity.term / entity.numofinstallment)
        entity._installments = svc.modifyInstallmentDueDates(entity.firstinstallmentdate, interval, entity._installments)
        installmentListHandler.load();
    }
    
    void restructureInstallments(){
        if (MsgBox.confirm('Re-structure the existing number of Installments?')){
            def numofinstallment = MsgBox.prompt('Enter the revised Number of Installments');
            entity.numofinstallment = validateAndGetNumberInstallment(numofinstallment)
            entity._installments = svc.restructureInstallments(entity)
            installmentListHandler.load();
        }
    }
    
    boolean getShowRestructure(){
        if (!entity.state.matches('FORAPPROVAL|APPROVED')) return false;
        if (entity._installments.find{it.amtpaid > 0.0}) return false;
        return true;
    }
    
    
    def contractMunicipal(def inv){
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
    
    def contractProvince(def inv){
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
    
    def validateAndGetNumberInstallment(numofinstallment){
        if (numofinstallment == null) throw new Exception('Number of Installment is required.');
        try{
            return new java.math.BigDecimal(numofinstallment+'').intValue();
        }
        catch(e){
            throw new Exception('Invalid number of installments.');
        }
    }
    
    
    def getShowDeleteAction(){
        if ( !entity.state.matches('DRAFT|FORPAYMENT') )
            return false;
        if (mode != 'view')
            return false;
        return true;
    }
    
    def getBalance(){
        def total = entity._items.total.sum();
        def payment = entity._items.payment.sum();
        return total - payment;
    }
    
    def getShowRestructure(){
        if (entity._installments){
            def paiditems = entity._installments.findAll{it.amtpaid > 0.0}
            if(paiditems)
                return false;
        }
        return true;
    }
}

