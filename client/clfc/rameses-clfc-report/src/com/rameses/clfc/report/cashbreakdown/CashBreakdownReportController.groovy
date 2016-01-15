package com.rameses.clfc.report.cashbreakdown;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CashBreakdownReportController extends ReportModel
{
    @Service("CashBreakdownReportService")
    def service;

    String title = "Cash Breakdown Report";

    def entity, objid, option, page;
    def optionList = [
        [caption: 'With Breakdown', value: 'withbreakdown'],
        [caption: 'Without Breakdown', value: 'withoutbreakdown']
    ]

    def close() {
        return "_close";
    }

    void init() {
        //page = "default";
        objid = entity.objid;
        if (isRole("LOAN.CASHIER") || isRole("TREASURY.CASHIER")) {
            option = optionList[1].value;
        } else {
            option = optionList[0].value;
        }
        preview();
    }

    void preview() {
        //page = 'preview';
        //objid = entity.objid;
        viewReport();
        //return page;
    }
    
    def isRole( role ) {
        return ClientContext.currentContext.headers.ROLES.containsKey(role)
    }

    def back() {
        page = 'default';
        return page;
    }

    def optionListHandler = [
        fetchList: { o->
            return optionList;
        }
    ] as BasicListModel;

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        def params = [
            objid   : objid,
            option  : option
        ]
        return service.getReportData(params);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/cashbreakdown/CashBreakdownReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('CASHBREAKDOWN', 'com/rameses/clfc/report/cashbreakdown/CashBreakdownReportDetail.jasper')
        ];
    }
}