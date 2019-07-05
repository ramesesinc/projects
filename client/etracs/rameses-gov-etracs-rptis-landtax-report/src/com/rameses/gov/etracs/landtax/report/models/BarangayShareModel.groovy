package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class BarangayShareModel extends com.rameses.gov.etracs.landtax.report.LandTaxReportController
{
    @Service('LandTaxReportStatementOfShareService')
    def svc; 

    String title = 'Statement of Share Report - Barangay';

    def reportpath = 'com/rameses/gov/etracs/landtax/reports/'
    String reportName = reportpath + 'statement_of_share_brgy.jasper';
    
    def data;
    
    public def getReportData(params){
        data = svc.getBarangayShares(params);
        return data.shares;
    }
    
        
    def getReportParameters(){
        return data?.params;
    }
    
    def getPeriods(){
        return ["Monthly"];
    }

}
