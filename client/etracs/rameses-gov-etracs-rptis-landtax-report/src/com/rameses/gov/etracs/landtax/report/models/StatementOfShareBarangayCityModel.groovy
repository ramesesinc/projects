package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class StatementOfShareBarangayCityModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController 
{
    @Service('LandTaxReportBarangayCityShareService') 
    def svc
    
    String title = 'Statement of Barangay Share Report';
    String reportPath = 'com/rameses/gov/etracs/landtax/reports/';
    def months;

    def reportformats = [
        [code: 'standard',  caption: 'Standard', reportname: 'statement_of_share_brgy_city.jasper'],
    ]        

    def showPostingType = true;
    
    def postingtypes = [
        [code:'byliq', caption:'By Liquidation Date'],
        [code:'byrem', caption:'By Remittance Date'],
    ]
    
    def periods = [
        [code:'monthly', name:'Monthly']
    ]

    String getReportName() {
        return reportPath + entity.reportformat.reportname;
    }
    
    def initReport() { 
        def outcome = super.initReport();         
        entity.period = periods[0]; 
        months = dtSvc.getMonths();
        return outcome;
    } 
    
    void buildReportData(entity, asyncHandler){
        entity.reporttype = 'standard';
        svc.generateReport(entity, asyncHandler);
    } 
} 