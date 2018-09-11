package com.rameses.gov.etracs.bpls.reports.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public class BPDelinquencyReportModel extends com.rameses.etracs.shared.AsyncReportController {
    
    @Binding
    def binding;
    
    @Service('BPDelinquencyReportService') 
    def svc;

    @Script('ReportPeriod') 
    def periodUtil; 
    
    String title = "Business Delinquency Listing";
    String reportpath = 'com/rameses/gov/etracs/bpls/reports/'; 
    String reportName = reportpath + 'BPDelinquency.jasper'; 

    def periods = [ 
        [type:'asofqtr', title:'As of Quarter'], 
        [type:'quarterly', title:'Quarterly'],
        [type:'yearly', title:'Yearly']        
    ];
    
    def barangays;

    def initReport() {
        def outcome = super.initReport(); 
        def resp = svc.initReport([:]); 
        barangays = resp.barangays; 
        entity.period = periods[0].type; 
        return outcome; 
    } 

    void buildReportData(entity, asyncHandler) { 
        def startdate = null; 
        def enddate = null; 
        if ( entity.period.toString().matches('quarterly') ) {
            startdate = periodUtil.getQtrStartDate( entity.year, entity.qtr ); 
            enddate = periodUtil.getQtrEndDate( entity.year, entity.qtr ); 
        } else if ( entity.period.toString().matches('asofqtr') ) { 
            startdate = periodUtil.getYearStartDate( 1900 ); 
            enddate = periodUtil.getQtrEndDate( entity.year, entity.qtr ); 
        } else if ( entity.period.toString().matches('yearly') ) {
            startdate = periodUtil.getYearStartDate( entity.year ); 
            enddate = periodUtil.getYearEndDate( entity.year ); 
        } else if ( entity.period.toString().matches('asofyear') ) {
            startdate = periodUtil.getYearStartDate( 1900 ); 
            enddate = periodUtil.getYearEndDate( entity.year ); 
        } else {
            throw new Exception('Please select the type of period first'); 
        }

        enddate = com.rameses.util.DateUtil.add( enddate, "1d" );
        entity.startdate = periodUtil.format( startdate, 'yyyy-MM-dd' );
        entity.enddate   = periodUtil.format( enddate,   'yyyy-MM-dd' );
        svc.getReport( entity, asyncHandler );
    }
    
    void buildResult( data ) {  
        if ( !data.reportdata ) {
            throw new Exception('No record(s) that matches your criteria'); 
        } 
        
        entity.builddate = data.builddate; 
    }

    Map getParameters(){ 
        def m = [:]; 
        if ( entity.period == 'yearly' ) { 
            m.PERIOD = periodUtil.getPeriodTitle([ year: entity.year ]);
        } else if ( entity.period == 'asofyear' ) { 
            m.PERIOD = 'AS OF '+ entity.year; 
        } else if ( entity.period == 'quarterly' ) { 
            m.PERIOD = periodUtil.getPeriodTitle([ year: entity.year, qtr: entity.qtr ]);
        } else if ( entity.period == 'asofqtr' ) { 
            def dt = periodUtil.getQtrEndDate( entity.year, entity.qtr );
            m.PERIOD = 'AS OF '+ periodUtil.getPeriodTitle([ year: entity.year, month: getMonth(dt) ]);
        } 

        m.BARANGAYNAME = ( entity.barangay? entity.barangay.name : '(ALL)' );
        m.BUILDDATE = entity.builddate; 
        return m; 
    } 
    
    private int getMonth( dateObj ) {
        def cal = java.util.Calendar.getInstance(); 
        cal.setTime( dateObj ); 
        return cal.get( java.util.Calendar.MONTH )+1;
    }
    
    def cancelPreview() {
        return back();
    }
} 