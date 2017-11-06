package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import java.text.SimpleDateFormat;

public class CashReceiptFormCT extends ReportModel {

    @Service("ReportParameterService")
    def paramSvc;

    def reportPath = 'com/rameses/gov/treasury/cashreceipt/forms/';
    def YMD = new SimpleDateFormat("yyyy-MM-dd");
    def YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    def reportData;
    
    public Map getParameters() {
        def p = [:]; 
        p.RECEIPTITEMCOUNT = reportData.items.size(); 
        
        def remarks = []; 
        reportData.items.each {  
            if (it.remarks) remarks << it.remarks; 
        }        
        p.REMARKS = remarks.join(', '); 
        return p;
    }
    
    public Object getReportData() {
        reportData.voided = reportData.voided.toString().matches("1|true") ? 1 : 0; 

        def receiptdate = reportData.receiptdate; 
        if ( receiptdate instanceof String ) {
            reportData.receiptdate = YMD.parse( receiptdate ); 
        }
        
        def txndate = reportData.txndate; 
        if ( txndate instanceof String ) { 
            try { 
                txndate = YMDHMS.parse( txndate ); 
            } catch(Throwable ign) {
                txndate = null; 
            }
        }
        reportData.txndate = txndate; 
        return reportData; 
    }
    
    public String getReportName() {
        return reportPath + "form_ct.jasper";
    }
    
    public SubReport[] getSubReports(){
        return [
            new SubReport("Item", reportPath + "form_ctitem.jasper")
        ] as SubReport[];    
    }
}
