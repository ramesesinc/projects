package com.rameses.gov.treasury.report.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class SubCollectorRemittanceReportController {

    @Binding
    def binding;

    @Service("SubCollectorRemittanceService")
    def svc

    def reportPath = "com/rameses/gov/treasury/remittance/subcollector/report/"
    def reportdata;
    def entity;

    String title = "Daily Collection Report";

    void init() {
        viewReport();
    }

    void viewReport() {
        reportdata = svc.generateDCR(entity.objid);
        report.viewReport();
    }

    def report = [
        getReportName : { return reportPath + 'dcr_main.jasper' },
        getReportData : { return reportdata }, 
        getSubReports : {
            return [ 
                new SubReport("CollectionSummary", reportPath + "collectionsummary.jasper"),
                new SubReport("OtherPayment", reportPath + "otherpayment.jasper"),
            ] as SubReport[];    
        }
    ] as ReportModel 
}  