package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CashReceiptPrintDetailForm51Model extends ReportModel {

    def reportData; 
    def reportPath = 'com/rameses/gov/treasury/cashreceipt/forms/';

    public void init() {
        //do nothing 
    }

    public String getReportName() { 
        return reportPath + "af51detail.jasper" 
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport("ITEM", reportPath + "af51detailitem.jasper")
        ] as SubReport[];  
    }
}