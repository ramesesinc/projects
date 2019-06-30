package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
        
class CashReceiptForm56 extends ReportModel {

    def reportData;
    def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    
    def reportPath = 'com/rameses/gov/etracs/rpt/collection/ui/';
    // def newReportPath = 'com/rameses/gov/etracs/landtax/reports/';
    
    public Object getReportData() {
        def checks = [];
        def dates = [];
        reportData.paymentitems.each{ 
            checks << it.check?.bank?.code +' - '+ it.refno
            if( it.refdate instanceof String ) it.refdate = sdf.parse(it.refdate) 
            dates << sdf.format( it.refdate );
        }
        reportData.refno = checks.unique().join(',');
        reportData.refdate = dates.unique().join(',');
        reportData.voided = reportData.voided.toString().matches("1|true") ? 1 : 0; 
        return reportData;
    }
    public String getReportName() {
        return reportPath + 'AF56.jasper';
    }
    
    public SubReport[] getSubReports(){
        return [
            new SubReport('AF56Item', reportPath + 'AF56Item.jasper'),
        ] as SubReport[];    
    }
}