package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;

class JatReportModel extends AsyncReportModel2
{
    @Service('RPTReportJATService') 
    def svc 
    
    String title = 'Journal of Assessment Transaction Report'
    
    String reportName = 'com/rameses/gov/etracs/rptis/reports/jat.jasper';

    def showSection = false;
    def barangayRequired = false;
    
    void buildReportData(entity, asyncHandler){
        svc.generateJAT(entity, asyncHandler);
    }

    List getQuarters() {
        return null;
    }
}
