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
    
    def mode;
    def parsedate;
    def bill;
    def billto;
    def taxpayer;
    
    String title = 'Realty Tax Billing'
    
    void init() {
        mode = 'init'
        bill = svc.initBill(null)
    }
    
    def back() {
        init();
        clearLoadedProperties();
        return 'default' 
    }
    
    def getLookupTaxpayer() {
        return InvokerUtil.lookupOpener('entity:lookup', [
            onselect : {
                bill.taxpayer = it;
                bill.taxpayer.address = it.address.text;
                billto = bill.taxpayer;
                bill.billto = billto;
                clearLoadedProperties();
                loadProperties();
            },
                
            onempty : {
                bill.taxpayer = null;
                bill.billto = null;
                clearLoadedProperties();
            }
        ] )
    }
    
    def selectedItems 
    void updateLedgerBillStatement(){
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
    
    void buildBillReportInfo(){
        updateLedgerBillStatement();
        if (billto == null) 
            billto = bill.taxpayer
        bill.billto = billto;
        bill.ledgers = [];
        if (items) {
            selectedItems = items.findAll{it.bill == true}
            if (!selectedItems) selectedItems = items;
            selectedItems.each{
                try{
                    bill.rptledgerid = it.objid 
                    bill.ledgers << billReportSvc.getBilledLedger([objid:bill.objid, rptledgerid:bill.rptledgerid])
                }
                catch(e){
                    e.printStackTrace();
                }
            }
        }
        else {
            bill.ledgers << billReportSvc.getBilledLedger([objid:bill.objid, rptledgerid:bill.rptledgerid])
        }
        
        updateBillInfo()
        
        if(onbill) onbill();
        report.viewReport()
    }
    
    void printBill() {
        def b = svc.initBill(null)
        bill.objid = b.objid 
        bill.barcode = b.barcode 
        buildBillReportInfo()
        ReportUtil.print( report.report, true )
    }
    
    void initBatch(){
        init();
        bill.taxpayer = taxpayer;
        loadProperties();
        previewBill();
    }
    
    def previewBill() {
        buildBillReportInfo()
        mode = 'view'
        return 'preview'
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