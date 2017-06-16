package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class PdapRpta100ReportModel extends AsyncReportModel
{
    @Service('RPTReportPDAPRPTA100Service') 
    def svc 
    
    String title = 'PDAP-RPTA 100 Report'
    
    String reportName = 'com/rameses/gov/etracs/rptis/reports/pdaprpta100.jasper';
    
        
    def initReport(){
        entity.qtr = null
        return 'default'
    }
    
    void buildReportData(entity, asyncHandler){
        svc.generatePdapRpta100(entity, asyncHandler)
    }
    
    Map getParameters(){
        return data.parameters;
    }
    
    
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [caption:'LGU', name:'entity.lgu', required:true, items:'lgus', expression:'#{item.name}', allowNull:false]),
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Month', name:'entity.month', items:'months', depends:'entity.qtr', expression:'#{item.caption}', required:true, dynamic:true, preferredSize:'100,19', allowNull:false]),
            ]    
        },
   ] as FormPanelModel;
}