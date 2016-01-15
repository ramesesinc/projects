package com.rameses.clfc.report.treasury.shortagesummary;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class VaultDepositSummaryReportController extends ReportModel
{    
    @Service("VaultDepositSummaryReportService")
    def service;

    String title = "Vault Deposit Summary Report";

    def mode = 'init'
    def startdate;
    def enddate;

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
        return service.getReportData([startdate: startdate, enddate: enddate]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/vaultdepositsummary/VaultDepositSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/vaultdepositsummary/VaultDepositSummaryReportDetail.jasper'),
            new SubReport('ITEMDETAIL', 'com/rameses/clfc/report/treasury/vaultdepositsummary/VaultDepositSummaryReportItemDetail.jasper')
        ];
    }
}