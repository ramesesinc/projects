package com.rameses.etracs.shared;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import java.io.*;
import java.net.URL;

abstract class ReportController
{
    def clientContext = com.rameses.rcp.framework.ClientContext.currentContext;

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

    public String getImagePath( def imagename ) {
        def appEnv = clientContext.appEnv; 
        def customfolder = appEnv['report.custom'];
        if (!customfolder) customfolder = appEnv['app.custom'];
        
        def path = 'images/' + imagename 
        if( customfolder ) { 
            def cpath = 'images/' + customfolder + '/' + imagename  
            if( clientContext.getResource(cpath) )  path = cpath 
        } 

        return path 
    }
    
    public URL getURL( def imagename ) { 
        try { 
            return clientContext.classLoader.getResource( getImagePath( imagename ) ); 
        } catch(e) {
            return null; 
        }
    }
    
    public InputStream getInputStream( def imagename ) {
        return clientContext.classLoader.getResourceAsStream( getImagePath( imagename ) );
    }

    public def getResource( def imagename) {
        def path = getImagePath( imagename );
        if(!path) return null
        
        def url = clientContext.classLoader.getResource( path )
        return url ? url.toString() : null ;
    }
    
    def initReport(){
        return 'default'
    }
    
    def init() {
        def parsedate = dtSvc.parseCurrentDate();
        entity.year = parsedate.year;
        entity.qtr  = parsedate.qtr;
        entity.month = getMonthsByQtr().find{it.index == parsedate.month}
        entity.day = parsedate.day;
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

        def loaders = Inv.lookupOpeners('report-parameter-loader', [ params : params ]); 
        loaders.each{ o-> 
            try {
                o.target = 'process'; 
                Inv.invokeOpener( o );  
            } catch (Throwable t) { 
                t.printStackTrace(); 
            } 
        } 

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
    
}
