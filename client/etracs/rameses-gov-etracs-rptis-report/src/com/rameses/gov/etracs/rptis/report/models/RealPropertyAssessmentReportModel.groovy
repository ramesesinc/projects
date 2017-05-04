package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RealPropertyAssessmentReportModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('RPTReportRPAService') 
    def svc 
    
    @Service('QueryService')
    def querySvc;    
    
    @Binding 
    def binding;
    
    String title = 'Report on Real Property Assessment'
    boolean showClassification = true;
    boolean showRate = false;
    
    String reportName = 'com/rameses/gov/etracs/rpt/report/reportonrpa.jasper';
    
            
    def initReport(){
        showRate = true;
        entity.basicrate = 1.0
        entity.sefrate = 1.0 
        return 'default';
    }
    
    void buildReportData(entity, asyncHandler){
        svc.generateReportOnRPA(entity, asyncHandler); 
    }
    
    Map getParameters(){
        data.parameters.LOGOLGU = getInputStream('lgu-logo.png');
        return data.parameters;
    }
    
   def getReporttypes(){
        return [
            [type:'period', caption:'PERIOD'],
            [type:'asof', caption:'AS OF DATE'],
        ]
   }    
   
    def getPropertyClassifications(){
        def q = [_schemaname:'propertyclassification', where:['1=1'], orderBy:'orderno']
        return querySvc.getList(q)
    }   

}
