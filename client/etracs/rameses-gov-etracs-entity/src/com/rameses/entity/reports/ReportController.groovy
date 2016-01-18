package com.rameses.entity.reports;

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
            
    @Service('LGUService')
    def lguSvc
    
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
    
            
    List getDays(){
        return svc.getNumDays( entity.year, entity.month?.index-1 )
    }
    
    
    List getClassifications(){
        return svc.getClassifications()
    }
    
    
    def getLgus(){
        def orgclass = OsirisContext.env.ORGCLASS
        def orgid = OsirisContext.env.ORGID

        if ('PROVINCE'.equalsIgnoreCase(orgclass)) {
            return lguSvc.lookupMunicipalities([:])
        }
        else if ('MUNICIPALITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupMunicipalityById(orgid)]
        }
        else if ('CITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupCityById(orgid)]
        }
        return []
    }

    def getBarangays(){
        if (! entity.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }
    
    
}
