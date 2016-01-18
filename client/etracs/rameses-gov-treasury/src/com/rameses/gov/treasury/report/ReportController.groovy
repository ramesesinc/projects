package com.rameses.gov.treasury.report;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

abstract class ReportController
{
    @Binding
    def binding
    
    @Service("ReportParameterService")
    def svcParams;
    
    @Service('DateService')
    def dtSvc
    
    def mode
    def entity = [:];
    def params = [:];
    def reportdata
    
    abstract def getReportData();
    abstract String getReportName();
    
    
    
    def getFormControl(){
        return null;
    }
    
    
    SubReport[] getSubReports(){
        return null;
    }
    
    Map getParameters(){
        return [:]
    }
    
    
    def initReport(){
        return 'default'
    }
    
    def init() {
        def parsedate = dtSvc.parseCurrentDate();
        entity.year = parsedate.year;
        entity.qtr  = parsedate.qtr;
        entity.month = getMonthsByQtr().find{it.index == parsedate.month}
        mode = 'init'
        return initReport();
    }
    
    def preview() {
        buildReport()
        mode = 'view'
        return 'preview' 
    }
    
    
    void print(){
        buildReport()
        ReportUtil.print( report.report, true )
    }
    
    void buildReport(){
        reportdata = getReportData();
        params = svcParams.getStandardParameter()  + getParameters()
        report.viewReport()
    }
        
    def report = [
        getReportName : { return getReportName() }, 
        getReportData : { return reportdata },
        getSubReports : { return getSubReports() },
        getParameters : { return  params },
    ] as ReportModel;
    
    def back() {
        mode = 'init'
        return 'default' 
    }
    
    List getQuarters() {
        return [1,2,3,4]
    }
        
    List getMonthsByQtr() {
        return dtSvc.getMonthsByQtr( entity.qtr );
    }
    
    List getMonths(){
        return getMonthsByQtr();
    }
    
    List getBarangays(){
        return svc.getBarangays([:])
    }
    
}
