package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.common.*;
import java.util.GregorianCalendar;

abstract class AsyncReportModel2
{
    @Binding
    def binding
    
    @Service("ReportParameterService")
    def svcParams;
    
    @Service('DateService')
    def dtSvc
            
    @Service('LGUService')
    def lguSvc

    @Service('QueryService')
    def querySvc; 

    def clientContext = com.rameses.rcp.framework.ClientContext.currentContext;
    
    def mode;
    def entity = [:];
    def params = [:];
    def reportdata;
    
    def data;
    def asyncHandler;
    def has_result_preview = false; 
    def showBack = true;
    def showSection = false;
    def barangayRequired = false;
    def revisionyears = [];

    def allowPrint = true;
    def allowSave = true;
    
    
    abstract String getReportName();
    abstract void buildReportData(entity, asyncHandler);
    

    def formControl = [
        getFormControls: {
            getStandardFormControls()
        }
   ] as FormPanelModel;
    

    def getStandardFormControls(){
        return [
            new FormControl( "combo", [captionWidth:110, caption:'Report Type', name:'entity.reporttype', required:true, items:'reporttypes', allowNull:false, expression:'#{item.caption}', visibleWhen:'#{reporttypes!=null}']),
            new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true]),
            new FormControl( "combo", [captionWidth:110, caption:'Quarter', name:'entity.qtr', required:true, items:'quarters', visibleWhen:'#{quarters != null}']),
            new FormControl( "combo", [captionWidth:110, caption:'Month', name:'entity.month', required:true, depends:'entity.reporttype,entity.qtr', items:'months', expression:'#{item.caption}', dynamic:true]),
            new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', required:true, allowNull:false, items:'lgus', expression:'#{item.name}']),
            new FormControl( "combo", [captionWidth:110, caption:'Barangay', name:'entity.barangay', required:barangayRequired, allowNull:!barangayRequired, items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, emptyText:'ALL']),
            new FormControl( "combo", [captionWidth:110, caption:'Revision Year', name:'entity.ry', required:true, allowNull:false, items:'revisionyears', visibleWhen:'#{revisionyears.size() > 0}']),
            new FormControl( "text", [captionWidth:110, caption:'Section', name:'entity.section', visibleWhen:'#{showSection}']),
        ]
    }
    
    
    SubReport[] getSubReports(){
        return null;
    }
    
    Map getParameters(){
        if (data && data.parameters)
            return data.parameters
        return [:]
    }
    
    
    def initReport(){
        return 'default'
    }
    
    def init() {
        def parsedate = dtSvc.parseCurrentDate();
        entity.year = parsedate.year;
        entity.qtr  = parsedate.qtr;
        entity.month = getMonthsByQtr().find{it.index == parsedate.month};
        revisionyears = getRevisionyears();
        mode = 'init'
        return initReport();
    }
    
    
    
    def preview() {
        asyncHandler = [
            onError: {o-> 
                MsgBox.err(o.message); 
                back();
                binding.refresh(); 
            }, 
            onTimeout: {
                asyncHandler.retry(); 
            },
            onCancel: {
                binding.fireNavigation( back() );
            }, 
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                    if (!has_result_preview) {
                        back();
                        binding.refresh(); 
                    } 
                    
                } else if (o instanceof Throwable) { 
                    MsgBox.err(o.message); 
                    asyncHandler.cancel();
                    back();
                    binding.refresh();
                    
                } else {
                    data = o;     
                    has_result_preview = true; 
                    if (data)
                        binding.fireNavigation( buildReport( data.reportdata ) ); 
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler 
        
        has_result_preview = false; 
        buildReportData(entity, asyncHandler); 
        mode = 'processing'; 
        return null; 
    } 
    
    void print() {
        asyncHandler = [
            onError: {o-> 
                MsgBox.err(o.message); 
                back();
                binding.refresh(); 
            }, 
            onTimeout: {
                asyncHandler.retry(); 
            },
            onCancel: {
                back();
                binding.refresh(); 
            }, 
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                    if (!has_result_preview) {
                        back();
                        binding.refresh(); 
                    } 
                    
                } else if (o instanceof Throwable) { 
                    MsgBox.err(o.message); 
                    asyncHandler.cancel();
                    back();
                    binding.refresh(); 
                    
                } else {
                    data = o;                
                    has_result_preview = true; 
                    printReport( data.reportdata ); 
                    back();
                    binding.refresh(); 
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler 
        
        has_result_preview = false; 
        buildReportData(entity, asyncHandler); 
        mode = 'processing'; 
    } 
    
    void printReport( data ){
        buildReport( data ); 
        ReportUtil.print( report.report, true )
    }
    
    
    void beforeBuildReport(data){}
        
    
    def buildReport( data ) {
        reportdata = data; 
        params = svcParams.getStandardParameter() + getParameters(); 
        params.WATERMARK = getInputStream( 'lgu-watermark.png' )         
        params.LOGOLGU = getInputStream( 'lgu-logo.png' ) 
        report.viewReport();
        mode = 'view'; 
        return 'preview'; 
    }
    
    String getImagePath( def imagename ) {
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
    
    InputStream getInputStream( def imagename) {
        return clientContext.classLoader.getResourceAsStream( getImagePath( imagename ) );
    }
        
    def report = [
        isAllowPrint : { allowPrint },
        isAllowSave : { allowSave },
        getReportName : { return getReportName() }, 
        getReportData : { return reportdata },
        getSubReports : { return getSubReports() },
        getParameters : { 
            return  params 
        }
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
    
    List getClassifications(){
        def m = [_schemaname: 'propertyclassification'];
        m.where =["state = 'APPROVED'"];
        m.orderBy = 'orderno';
        return querySvc.getList(m);
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
    
    def cancel() {
        asyncHandler?.cancel(); 
        return back(); 
    } 
    
    def getDays(){
        int numDays = getMonthDays()
        def list = []
        1.upto(numDays){
            list << it 
        }
        return list
    }
    
    def getMonthDays(){
        if (entity.year == null || entity.month == null) 
            return [];
            
        int yr = entity.year
        int mon = entity.month.index - 1
        int day = 1;

        Calendar cal = new GregorianCalendar(yr, mon, day);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    def getRevisionyears() {
        def m = [_schemaname: 'rysetting_land'];
        m.select = 'ry';
        m.where =['1=1'];
        m.orderBy = 'ry desc';

        def list = querySvc.getList(m);
        if (list) {
            return list.ry.unique();
        }
        return [];
    }
}
