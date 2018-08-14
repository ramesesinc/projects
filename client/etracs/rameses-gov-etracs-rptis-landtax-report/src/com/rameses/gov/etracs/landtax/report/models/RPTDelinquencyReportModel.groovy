package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RPTDelinquencyReportModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('LandTaxReportDelinquencyService') 
    def svc
    
    String title = 'Realty Tax Delinquency Listing';
    
    String reportpath = 'com/rameses/gov/etracs/rpt/report/landtax/';
    def data;
    
    def task;
    def msg;
    
    String getReportName(){
       return reportpath + entity.format.reportname;
     }
    
    def formats = [
        [title:'STANDARD', type:'standard', reportname:'rptdelinquency.jasper'],
        [title:'FORMAT 1', type:'format1', reportname:'rptdelinquency_format1.jasper'],
        [title:'FORMAT 2', type:'format2', reportname:'rptdelinquency_format2.jasper'],
        [title:'FORMAT 3', type:'format3', reportname:'rptdelinquency_format3.jasper'],
    ]
    
    def sorttypes = [
        [code:'pin', name:' PIN'],
        [code:'taxpayer', name:' Taxpayer'],
        [code:'tdno', name:' TD Number'] 
    ];
    
    def periods = [
        [code:'AS_OF_BUILD',  name:' AS OF BUILD '], 
        [code:'AS_OF_YEAR', name:' AS OF YEAR '],  
        [code:'FOR_THE_YEAR', name:' FOR THE YEAR '],
        [code:'YEAR_RANGE', name:' YEAR RANGE '],
    ];    
    
    def initReport() { 
        def outcome = super.initReport(); 
        entity.period = periods[0]; 
        return outcome; 
    }
        
    void buildReportData(entity, asyncHandler){
        if (entity.period?.code == 'YEAR_RANGE' && entity.fromyear > entity.toyear)
            throw new Exception('From Year must be less than or equal to To Year.');
            
        entity.reporttype = 'listing';
        svc.generateDelinquencyReport(entity, asyncHandler);
    }
    
    Map getParameters(){
        def map = [TITLE : 'Realty Tax Delinquency Listing'];
        map.SUBTITLE = (entity.classification ? ' (' + entity.classification.name + ')' : '');
        map.BARANGAYNAME = (entity.barangay?.objid? entity.barangay.name: 'ALL'); 
        map.PERIODCODE = entity.period?.code; 
        
        if ( entity.period?.code.toString().matches('FOR_THE_YEAR|AS_OF_YEAR') ) { 
            map.PERIODNAME = entity.period.name.trim() + ' ' + entity.year; 
        } 
        else if (entity.period?.code == 'YEAR_RANGE'){
            map.PERIODNAME = 'YEAR ' + entity.fromyear + ' TO ' + entity.toyear 
        }else { 
            map.PERIODNAME = entity.period.name.trim(); 
        } 
        return map; 
    } 
}

