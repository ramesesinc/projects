package com.rameses.clfc.report.legal.aging

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class LegalAgingReportController extends ReportModel
{
    @Binding
    def binding;
    
    @Service("LegalAgingReportService")
    def service;
    
    @Service("DateService")
    def dateSvc;
    
    String title = "Loan Aging Report";

    def mode = 'init'
    def txndate, criteria, notedby;
    def notedbyLookup = Inv.lookupOpener('report-user:lookup', [
         onselect: { o->
             notedby = o;
             if (!notedby.name) {
                 notedby.name = o.lastname + ', ' + o.firstname;
                 if (o.middlename) notedby.name += ' ' + o.middlename;
             }
             binding?.refresh('xnotedby');
         },
         roles  : 'BRANCH_MANAGER',
         domains: 'LOAN'
    ]);

    void init() {
        txndate = dateSvc.getServerDateAsString();
        //reportCriteriaList = service.getCriteria();
    }
    
    def getTypeList() {
        return service.getCriteria([:]);
    }
    
    def close() {
        return "_close";
    }
    
    void clearNotedBy() {
        notedby = [:];
        binding?.refresh('xnotedby');
    }
    
    private def getParams() {
        return [
            txndate : txndate, 
            criteria: criteria,
            notedby : notedby
        ];
    }
    
    def rptdata;
    def preview() {
        mode = 'preview';
        //service.generate(getParams());
        rptdata = service.getReportData(getParams());
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
        return rptdata;//service.getReportData(getParams());
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/legal/aging/LegalAgingReport.jasper";
    }

    public SubReport[] getSubReports() {
        def list = [];
        switch (criteria) {
            case 'amount'   : list = getAsToAmountSubReports(); break;
            case 'account'  : list = getAsToAccountSubReports(); break;
        }
        return list;
    }
    
    def getAsToAccountSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/legal/aging/LegalAgingAsToAccountReportDetail.jasper')
        ];
    }
    
    def getAsToAmountSubReports() {       
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/legal/aging/LegalAgingAsToAmountReportDetail.jasper')
        ];
    }
}

