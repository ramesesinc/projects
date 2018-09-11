package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class AEEMMReportModel extends AsyncReportModel2
{
    @Service('RPTReportAEEMMService') 
    def svc 
    
    String title = 'AEEMM Form No. 2 Report'
    String reportName = 'com/rameses/gov/etracs/rpt/report/aeemm.jasper';
        
    def initReport(){
        entity.qtr = null;
        return 'default';
    }
            
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler)
    }
    
    Map getParameters(){
        return data.parameters;
    }
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:110, caption:'Revision Year', name:'entity.ry', items:'revisionyears', emptyText:'ALL']),
                new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'Month', name:'entity.month', items:'months', depends:'entity.year', expression:'#{item.caption}', dynamic:true, preferredSize:'100,19']),
            ]
        },
   ] as FormPanelModel;
   
}