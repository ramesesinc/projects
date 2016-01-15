package com.rameses.clfc.report.treasury.shortagesummary;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class VaultDepositReportController extends ReportModel
{    
    @Service("VaultCheckoutReportService")
    def service;

    String title = "Vault Checkout Report";

    def mode = 'init'
    def txndate;

    def close() {
        return "_close";
    }
    
    def preview() {
        mode = 'preview';
        viewReport();
        return 'preview';
    }
    
    def back() {
        mode = 'init';
        return "default";
    }

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getReportData([txndate: txndate]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/vaultcheckout/VaultCheckoutReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/vaultcheckout/VaultCheckoutReportDetail.jasper'),
            new SubReport('ITEMDETAIL', 'com/rameses/clfc/report/treasury/vaultcheckout/VaultCheckoutReportItemDetail.jasper')
        ];
    }
}