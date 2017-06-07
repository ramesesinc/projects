package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import com.rameses.gov.etracs.rptis.report.models.*;

class AbstractRPTCollectionReportModel {
    
    @Service('DateService')
    def dtsvc;

    @Service("ReportParameterService")
    def svcParams;

    @Service('UsergroupMemberLookupService')
    def ugmsvc;

    @Binding
    def binding;

    @Service('LandTaxReportAbstractTaxCollectionService')
    def svc;

    @Service('LGUService')
    def lguSvc

    def mode
    def entity = [:];
    def params = [:];
    def reportdata;
    
    def showPostingType = true;
    
    String title = "Abstract of Real Property Tax Collection";
    String reportpath = "com/rameses/gov/etracs/landtax/report/abstractofcollection/"
    String reportName = reportpath + 'abstractrptcollection.jasper'

    @PropertyChangeListener
    def listener = [
        "entity.period":{
            binding.refresh("entity.*");
            entity.fromdate = '';
            entity.todate = '';   
        }
    ];
    
    def postingtypes = [
        [code:'byliq', caption:'By Liquidation Date'],
        [code:'byrem', caption:'By Remittance Date'],
    ]

    def initReport(){
        return 'default'
    }

    def init() {
        def parsedate = dtsvc.parseCurrentDate();
        entity.year = parsedate.year;
        entity.qtr  = parsedate.qtr;
        entity.month = getMonthsByQtr().find{it.index == parsedate.month}
        mode = 'init';
        return initReport();
    }

    def preview() {
        buildReport();
        mode = 'view';
        return 'preview'; 
    }


    void print(){
        buildReport();
        ReportUtil.print( report.report, true );
    }

    void buildReport(){
        reportdata = getReportData();
        report.viewReport();
    }

    def report = [
        getReportName : { return getReportName() }, 
        getReportData : { return reportdata },
        getSubReports : { return getSubReports() },
        getParameters : { return  params },
    ] as ReportModel;

    def back() {
        binding.refresh("entity.*");
        mode = 'init';
        return 'default';  
    }

    def getReportData(){
       if(!entity.date && entity.period=='Daily') throw new Exception("Date is Required.");
       if(!entity.month && entity.period=='Monthly') throw new Exception("Month is Required.");
       if(!entity.year && entity.period=='Monthly') throw new Exception("Year is Required.");
       if(!entity.startdate && entity.period=='Range') throw new Exception("Date is Required.");
       if(!entity.enddate && entity.period=='Range') throw new Exception("Date is Required.");
       def list = svc.generateAbstractOfRPTCollection(entity);
       if(!list.items && !list.advitems) throw new Exception("Record(s) not found.");
       params = svcParams.getStandardParameter()  + list.header;
       params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png");
       return list;
    }

    SubReport[] getSubReports() {
         return [ 
            new SubReport("AbstractItem", reportpath + "abstractrptcollectionitem.jasper"),
            new SubReport("AbstractAdvance", reportpath + "abstractrptcollectionadv.jasper") 
        ] as SubReport[];    
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
