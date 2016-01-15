package com.rameses.clfc.report.bankdeposit;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class BankDepositController extends ReportModel
{
    @Service("BankDepositReportService")
    def service;

    String title = "Bank Deposit Report";

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
        return "com/rameses/clfc/report/bankdeposit/BankDepositReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/bankdeposit/BankDepositReportDetail.jasper'),
            new SubReport('DEPOSITSLIP_DETAIL', 'com/rameses/clfc/report/bankdeposit/BankDepositReportDepositSlipDetail.jasper'),
            new SubReport('CBS_DETAIL', 'com/rameses/clfc/report/bankdeposit/BankDepositReportCBSDetail.jasper'),
            new SubReport('CHECK_DETAIL', 'com/rameses/clfc/report/bankdeposit/BankDepositReportCheckDetail.jasper')
        ];
    }
}