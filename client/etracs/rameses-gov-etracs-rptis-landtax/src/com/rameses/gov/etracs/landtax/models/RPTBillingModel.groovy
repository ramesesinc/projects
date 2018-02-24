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
        
    String title = 'Real Property Tax Bill';
    
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
        bill.putAll(svc.generateBill(bill));
        report.viewReport();
    }     
    
    
    
    /*=============================================================
     *
     * ASYNC SUPPORT 
     * 
    =============================================================*/
    def asyncHandler;
    def has_result;
    def msg;
    
    def getAsyncHandler(reportHandler) {
        return [
            onError: {o-> 
                MsgBox.err(o.message); 
                back();
                binding.refresh(); 
            }, 
            onTimeout: {
                asyncHandler.retry(); 
            },
            onCancel: {
                binding.fireNavigation( back() );
            }, 
            onMessage: {data-> 
                if (data == com.rameses.common.AsyncHandler.EOF) {
                    if (!has_result) {
                        back();
                        binding.refresh(); 
                    } 
                    
                } else if (data instanceof Throwable) { 
                    MsgBox.err(o.message); 
                    asyncHandler.cancel();
                    back();
                    binding.refresh();
                    
                } else {
                    has_result = true; 
                    bill = data;
                    binding.fireNavigation(reportHandler()); 
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler 
    }
    
    def buildAsyncReport(reportHandler){
        asyncHandler = getAsyncHandler(reportHandler);
        has_result = false; 
        bill.ledgers = items.findAll{it.bill == true}.collect{[objid:it.objid]};
        if (!bill.ledgers) throw new Exception('Ledgers to bill must be specified.')
        svc.generateBillAsync(bill, asyncHandler);
        processing = true; 
        msg = 'Processing. Please wait...';
        return null; 
    }
    
    def doPreview() {
        def reportHandler = {
            report.viewReport();
            processing = false;
            mode = 'view';
            msg = null;
            binding.fireNavigation('report');
            binding.refresh();
        }
        buildAsyncReport(reportHandler);
    } 
    
    def doPrint() {
        def reportHandler = {
            report.viewReport();
            processing = false;
            mode = 'init';
            msg = null;
            ReportUtil.print( report.report, true );
            binding.refresh(); 
        }
        buildAsyncReport(reportHandler);
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