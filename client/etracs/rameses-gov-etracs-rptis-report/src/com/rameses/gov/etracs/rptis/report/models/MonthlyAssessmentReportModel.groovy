package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;


class MonthlyAssessmentReportModel extends AsyncReportModel2
{
    @Service('RPTReportAssessmentReportService') 
    def svc
    
    String title = 'Monthly Assessment Report';
    String getReportName(){
        return 'com/rameses/gov/etracs/rptis/reports/' + entity.reportformat.reportname;
    } 
    
    
    def initReport(){
        entity.qtr = null;
        return 'default';
    }
    
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler);
    }
    
    def valuetypes = ['MV', 'AV']
    
    def formControl = [
         getFormControls: {
             return [
                 new FormControl( "combo", [captionWidth:110, caption:'Revision Year', name:'entity.ry', items:'revisionyears', emptyText:'ALL']),
                 new FormControl( "combo", [captionWidth:110, caption:'Report Format', name:'entity.reportformat', items:'reportformats', expression:'#{item.caption}', required:true, allowNull:false]),
                 new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', required:true, allowNull:false]),
                 new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                 new FormControl( "combo", [captionWidth:110, caption:'Month', name:'entity.month', items:'months', expression:'#{item.caption}', required:true, preferredSize:'100,19']),
                 new FormControl( "combo", [captionWidth:110, caption:'Value', name:'entity.valuetype', items:'valuetypes', required:true, allowNull:false, preferredSize:'100,19']),
             ]    
         },
    ] as FormPanelModel;

    def reportformats = [
        [type:'standard', caption:'STANDARD', reportname:'assessment_report.jasper'],
        [type:'lift', caption:'LIFT', reportname:'assessment_report_lift.jasper'],
    ]
    
}