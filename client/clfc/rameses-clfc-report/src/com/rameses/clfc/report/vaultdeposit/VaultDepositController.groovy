package com.rameses.clfc.report.vaultdeposit;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class VaultDepositController extends ReportModel
{
    @Service("VaultDepositReportService")
    def service;

    String title = "Vault Deposit Report";

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
        return service.getReportDataByDepositid(entity);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/vaultdeposit/VaultDepositReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/vaultdeposit/VaultDepositReportDetail.jasper'),
            new SubReport('DEPOSITSLIP_DETAIL', 'com/rameses/clfc/report/vaultdeposit/VaultDepositReportDepositSlipDetail.jasper'),
            new SubReport('CBS_DETAIL', 'com/rameses/clfc/report/vaultdeposit/VaultDepositReportCBSDetail.jasper'),
            new SubReport('CHECK_DETAIL', 'com/rameses/clfc/report/vaultdeposit/VaultDepositReportCheckDetail.jasper')
        ];
    }
}