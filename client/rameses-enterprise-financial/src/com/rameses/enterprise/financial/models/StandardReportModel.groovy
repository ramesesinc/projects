package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.* 

class StandardReportModel extends ReportModel { 
    
    @Service('AccountingStandardReportService') 
    def reportSvc;
    
    // def reportpath = 'com/rameses/enterprise/accounting/reports/';
    def query;
    def reportData = [:];
    String reportName;
    
    void initA() {
        reportData = reportSvc.getReport( query );
        reportName = invoker.properties.reportName;
        viewReport();
    }

    void initB() {
        reportData = reportSvc.getReport( query );
        reportName = invoker.properties.reportName;
        viewReport();
    }
    
    /*
        if ( !query.template?.name ) throw new Exception('Please select a template'); 

        def filter = criteriaList.find{( it.field?.name )}
        if ( !filter ) throw new Exception('Please specify at least one filter'); 
        
        query.filters = criteriaList;
        query.maingroupid = query.maingroup?.objid; 
        def resp = reportSvc.getReport( query ); 
        report = [
            getReportName: { return reportpath + query.template.name; },
            getReportData: { return resp.reportdata; }, 
            getParameters: { 
                if ( query.reporttitle ) {
                    resp.reportparam.TITLE = query.reporttitle; 
                }
                return resp.reportparam; 
            } 
        ] as ReportModel;
        report.viewReport();
        
        mode = 'preview';         
        return 'preview';
    */
    
} 

