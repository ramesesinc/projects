package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RestrictionAVSummaryModel extends AsyncReportModel 
{
    @Service('RPTReportFAASRestrictionAVSummaryService') 
    def svc 
    
    String title = 'Assessed Value of Properties with Restrictions'
    
    String reportPath = 'com/rameses/gov/etracs/rptis/reports/';
    String reportName = reportPath + 'faas_restriction_av_summary.jasper';
    
    void buildReportData(entity, asyncHandler){
        svc.generateReport(entity, asyncHandler);
    }
    
    Map getParameters(){
        data.parameters.OVERRIDE_COMPARATOR = [
            compare : {c1, c2 ->
                return c1.idx <=> c2.idx;
            }
        ] as java.util.Comparator;
        
        return data.parameters;
    }    
    
    def periods = [
        [code:'PERIOD', caption:'PERIOD'],
    ];   
    
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:100, caption:'Report Type', name:'entity.period', items:'periods', required:true, allowNull:false, preferredSize:'0,21', expression:'#{item.caption}']),
                new FormControl( "integer", [captionWidth:100, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:100, caption:'Quarter', name:'entity.qtr', items:'quarters', required:true, immediate:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:100, caption:'Month', name:'entity.month', items:'months', depends:'entity.qtr', expression:'#{item.name}', dynamic:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:100, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "combo", [captionWidth:100, caption:'Barangay', name:'entity.barangay', items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, preferredSize:'0,21', emptyText:'ALL']),
                new FormControl( "separator", [preferredSize:'0,10']),
                new FormControl( "checkbox", [showCaption:false, name:'entity.hidezeroav', text:' Hide Restrictions with Zero Assessed Values?']),
            ]    
        },
   ] as FormPanelModel;
      
}
