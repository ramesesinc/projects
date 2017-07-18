package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;


class FAASValuationRestrictionSummaryModel extends AsyncReportModel
{
    @Service('RPTReportFAASValuationAndRestrictionService') 
    def svc 
    
    String title = 'FAAS Valuation and Restriction Summary'
    
    String reportPath = 'com/rameses/gov/etracs/rptis/reports/';
    String reportName = reportPath + 'faas_valuation_restriction_summary.jasper';
    
    def initReport(){
        entity.basicrate = 1.0;
        entity.sefrate = 1.0;
        return 'default';
    }
    
    void buildReportData(entity, asyncHandler){
        svc.generateReport(entity, asyncHandler);
    }
    
    Map getParameters(){
        return data.parameters;
    }    
    
    def periods = [
        [code:'PERIOD', caption:'PERIOD'],
    ];   
    
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:100, caption:'Report Type', name:'entity.period', items:'periods', required:true, allowNull:false, preferredSize:'0,21', expression:'#{item.caption}']),
                new FormControl( "combo", [captionWidth:100, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText:'ALL', allowNull:true]),
                new FormControl( "integer", [captionWidth:100, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:100, caption:'Quarter', name:'entity.qtr', items:'quarters', required:true, immediate:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:100, caption:'Month', name:'entity.month', items:'months', depends:'entity.qtr', expression:'#{item.name}', dynamic:true, preferredSize:'100,19']),
                new FormControl( "separator", [preferredSize:'0,10']),                
                new FormControl( "decimal", [captionWidth:100, caption:'Basic (%)', name:'entity.basicrate', required:true, preferredSize:'100,19']),
                new FormControl( "decimal", [captionWidth:100, caption:'SEF (%)', name:'entity.sefrate', required:true, preferredSize:'100,19']),
            ]    
        },
   ] as FormPanelModel;
      
}
