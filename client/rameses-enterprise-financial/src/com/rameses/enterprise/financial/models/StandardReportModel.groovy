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
    def _parameters = [:];
    
    String reportName;
    
    public Map getParameters() {
        return _parameters;
    }
    
    void initA() {
        if ( query.reporttitle ) parameters.TITLE = query.reporttitle;
        else if ( query.maingroup?.title ) parameters.TITLE = query.maingroup.title;

        reportName = invoker.properties.reportName; 
        reportData = reportSvc.getReport( query ); 

        def yearinfo = query.criteriaList.find{( it.field.name == 'year')} 
        if ( yearinfo ) {
            def monthinfo = query.criteriaList.find{( it.field.name == 'month')} 
            if ( monthinfo ) {
                def str = ''+ yearinfo.value +'-'+ monthinfo.value.toString().padLeft(2, '0') +'-01'; 
                def date = new java.text.SimpleDateFormat('yyyy-MM-dd').parse( str ); 
                def formatter = new java.text.SimpleDateFormat('MMMMM yyyy'); 
                parameters.PERIOD = 'FOR THE MONTH OF '+ formatter.format( date ).toUpperCase(); 
            } 
            else {
                parameters.PERIOD = 'FOR THE YEAR '+ yearinfo.value; 
            }
        }
        
        def fundinfo = query.criteriaList.find{( it.field.name == 'fundid')} 
        if ( fundinfo?.value ) { 
            parameters.FUND = fundinfo.value.collect{ it.value }.findAll{( it )}.join(', '); 
        }

        def iteminfo = query.criteriaList.find{( it.field.name == 'itemid')} 
        if ( iteminfo?.value ) { 
            parameters.ITEMACCOUNT = iteminfo.value.collect{ it.value }.findAll{( it )}.join(', '); 
        } 
        viewReport(); 
    } 

    
    /*
    void initB() {
        reportData = reportSvc.getReport( query );
        reportName = invoker.properties.reportName;
        viewReport();
    }
    */
    
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

