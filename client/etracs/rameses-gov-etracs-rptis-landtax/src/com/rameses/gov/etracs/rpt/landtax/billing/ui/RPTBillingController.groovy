package com.rameses.gov.etracs.rpt.landtax.billing.ui;


import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*
import java.rmi.server.UID;
import com.rameses.etracs.shared.*;


public class RPTBillingController  
{
    @Binding
    def binding
            
    @Caller
    def caller;
    
    def onbill;
    
    @Service("RPTBillingService")
    def svc;
    
    @Service("RPTBillingReportService")
    def billReportSvc;

    @Service("ReportParameterService")
    def reportSvc;
    
    @Service('LGUService')
    def lguSvc 
    
    def selectedItem;
    def msg;
    def processing = false;
    def mode;
    def parsedate;
    def bill;
    def billto;
    def taxpayer;
    def rptledgerid;
    def showBack = true; 
    def advancebill;
    def billdate;
    
    String title = 'Real Property Tax Bill'
    
    @PropertyChangeListener
    def listener = [
        'bill.taxpayer' : {
            loadTaxpayerBillingInfo();
        }
    ]
    
    void init() {
        mode = 'init'
        bill = svc.initBill(rptledgerid)
    }
    
    def back() {
        init();
        clearLoadedProperties();
        return 'default' 
    }
    
    /*
    def selectedItems 
    void updateLedgerBillStatement(){
        if(advancebill){
            bill.advancebill = advancebill;
            bill.billdate = billdate;
        }
        
        if (items) {
            selectedItems = items.findAll{it.bill == true}
            if (!selectedItems) selectedItems = items;
            selectedItems.each{
                bill.rptledgerid = it.objid 
                bill.putAll(svc.generateBill(bill));
            }
        }
        else {
            // if no items, this is invoke from outside 
            bill.putAll(svc.generateBill(bill));
        }
        
    }
    */
    
    def billtask ;
    
    void doCancel(){
        billtask?.cancelled = true;
    }
    
    void buildBillReportInfo(){
        // updateLedgerBillStatement();
        if (!forprinting){
            processing = true;
        }
        
        if(advancebill){
            bill.advancebill = advancebill;
            bill.billdate = billdate;
        }
        if (billto == null) {
            billto = bill.taxpayer
        }
        bill.billto = billto;
        
        billtask = new BillTask(bill:bill, items:items, report:report)
        billtask.oncomplete = oncomplete;
        billtask.oncancel = oncancel;
        billtask.showmsg = showmsg;
        billtask.svc = svc;
        billtask.billReportSvc = billReportSvc;
        Thread t = new Thread(billtask);
        t.start();
        binding?.refresh();
    }
    
    def forprinting = false;
    void printBill() {
        def b = svc.initBill(rptledgerid)
        bill.objid = b.objid 
        bill.barcode = b.barcode 
        forprinting = true;
        buildBillReportInfo()
        // ReportUtil.print( report.report, true )
    }

    def closeform = false;
    void print() {
        forprinting = true;
        closeform = true 
        buildBillReportInfo()
    }    
    
    
    void buildSingleBill(){
        bill.putAll(svc.generateBill(bill));
        if (billto == null) 
            billto = bill.taxpayer
        bill.billto = billto;
        bill.ledgers = [];
        bill.ledgers << billReportSvc.getBilledLedger([objid:bill.objid, rptledgerid:bill.rptledgerid])
        updateBillInfo()
        if(onbill) onbill();
        report.viewReport();
    }
    
    def printSingleBill(){
        buildSingleBill();
        ReportUtil.print( report.report, true );
        return '_close';
    }
    
    def previewSingleBill(){
        buildSingleBill();
        mode = 'view'
        return 'preview'
    }
    
    void initBatch(){
        init();
        bill.taxpayer = taxpayer;
        buildSingleBill();
    }
    
    def showmsg = {
        msg = it;
        binding?.refresh('msg');
    }
    
    def oncancel = {msg->
        mode = 'init';
        processing = false;
        binding?.refresh();
        if (msg) {
            MsgBox.alert(msg);
        }
    }
    
    def oncomplete = {
        processing = false;
        updateBillInfo();
        if(onbill) onbill();
        report.viewReport()
        if (forprinting){
            ReportUtil.print( report.report, true );
        }
        else if(closeform){
            binding?.fireNavigation('_close');
        }
        else{
            mode = 'view';
            binding?.fireNavigation('preview');
        }
    }
    
    
    def hideitems = false;
    def previewBill() {
        hideitems = true;
        buildBillReportInfo()
    }
    
    def preview() {
        buildBillReportInfo()
    }

    void updateBillInfo(){
            //set expirty
            bill.validuntil = bill.ledgers[0].validuntil
                    
            //summarize totals
            bill.totalbasic     = sumListField(bill.ledgers, 'basic')
            bill.totalbasicdp	= sumListField(bill.ledgers, 'basicdp')
            bill.totalbasicnet	= sumListField(bill.ledgers, 'basicnet')
            bill.totalbasicidle	= sumListField(bill.ledgers, 'basicidle')
            bill.totalsef	= sumListField(bill.ledgers, 'sef')
            bill.totalsefdp	= sumListField(bill.ledgers, 'sefdp')
            bill.totalsefnet	= sumListField(bill.ledgers, 'sefnet')
            bill.totalfirecode  = sumListField(bill.ledgers, 'firecode')
            bill.grandtotal     = sumListField(bill.ledgers, 'total')
    }
    
    def sumListField(list, field){
        return list."${field}".sum();
    }

        
                      
    def reportpath = 'com/rameses/gov/etracs/rpt/landtax/billing/report/'
            
    def report = [
        getReportName : { return reportpath + 'rptbilling.jasper' },
        getSubReports  : {
            return [
                new SubReport('RPTBillingLedger', reportpath + 'rptbillingledger.jasper'),
                new SubReport('RPTBillingLedgerItem', reportpath + 'rptbillingledgeritem.jasper'),
            ] as SubReport[]
        },
        getReportData : { return bill },
        getParameters : {
            def params = reportSvc.getStandardParameter()
            params.RPUCOUNT = bill.ledgers.size() 
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            params.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
            return params 
        },
    ] as ReportModel
    
    List getQuarters() {
        return  [1,2,3,4]
    }
    
    List getRpuTypes(){
        return ['land', 'bldg', 'mach', 'planttree', 'misc']
    }
    
    
    List getBarangays(){
        return lguSvc.lookupBarangays([:])
    }
    
    
    
    
    
    def items = []
            
    def listHandler = [
        fetchList : { return items },
    ] as EditorListModel
            
                
    void selectAll(){
        items.each{
            it.bill = true;
            listHandler.reload();
        }
    }
    
    
    void deselectAll(){
        items.each{
            it.bill = false;
            listHandler.reload();
        }
    }
    
    
    void loadTaxpayerBillingInfo(){
        if (bill.taxpayer){
            bill.taxpayer.address = bill.taxpayer.address.text;
            billto = bill.taxpayer;
            bill.billto = billto;
            clearLoadedProperties();
            loadProperties();
        }
        else{
            billto = null;
            bill.billto = null;
            clearLoadedProperties();
        }
    }
    
    void loadProperties(){
        if (!bill.taxpayer) {
            throw new Exception('Taxpayer is required.')
        }
        bill.rptledgerid = null;
        items = svc.loadProperties(bill).each{ it.bill = true }
        listHandler.reload();
    }
    
    void clearLoadedProperties(){
        items = []
        listHandler.reload()
    }
    
    def getCount(){
        return items?.size();
    }
    
    
}

class BillTask implements Runnable 
{
    def showmsg;
    def oncomplete;
    def oncancel;
    def bill;
    def items;
    def svc; 
    def billReportSvc;
    def selectedItems;
    def cancelled;
    def report;
    
    
    public void run(){
        cancelled = false;
        bill.ledgers = [] 
        showmsg('Initializing...');
        if (items) {
            selectedItems = items.findAll{it.bill == true}
            if (!selectedItems) selectedItems = items;
            for( int idx = 0; idx < selectedItems.size(); idx++){
                if (cancelled) break;
                def item = selectedItems[idx]
                showmsg('Computing tax for TD No. ' + item.tdno + '   #' + (idx+1))
                
                try{
                    bill.rptledgerid = item.objid;
                    bill.putAll(svc.generateBill(bill));
                    bill.ledgers << billReportSvc.getBilledLedger(bill);
                }
                catch(e){
                    //
                }
                
            }
        }
        else {
            // if no items, this is invoke from outside 
            bill.putAll(svc.generateBill(bill));
            bill.ledgers << billReportSvc.getBilledLedger(bill);
        }
        if (cancelled){
            showmsg('');
            oncancel();
        }
        else{
            try{
                // showmsg('Loading billing statement. Please wait.');
                // bill.ledgers  = billReportSvc.getBilling(bill)
                showmsg('Building report. Please wait.');
                oncomplete()
                showmsg('');
            }
            catch(e){
                e.printStackTrace();
                oncancel(e.message)
            }
        }
            
        
    }
    
}