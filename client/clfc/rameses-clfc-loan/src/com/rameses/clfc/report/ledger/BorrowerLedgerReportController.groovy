package com.rameses.clfc.report.ledger

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class BorrowerLedgerReportController extends ReportModel
{
    @Service('LoanLedgerReportService')
    def reportSvc;
    
    def ledgerid, appid;
	
    void preview() {
        viewReport();
    }
    
    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        def params = [objid: ledgerid, appid: appid];
        return reportSvc.getReportData(params);
    }

    public String getReportName() {
        return "com/rameses/clfc/report/ledger/BorrowerLedger.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('BORROWERLEDGERPAYMENT', 'com/rameses/clfc/report/ledger/BorrowerLedgerPayment.jasper')
        ];
    }
}

