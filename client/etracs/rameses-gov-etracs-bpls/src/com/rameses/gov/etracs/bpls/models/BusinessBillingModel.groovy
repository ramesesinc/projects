package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.osiris2.reports.*;

class BusinessBillingModel  extends com.rameses.etracs.shared.ReportController {

    @Service("BusinessBillingService")
    def billSvc;

    def entity;
    def title = "Business Billing"
    def reportPath = "com/rameses/gov/etracs/bpls/reports/billing/"
    String reportName = reportPath + "BPBilling.jasper";

    def getReportData() {
        return billSvc.getBilling( [applicationid: entity.objid] ); 
    }

    SubReport[] getSubReports(){ 
        return [
            new SubReport("BPBillingItem", reportPath + "BPBillingItem.jasper"),                
        ] as SubReport[]; 
    } 

    void sendSMS() {
        billSvc.sendSMS([ applicationid: entity.objid ]); 
        MsgBox.alert('Message successfully sent'); 
    } 
 } 