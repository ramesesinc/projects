package com.rameses.gov.etracs.rpt.landtax.billing.ui;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.gov.etracs.rpt.report.*;

class RPTBillingBatchController extends AbstractBatchReportController
{
    @Service("LandTaxBatchBillingService")
    def svc
    
    @Service("RPTBillingService")
    def billSvc;
    
    @Service("RPTBillingReportService")
    def billReportSvc;    
    
    @Service("ReportParameterService")
    def reportSvc;

    def title='Realty Tax Billing Batch Printing'
    
    @PropertyChangeListener
    def listener = [
        "params.advancebill" :{
            if (!params.advancebill){
                params.billdate = null;
                binding.refresh('params.billdate');
            }
                
        }
    ]

    public void afterInit(params){
        params.advancebill = false;
        params.billdate = null;
    }
    
            
    public def getItems(params){
        return svc.getLedgerIds(params);
    }
            
    public def getReportData(ledger){
        def bill = billSvc.initBill();
        bill.ledgers = [[objid: ledger.objid]];
        bill.taxpayer = ledger.taxpayer;
        bill.advancebill = params.advancebill;
        bill.billdate = params.billdate;
        return [bill: bill];
    }
            
    public def getReportInvokerName(){
        return 'rptbill:preview';
    }
    
    public def continueOnError(){return true}
    
    public def getItemMessage(data, copycount){
        return "Processing bill for TD No. " + data.tdno + '.'
    }
    
}