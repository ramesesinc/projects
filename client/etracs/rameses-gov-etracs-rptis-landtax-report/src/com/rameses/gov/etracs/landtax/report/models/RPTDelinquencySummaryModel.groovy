package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RPTDelinquencySummaryController extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('LandTaxReportDelinquencyService') 
    def svc
    
    String title = 'Realty Tax Delinquency Summary';
    String path = 'com/rameses/gov/etracs/rpt/report/landtax/';
    
    def data;
    def task;
    def msg;
    
    void buildReportData(entity, asyncHandler){
        entity.reporttype = entity.format.reporttype 
        svc.generateDelinquencyReport(entity, asyncHandler);
    }
    
    String getReportName(){
        return path + entity.format.reportname;
    }
    
    Map getParameters(){
        def map = [:]
        map.PERIODNAME = 'Computed as of ' + (new java.text.SimpleDateFormat('MMMMM dd, yyyy').format(reportdata[0].dtgenerated));
        if (entity.format.reporttype == 'summary') {
            map.TITLE = 'SUMMARY OF REALTY TAX DELINQUENCY';
            map.SUBTITLE = entity.period?.caption + ' '  + entity.year 
        }
        else {
            map.TITLE = 'CERTIFIED LIST OF ALL REAL PROPERTY TAX DELINQUENCIES';
            map.SUBTITLE = 'As of December 31, ' + entity.year 
        }
        return map;
    } 
    
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', required:true, items:'lgus', expression:'#{item.name}', allowNull:false]),
                new FormControl( "combo", [captionWidth:110,caption:'Report Format', name:'entity.format', required:true, items:'formats', expression:'#{item.caption}', allowNull:false]),
                new FormControl( "combo", [captionWidth:110,caption:'Period', name:'entity.period', required:true, items:'periods', expression:'#{item.caption}', allowNull:false, depends:'entity.format', dynamic:true]),
                new FormControl( "integer", [captionWidth:110,caption:'Year', name:'entity.year', required:true]),
            ]    
        } 
    ] as FormPanelModel;
    
    def getPeriods(){
        def p = []
        p << [code:'FOR_THE_YEAR', caption:'FOR THE YEAR']
        p << [code:'AS_OF_YEAR', caption:'AS OF YEAR']
        return p;
    }
    
    def formats = [
        [reporttype:'summary', caption:'STANDARD', reportname:'rptdelinquency_summary.jasper'],
        [reporttype:'certifiedlist', caption:'CERTIFIED LIST', reportname:'rptdelinquency_certified_list.jasper'],
        [reporttype:'byclassification', caption:'BY CLASSIFICATION', reportname:'rptdelinquency_classification.jasper'],
    ]
}