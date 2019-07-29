package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class FAASMonitoringReportModel extends AsyncReportModel
{
    @Binding
    def binding;

    @Service('RPTReportFaasMonitoringService') 
    def svc;
    
    String title = 'FAAS Encoding Monitoring'
    String path = 'com/rameses/gov/etracs/rptis/report/forms/';
    String reportName = path + 'faas_monitoring.jasper'
    
    def initReport() {
        entity.qtr = null;
        return 'default';
    }    

    Map getParameters() {
        return data.parameters;
    }

    def getUsers() {
        return svc.getUsers(entity);
    }
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Month', name:'entity.month', required:true, items:'months', depends:'entity.year', expression:'#{item.caption}', dynamic:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Day', name:'entity.day', items:'days', depends:'entity.month', dynamic:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'User', name:'entity.user', items:'users', dynamic:true, depends: 'entity.(year|month|qtr|day)', expression:'#{item.username}', preferredSize:'250,19']),
            ]
        },
   ] as FormPanelModel;
   
    void buildReportData(entity, asyncHandler) {
        svc.buildReport(entity, asyncHandler); 
    }
}