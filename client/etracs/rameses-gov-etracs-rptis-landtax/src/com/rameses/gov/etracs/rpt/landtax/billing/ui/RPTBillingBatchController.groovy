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
            
    public def getItems(params){
        return svc.getLedgerIds(params);
    }
            
    public def getReportData(ledger){
        return [rptledgerid:ledger.objid, taxpayer:ledger.taxpayer];
    }
            
    public def getReportInvokerName(){
        return 'rptbill:batch';
    }
    
    public def continueOnError(){return true}
    
    public def getItemMessage(data, copycount){
        return "Processing bill for TD No. " + data.tdno + '.'
    }
    
}