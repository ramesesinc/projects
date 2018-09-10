package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RPTBillingModel
{
    @Binding
    def binding;
        
    @Service('LGUService')
    def lguSvc;
    
    @Service('RPTBillingService')
    def svc;
    
    @Service("ReportParameterService")
    def reportSvc;
        
    def title = 'Real Property Tax Bill'
    
    def bill;
    def mode = 'init';
    def processing = false;
    def showBack = false;
    
    
        
    void init() {
        mode = 'init';
        showBack = true;
        bill = svc.initBill();
    }
    
    def back() {
        init();
        processing = false;
        msg = null;
        clearLoadedProperties();
        return 'default' 
    }
    
    
    
    /*=============================================================
     *
     * REPORT SUPPORT 
     * 
    =============================================================*/
    
    def reportpath = 'com/rameses/gov/etracs/landtax/reports/'
            
    def report = [
        getReportName : { return reportpath + 'rptbilling.jasper' },
        getReportData : { return bill },
        getParameters : {
            def params = reportSvc.getStandardParameter()
            params.RPUCOUNT = bill.ledgers.size() 
            return params 
        },
    ] as ReportModel
    
    
    
    /*=============================================================
    * structure:
    * bill 
    *   ---> ledgers
    * ledger 
    *   --> items         = yearly items
    *   --> postingitems  = posting data (rptpayment_item)
    =============================================================*/
    def preview(){
        buildBill();
        mode = 'view'
        return 'report'
    }
    
    def print(){
        buildBill();
        ReportUtil.print( report.report, true );
        return '_close';
    }
        
    void buildBill(){
        bill.totals = [:];
        bill.putAll(svc.generateBill(bill));
        report.viewReport();
    }     
    
    
    
    /*=============================================================
     *
     * ASYNC SUPPORT 
     * 
    =============================================================*/
    def msg = 'Processing. Please wait...';
    def _printmode 

    def afterBuild = {
        report.viewReport();
        processing = false;
        msg = null;
        if (_printmode == 'preview') {
            mode = 'view';
            binding.fireNavigation('report');
        } else {
            mode = 'init';
            ReportUtil.print( report.report, true );
        }
        binding.refresh();
    }

    void sleep(millis) {
        try {
            Thread.sleep(millis);
        }catch(e){
            //
        }
    }

    void updateBillInfo(tmpbill) {
        if (!bill.billto) {
            bill.billdate = tmpbill.billdate;
            bill.billto = tmpbill.billto ;
            bill.taxpayer = tmpbill.taxpayer ;
            bill.dtposted = tmpbill.dtposted;
            bill.validuntil = tmpbill.validuntil;
        }
    }

    void aggregateBill(tmpbill) {
        bill.ledgers += tmpbill.ledgers;
        tmpbill.totals.each{ k,v ->
            if (!bill.totals[k]) {
                bill.totals[k] = 0
            }
            bill.totals[k] = bill.totals[k] + v
        }
    }

    def cancelled = false;

    void doCancel() {
        cancelled = true;
        processing = false;
        mode = 'init';
    }

    def task = [
        run : {
            bill.ledgers = [];
            bill.totals = [:];
            
            processing = true;
            cancelled = false;

            def tmpbill = [:];
            tmpbill.putAll(bill);
            tmpbill.totals = [:];

            def itemsToBill = items.findAll{it.bill == true}.collect{[objid:it.objid, tdno: it.tdno]};
            def cnt = 0;
            def itemCount = itemsToBill.size() 

            for (int i=0; i < itemsToBill.size(); i++) {
                if (cancelled) break;

                def ledger = itemsToBill[i]
                tmpbill.ledgers = [ledger];
                tmpbill._forpayment = false;
                try {
                    def b = svc.generateBill(tmpbill);
                    aggregateBill(b);
                    updateBillInfo(b)
                    cnt += 1;
                    msg = 'Processing Ledger ' + ledger.tdno + '    (' + cnt + '/' + itemCount + ')';
                    binding.refresh('msg');
                    sleep(100);
                } catch(e) {
                    msg = 'Error Processing Ledger ' + ledger.tdno + ' [ERROR] ' + e.message  + '. Ledger will be excluded.'
                    binding.refresh('msg');
                    sleep(1500);
                }
            }
            if (!cancelled){
                afterBuild();
            }
        }
    ] as Runnable 
    
    void doPreview() {
        _printmode = 'preview'
        new Thread(task).start();
    } 
    
    def doPrint() {
        _printmode = 'print'
        new Thread(task).start();
    } 
       
    
    
    /*=============================================================
     *
     * BATCH BILLING SUPPORT 
     * 
    =============================================================*/
    def items = [];
    def selectedItem;
    def quarters = [1,2,3,4];
    def rpuTypes = ['land', 'bldg', 'mach', 'planttree', 'misc'];
    
    
    @PropertyChangeListener
    def listener = [
        'bill.(billtoyear|billtoqtr|rputype|barangay)' : {
            loadProperties();
        }
    ]
    
    def getLookupTaxpayer(){
        return Inv.lookupOpener('entity:lookup', [
            onselect : {
                bill.taxpayer = it;
                loadProperties();
            },
            
            onempty : {
                clearLoadedProperties();
            }
            
        ]);
    }
    
    
    def listHandler = [
        fetchList : { return items },
    ] as EditorListModel
    
    void loadProperties(){
        if (bill.taxpayer) {
            items = svc.getOpenLedgers(bill).each{ it.bill = true }
            listHandler.reload();
        }
    }
    
    void clearLoadedProperties(){
        items = [];
        listHandler.reload();
    }
    
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
    
    def getCount(){
        return items?.size();
    }
    
    List getBarangays(){
        return lguSvc.lookupBarangays([:])
    }
} 