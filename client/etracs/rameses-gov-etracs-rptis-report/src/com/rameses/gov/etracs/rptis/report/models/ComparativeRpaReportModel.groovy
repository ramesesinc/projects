package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class ComparativeRpaReportModel extends AsyncReportModel2
{
    @Service('RPTComparativeRpaReportService') 
    def svc 
    
    String title = 'Comparative Report on Real Property Assessment'
    
    String reportName = 'com/rameses/gov/etracs/rptis/reports/comparative_rpa.jasper';
    
        
    void buildReportData(entity, asyncHandler){
        svc.getReport(entity, asyncHandler)
    }
    
    Map getParameters(){
        return data.parameters;
    }

    def quarters1 = [1,2,3];

    def taxabilities = ['TAXABLE', 'EXEMPT'];
    def valuetypes = ['MV', 'AV'];

    def getQuarters2() {
        def qtrs = [2,3,4];
        return qtrs.findAll{ it > entity.qtr1};
    }


   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:110, caption:'Revision Year', name:'entity.ry', items:'revisionyears', emptyText:'ALL']),
                new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText: 'ALL']),
                new FormControl( "combo", [captionWidth:110, caption:'Taxability', name:'entity.taxability', items:'taxabilities', allowNull: false]),
                new FormControl( "combo", [captionWidth:110, caption:'Value Type', name:'entity.valuetype', items:'valuetypes', allowNull: false]),

                new FormControl( "separator", [preferredSize:'0,21']),

                new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'Compare Qtr', name:'entity.qtr1', items:'quarters1', required:true, immediate:true, allowNull:false, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'To Qtr', name:'entity.qtr2', items:'quarters2', required:true, immediate:true, depends:'entity.qtr1', dynamic:true, allowNull:false, preferredSize:'100,19']),
            ]    
        },
   ] as FormPanelModel;
}