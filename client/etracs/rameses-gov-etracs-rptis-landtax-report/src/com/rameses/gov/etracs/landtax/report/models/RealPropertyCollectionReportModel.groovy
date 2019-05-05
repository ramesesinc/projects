package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class RealPropertyCollectionReportModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController 
{
    @Service('LandTaxReportService') 
    def svc
    
    String title = 'Real Property Tax Collection';
    String reportPath = 'com/rameses/gov/etracs/landtax/reports/';
    
    
    def showPostingType = true;

    def postingtypes = [
        [code:'byliq', caption:'By Liquidation Date'],
        [code:'byrem', caption:'By Remittance Date'],
    ]

    def reportformats = [
        [code: 'standard',  caption: 'Standard', reportname: 'rptcollection.jasper'],
        [code: 'summbymon',  caption: 'Summary by Month', reportname: 'rptcollection_summary_by_month.jasper'],
        [code: 'summbybrgy',  caption: 'Summary by Barangay', reportname: 'rptcollection_summary_by_brgy.jasper'],
    ]    
 
    def getPeriods() { 
        def periods = [
            [code:'monthly', name:'Monthly'],     
            [code:'quarterly', name:'Quarterly'],
            [code:'yearly', name:'Yearly'] 
        ];

        if ('summbymon' == entity.reportformat?.code) {
            periods = periods.findAll{it.code == 'yearly'}
        } else if ('summbybrgy' == entity.reportformat?.code) {
            periods = periods.findAll{it.code == 'monthly'}
        } 
        return periods;
    }
    
    def quarters = [
        [code:1, name:' 1ST'],
        [code:2, name:' 2ND'],
        [code:3, name:' 3RD'],
        [code:4, name:' 4TH']
    ];

    String getReportName() {
        return reportPath + entity.reportformat.reportname;
    }
    
    
    
    def months;
    
    def initReport() { 
        def outcome = super.initReport();         
        entity.period = periods[0]; 
        months = dtSvc.getMonths(); 
        return outcome; 
    } 
    
    void buildReportData(entity, asyncHandler){
        entity.reporttype = 'standard';
        svc.generateRPTCollectionReport(entity, asyncHandler);
    } 
    
    // SubReport[] getSubReports() { 
    //     return [ 
    //         new SubReport("rptcollectionitem", reportPath + "rptcollectionitem.jasper")
    //     ] as SubReport[]; 
    // } 
} 