package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class TaxClearanceModel
{
    @Binding
    def binding;
    
    @Service('ReportParameterService')
    def paramSvc;
    
    @Service('LandTaxReportTaxClearanceService')
    def svc;
    
    @Service('DateService')
    def dtSvc;
    
    @Service('Var')
    def var 
    
    
    def MODE_INIT = 'init';
    def MODE_CREATE = 'create';
    def MODE_PREVIEW = 'preview';
    def MODE_PRINT = 'print';
    
    def entity;
    def mode;
    def addoption = 'bytd';
    def printoption = 'single';
    def lookup;
    def taxpayer;
    def address;
    def selectedItem;
    def includeditems;
    def msg;
    def processing;
    def showprinterdialog = false;
    
    def reportTypes;
    
    String title = 'Realty Tax Clearance'
    String entityName = 'rpttaxclearance';
            
                
    @PropertyChangeListener
    def listener = [
        'entity.year|entity.qtr|taxpayer|entity.reporttype' : {
            loadProperties()
        },
    ]
    
    def init(){
        taxpayer = null;
        reportTypes = svc.getReportTypes();
        entity = svc.initClearance();
        entity.certifiedby = var.getProperty('LANDTAX_CERTIFIEDBY', paramSvc.getStandardParameter().TREASURERNAME);
        entity.certifiedbytitle = var.getProperty('LANDTAX_CERTIFIEDBY_TITLE', paramSvc.getStandardParameter().TREASURERTITLE);
        entity.office = 'landtax';
        entity.ordate = dtSvc.getServerDate();
        entity.oramount = toDecimal(var.get('LANDTAX_TAXCLEARANCE_RATE'));
        entity.properties = [:]
        listHandler.reload();
        addoption = 'bytd';
        printoption = 'single';
        mode = MODE_INIT;
        return 'init'
    }
    
    def next() {
        svc.checkDuplicateReceipt(entity);
        mode = MODE_CREATE;
        return 'default';
    }
    
    def back() {
        mode = MODE_INIT;
        return 'init';
    }
    
    def doNew(){
        init();
        initTaxpayer(taxpayer);
        return 'init';
    }
    
    def open(){
        entity = svc.openClearance(entity.objid)
        entity.reporttype = entity.properties?.reporttype
        return preview();
    }
    
    def preview(){
        report.viewReport();
        mode = MODE_PREVIEW;
        return 'preview'
    }
    
    def printTask = [
        cancelled: false,
        
        run : {
            processing = true;
            includeditems.each{
                showMessage('Printing clearance for TD No. ' + it.tdno);
                createClearance([it]);
                report.viewReport();
                ReportUtil.print( report.report, showprinterdialog) ;
                sleep(1000);
            }
            completed();
        }
    ] as Runnable;
    
    void showMessage(status) {
        msg = status;
        binding.refresh('msg');
    }
    
    void completed() {
        showMessage('Printing completed...');
        processing = false;
        binding.refresh('loadingicon');
        MsgBox.alert('Clearances has been successfully printed.');
        binding.fireNavigation(init());
    }
    
    void sleep(ms) {
        try {
            Thread.sleep(ms);
        }catch(e) {
            //interrupted
        }
    }
    
    def save(){
        includeditems = entity.items.findAll{ it.included == true};
        if (!includeditems) throw new Exception('At least one property must be included.');
        
        if (MsgBox.confirm('Save clearance?')){
            if (printoption == 'single') {
                createClearance(includeditems)
                return preview();
            } else {
                msg = 'Preparing clearances for printing...';
                mode = MODE_PRINT;
                Thread t = new Thread(printTask);
                t.start();
                return 'print';
            }
        }
        return null;
    }
    
    def createClearance(items) {
        entity.objid = 'RPTC' + new java.rmi.server.UID();
        items.each{
            it.rptcertificationid = entity.objid;
        }
        entity.items = items;
        entity.properties.reporttype = entity.reporttype;
        entity.putAll(svc.createClearance(entity));
    }
    
    def getLookupTaxpayer(){
        return InvokerUtil.lookupOpener('entity:lookup',[
                onselect : {
                    initTaxpayer(it)
                },
                onempty : {
                    entity.taxpayer = null;
                    entity.requestedby = null;
                    entity.requestedbyaddress = null;
                    entity.items = [];
                    listHandler.load();
                    binding.refresh('entity.taxpayeraddress|entity.requested.*')
                },
        ])
    }
    
    void initTaxpayer(tpay){
        if (!taxpayer || taxpayer.objid != tpay.objid){
            this.taxpayer = tpay;
            this.address = tpay?.address
        }
        entity.taxpayer = taxpayer;
        entity.taxpayer?.address = address?.text;
        entity.requestedby = taxpayer?.name;
        entity.requestedbyaddress = address?.text;
        binding.refresh('entity.taxpayer.*|entity.requested.*|taxpayer')
        loadProperties();
    }
    
    def getLookupLedger(){
        def query = [:]
        query.state = 'APPROVED';
        return InvokerUtil.lookupOpener('rptledger:lookup', [
            query: query,
            onselect : { ledger ->
                if (! entity.items.find{it.objid == ledger.objid}) {
                    validateLedger(ledger);
                    ledger.refid   = ledger.objid
                    ledger.barangay = ledger.barangay.name;
                    ledger.rptcertificationid = entity.objid;
                    ledger.included = true; 
                    entity.items.add(ledger);
                    listHandler.load();
                }
            },
        ])
    }
    
    void validateLedger(ledger) {
        def msg = '';
        
        validateState(ledger);

        def allowSpecialCaseLedger = RPTUtil.toBoolean(var.get('landtax_taxclearance_allow_special_case_ledger'), false);
        if (allowSpecialCaseLedger) {
            if (ledger.totalav == 0) return;
            if (!ledger.taxable) return;
            if (ledger.effectivityyear == entity.effectivityyear) return;
        }
        
        if ('fullypaid' == entity.reporttype.name) {
            if (ledger.lastyearpaid < entity.year || 
                (ledger.lastyearpaid == entity.year && ledger.lastqtrpaid < entity.qtr)) 
            {
                if (entity.qtr == 4) {
                    msg = 'Ledger is not fully paid for the year ' + entity.year + '.'
                } else {
                    def qtrsuffixes = ['', 'st', 'nd', 'rd', 'th'];
                    def sqtr = entity.qtr + qtrsuffixes[entity.qtr] + 'qtr';
                    msg = 'Ledger is not fully paid for ' + sqtr + ' of ' + entity.year + '.'
                }
                throw new Exception(msg);
            }
        } else if ('zerovalue' == entity.reporttype.name) {
            if (ledger.totalav > 0) {
                throw new Exception('Ledger assessed value is not zero.');
            }
        } else if ('newdiscovery' == entity.reporttype.name) {
            if (ledger.effectivityyear != entity.effectivityyear) {
                throw new Exception('Ledger effectivity year msut be equal to ' + entity.effectivityyear);
            }
        } else if ('exempt' == entity.reporttype.name) {
            if (ledger.taxable == 1 || ledger.taxable == true) {
                throw new Exception('Ledger must be exempted.');
            }
        } else  {
            throw new Exception('Clearance type ' + entity.reporttype.title + ' is not yet supported.')
        }
    }
    
    def listHandler = [
        getRows : { return entity.items.size() },
        fetchList : { return entity.items },
    ] as EditorListModel
    
    
    void setAddoption(addoption){
        this.addoption = addoption;
        loadProperties();
    }
    
    void validateState(ledger){
        if (ledger.state == 'CANCELLED') {
            throw new Exception('Ledger has already been cancelled.')
        }
        if (ledger.state != 'APPROVED') {
            throw new Exception('Ledger is not yet approved.')
        }
    }
    
    void loadProperties(){
        entity.items = []
        if (taxpayer && entity.reporttype.name == 'fullypaid' && addoption == 'all'){
            entity.items = svc.getClearedLedgers(entity)
            entity.items.each{
                it.rptcertificationid = entity.objid;
                it.included = true;
            }
        }
        listHandler?.load();
    }
    
    
    
    void selectAll(){
        entity.items.each{
            it.included = true;
        }
        listHandler.load();
        binding?.refresh('selectedCount');
    }
    
    void deselectAll(){
        entity.items.each{
            it.included = false;
        }
        listHandler.load();
        binding?.refresh('selectedCount');
    }
    
    
    def reportpath = 'com/rameses/gov/etracs/landtax/report/taxclearance/'
    def report = [
        getReportName : { return reportpath + 'taxclearance.jasper' },
        getSubReports : { 
            return [
                new SubReport( 'taxclearanceitem', reportpath + 'taxclearanceitem.jasper' ),
                new SubReport( 'taxclearanceitempayment', reportpath + 'taxclearanceitempayment.jasper' ),
            ] as SubReport[]
        },
        getReportData : { return entity },
        getParameters : { 
            def params = paramSvc.getStandardParameter()
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            params.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
            return params 
        }
    ] as ReportModel
            
            
            
    List getQuarters(){
        return [1,2,3,4]
    }
    
    def toDecimal(val){
        if (val == null) return 0.0;
        try{
            return new BigDecimal(val.toString());
        }
        catch(e){
            return 0.0;
        }
    }
    
    def equalTaxpayerId(ledger) {
        return ledger.taxpayer.objid == entity.taxpayer.objid;
    }
    
    def replaceTaxpayer(ledger) {
        
    }
    
    def getSelectedCount() {
        return entity.items.findAll{it.included == true}.size();
    }
    
    def cancelPrint() {
        msg = null;
        mode = MODE_CREATE;
        return 'default';
    }

    def getManualCertificationNo() {
        return RPTUtil.toBoolean(var.get('landtax_taxclearance_numbering_manual'), false);
    }

    def getShowTransferPayment() {
        return RPTUtil.toBoolean(var.get('landtax_taxclearance_show_transfer_payment_info'), false);
    }

    def afterEdit = {
        open();
        report.reload();   
    }

    def edit() {
        return Inv.lookupOpener('rpttaxclearance:edit', [
            entity: entity,
            manualCertificationNo: getManualCertificationNo(),
            showTransferPayment: getShowTransferPayment(),
            afterEdit: afterEdit,
        ])
    }
}
