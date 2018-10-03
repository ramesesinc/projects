package com.rameses.gov.etracs.landtax.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CashReceiptPrintDetailForm56Model extends ReportModel {
    
    String reportPath = 'com/rameses/gov/etracs/rpt/collection/ui/';
            
    def reportData;
    
    void init() {
        reportData.refno = '';
        if (reportData.paymentitems) {
            reportData.refno = reportData.paymentitems.particulars.unique().join('/');
        } 
    }
    
    public String getReportName() { 
        return reportPath + "af51detail.jasper" 
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport("AF56Item", reportPath + "AF56DetailItem.jasper")
        ] as SubReport[];  
    }
}