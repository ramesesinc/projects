package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RealPropertyTaxConsolidatedReportModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('LandTaxReportConsolidatedReportService') 
    def svc
    
    String title = 'Consolidated Report on Real Property Tax';
    
    String reportName = 'com/rameses/gov/etracs/rpt/report/landtax/consolidated_real_property_tax.jasper';
    
    def task;
    def msg;
    
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler);
    }
    
    
    def rputypes = [
        [type:'land', name:'LAND'],
        [type:'bldg', name:'BUILDING'],
        [type:'mach', name:'MACHINERY'],
        [type:'planttree', name:'PLANT/TREE'],
        [type:'misc', name:'MISCELLANEOUS ITEM'],
    ];
    
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true]),
                new FormControl( "combo", [caption:'Property', name:'entity.rputype', items:'rputypes', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "combo", [caption:'Classification', name:'entity.classification', items:'classifications', expression:'#{item.name}', emptyText:'ALL']),
            ]    
        } 
    ] as FormPanelModel;
    
}
