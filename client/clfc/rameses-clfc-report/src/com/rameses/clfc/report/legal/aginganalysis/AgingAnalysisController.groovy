package com.rameses.clfc.report.legal.aginganalysis

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class AgingAnalysisController
{
    @Service('LegalAgingAnalysisService')
    def service;
    
    @Service('DateService')
    def dateSvc;
    
    @Service('VarService')
    def varSvc;
    
    @Service('UserService')
    def userSvc;
    
    String title = "Aging Analysis Report";
    def reportPath = "com/rameses/clfc/report/legal/aginganalysis/";
    def mode;
    
    def entity;
    def params;
    def months;
    def users;
    
    void init(){
        entity = [:];
        entity.year = dateSvc.getServerYear();
        def month = dateSvc.getServerMonth();
        def index = month == 12 ? 0 : month - 1;
        months = dateSvc.getMonths();
        entity.month = months.get(index);
        def var = varSvc.getList(['category':'BRANCH', 'name':'branch%']);
        var.each{
            if(it.name == 'branch_companyname') entity.branch_companyname = it.value;
            if(it.name == 'branch_name') entity.branch_name = it.value;
            if(it.name == 'branch_address') entity.branch_address = it.value;
            if(it.name == 'branch_contactno') entity.branch_contactno = it.value;

        }
        users = userSvc.getList([:]);
        users.each{
            it.middlename = it.middlename ? it.middlename : "";
        }
        def user = OsirisContext.getEnv();
        entity.preparedbyname = user.FULLNAME;
        entity.preparedbytitle = user.JOBTITLE;
        println user;
        mode = 'init';
    }
    
    def back(){
        mode = 'init';
        return 'default';
    }
    
    def preview(){
        mode = 'preview';
        entity.period = "For the period of "+entity.month.caption+" "+entity.year;
        entity.items = service.getReportData();
        entity.items.each{
            println it;
        }
        entity.notedbyname = entity.notedby.firstname+" "+entity.notedby.middlename+" "+entity.notedby.lastname;
        entity.notedbytitle = entity.notedby.jobtitle;
        report.viewReport();
        return 'preview';
    }
    
    def report = [
        getReportName : { return reportPath + 'AgingAnalysisAmount.jasper' },
        getReportData : { return entity },
        getSubReports : { return getSubReports() },
        getParameters : { return params }
    ] as ReportModel;
    
    SubReport[] getSubReports(){
        return [ 
            new SubReport("DETAIL", reportPath+"AgingAnalysisAmountDetail.jasper")
        ] as SubReport[]; 
    }
    
}

