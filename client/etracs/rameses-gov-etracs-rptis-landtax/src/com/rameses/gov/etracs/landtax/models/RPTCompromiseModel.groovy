package com.rameses.gov.etracs.landtax.models;

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
        entity.putAll(svc.init( entity ))
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
        entity.rptledger        = [objid:ledger.objid]
        entity.faasid           = ledger.faasid 
        entity.bill = svc.validateAndGetDelinquentBill(ledger, entity.txndate);
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
        entity.putAll(svc.open([objid:entity.objid]))
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
                entity.putAll(svc.create( entity ))
        }
        else {
                entity.putAll(svc.update( entity ))
        }
        mode = 'view' 
    }
    
    void edit() {
        mode = 'edit'
    }
    
    def delete() {
        if( MsgBox.confirm('Delete compromise agreement?') ) {
                svc.delete([objid:entity.objid, rptledger:entity.rptledger])
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
                entity = svc.submit(entity )
        }
    }
    
    void submitForApproval() {
        if( MsgBox.confirm( 'Submit compromise agreement for approval?' ) ) {
            try{
                entity.putAll(svc.submitForApproval( entity ))
            }
            catch(e){
                refresh();
                throw e;
            }
        }
    }
    
    void approve() {
        if( MsgBox.confirm( 'Approve compromise agreement for approval?' ) ) {
                entity.putAll(svc.approve( entity ))
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
        fetchList  : { return entity.installments},
        getRows    : { return 50 },
    ] as EditorListModel
    
    /*------------------------------------------------------------------------
    * Credit Tab Support
    ------------------------------------------------------------------------ */
    def creditListHandler = [
        fetchList  : { return entity.credits },
        getRows    : { return entity.credits.size() },
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
        getRows       :  { return entity.items.size() + 1 },
        fetchList     : { return entity.items },
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
        entity.installments = svc.modifyInstallmentDueDates(entity.firstinstallmentdate, interval, entity.installments)
        installmentListHandler.load();
    }
    
    void restructureInstallments(){
        if (MsgBox.confirm('Re-structure the existing number of Installments?')){
            def numofinstallment = MsgBox.prompt('Enter the revised Number of Installments');
            entity.numofinstallment = validateAndGetNumberInstallment(numofinstallment)
            entity.installments = svc.restructureInstallments(entity)
            installmentListHandler.load();
        }
    }
    
    boolean getShowRestructure(){
        if (!entity.state.matches('FORAPPROVAL|APPROVED')) return false;
        if (entity.installments.find{it.amtpaid > 0.0}) return false;
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
        def total = entity.items.total.sum();
        def payment = entity.items.payment.sum();
        return total - payment;
    }
    
    def getShowRestructure(){
        if (entity.installments){
            def paiditems = entity.installments.findAll{it.amtpaid > 0.0}
            if(paiditems)
                return false;
        }
        return true;
    }
}

