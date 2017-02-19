package com.rameses.gov.etracs.landtax.report;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

abstract class LandTaxReportController
{
    @Service('DateService')
    def dtsvc;
            
    @Service("ReportParameterService")
    def svcParams;
            
    @Service('UsergroupMemberLookupService')
    def ugmsvc;
            
    @Service('LGUService')
    def lguSvc
    
    @Binding
    def binding;

    def mode
    def entity = [:];
    def reportdata;
          
    String title = 'Report Title';
    boolean showCollector = false;
    
    public abstract String getReportName();
    public abstract def getReportData(entity);
    
    def postingtypes = [
        [code:'byliq', caption:'By Liquidation Date'],
        [code:'byrem', caption:'By Remittance Date'],
    ]
    
    
    @PropertyChangeListener
    def listener = [
        "entity.period":{
            binding.refresh("entity.*");
            entity.fromdate = '';
            entity.todate = '';   
        }
    ];
            
    def initReport(){
        return 'default';
    }

    def init() {
        def parsedate = dtsvc.parseCurrentDate();
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
        reportdata = getReportData(entity);
        report.viewReport();
    }

    def report = [
        getReportName : { return getReportName() }, 
        getReportData : { return reportdata },
        getSubReports : { return getSubReports() },
        getParameters : { return  getParameters() },
    ] as ReportModel;

    def back() {
        binding.refresh("entity.*");
        mode = 'init';
        return 'default';  
    }
        
    def getParameters(){
        def reportparams = svcParams.getStandardParameter();
        reportparams.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png");
        
        def p = getReportParameters();
        if (p) reportparams += p;
        return reportparams;
    }
    
    def getReportParameters(){
        return [:];
    }
    
    SubReport[] getSubReports() {
        return null;   
    }
      

    def getPeriods(){
        return ["Daily", "Monthly", "Range"];
    }
            
    def getTxnMonths(){
        return dtsvc.getMonths();
    }
            
    List getMonthsByQtr() {
        return dtsvc.getMonthsByQtr( entity.qtr );
    }
            
    def getCollectors(){
        return ugmsvc.getList([_tag:"COLLECTOR"]);
    }  
}

