package com.rameses.clfc.report.vaultcheckout;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class VaultCheckoutController extends ReportModel
{
    @Service("VaultCheckoutReportService")
    def service;

    String title = "Vault Checkout Report";

    def mode = 'init'
    def entity;

    def close() {
        return "_close";
    }
    
    void preview() {
        viewReport();
    }

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getReportDataByCheckoutid(entity);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/vaultcheckout/VaultCheckoutReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/vaultcheckout/VaultCheckoutReportDetail.jasper'),
            new SubReport('DEPOSITSLIP_DETAIL', 'com/rameses/clfc/report/vaultcheckout/VaultCheckoutReportDepositSlipDetail.jasper'),
            new SubReport('CBS_DETAIL', 'com/rameses/clfc/report/vaultcheckout/VaultCheckoutReportCBSDetail.jasper'),
            new SubReport('CHECK_DETAIL', 'com/rameses/clfc/report/vaultcheckout/VaultCheckoutReportCheckDetail.jasper')
        ];
    }
}