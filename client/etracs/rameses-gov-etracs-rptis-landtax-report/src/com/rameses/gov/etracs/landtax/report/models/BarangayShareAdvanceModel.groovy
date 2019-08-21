package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class BarangayShareAdvanceModel extends com.rameses.gov.etracs.landtax.report.LandTaxReportController
{
    @Service('LandTaxReportStatementOfShareService')
    def svc; 

    String title = 'Barangay Share (Advance)';
    def reportpath = 'com/rameses/gov/etracs/landtax/reports/'
    def data;

    def summarytypes = [
        [name: 'ANNUAL', caption: 'ANNUAL'],
        [name: 'QTRLY', caption: 'QUARTERLY'],
    ]

    def initReport(){
        entity.advanceyear = entity.year + 1;
        return 'default';
    }

    public String getReportName() {
        return reportpath + 'statement_of_share_brgy_advance_' + entity.summarytype.name.toLowerCase() + '.jasper';
    }
    
    public def getReportData(params){
        data = svc.getBarangayAdvanceShares(params);
        return data.shares;
    }
    
    def getReportParameters(){
        return data?.params;
    }
    
}
