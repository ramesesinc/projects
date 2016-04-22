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
        return svc.getTaxpayerIds(params);
    }
            
    public def getReportData(entity){
        return [taxpayer:entity.taxpayer];
    }
            
    public def getReportInvokerName(){
        return 'rptbill:batch';
    }
    
    public def continueOnError(){return true}
    
    public def getItemMessage(data, copycount){
        return "Processing bill of " + data.taxpayer.name + '.'
    }
    
}