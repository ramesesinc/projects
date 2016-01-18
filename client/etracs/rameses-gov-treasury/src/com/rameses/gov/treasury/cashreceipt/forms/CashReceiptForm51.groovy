package com.rameses.gov.treasury.cashreceipt.forms;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import java.text.SimpleDateFormat;

public class CashReceiptForm51 extends ReportModel {

    @Service("ReportParameterService")
    def paramSvc;

    def reportPath = 'com/rameses/gov/treasury/cashreceipt/forms/';
    def sdf = new SimpleDateFormat("yyyy-MM-dd");
    def reportData;
    
    public Map getParameters() {
        def params = paramSvc.getStandardParameter(); 
        params.RECEIPTITEMCOUNT = reportData.items.size(); 
        def remarks = []; 
        reportData.items.each {  
            if (it.remarks) remarks << it.remarks; 
        }
        params.REMARKS = remarks.join(', '); 
        return params;
    }
    
    public Object getReportData() {
        def checks = [];
        def dates = [];
        reportData.paymentitems.each{
            checks << it.bank + ' - ' + it.refno; 
            if( it.refdate instanceof String ) it.refdate = sdf.parse(it.refdate); 
            
            dates << sdf.format( it.refdate );
        }
        reportData.refno = checks.join(', '); 
        reportData.refdate = dates.join(', ');
        reportData.voided = reportData.voided.toString().matches("1|true") ? 1 : 0; 

        def receiptdate = reportData.receiptdate; 
        if ( receiptdate instanceof String ) {
            reportData.receiptdate = sdf.parse( receiptdate ); 
        }
        return reportData; 
    }
    
    public String getReportName() {
        return reportPath + "form_51.jasper";
    }
    
    public SubReport[] getSubReports(){
        return [
            new SubReport("AF51Item", reportPath + "form_51item.jasper")
        ] as SubReport[];    
    }
}
