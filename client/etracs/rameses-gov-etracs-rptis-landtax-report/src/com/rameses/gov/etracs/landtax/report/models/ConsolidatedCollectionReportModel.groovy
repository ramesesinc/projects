package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.report.models.*;

class ConsolidatedCollectionReportModel extends AsyncReportModel
{
    @Service('LandTaxReportConsolidatedCollectionService') 
    def svc
    
    String title = 'Consolidated Report on Updated Real Properties';
    
    String getReportName(){
        return 'com/rameses/gov/etracs/landtax/reports/consolidated_collection.jasper';
    }
    
    def initReport(){
        entity.qtr = null;
        entity.month = null;
        return 'default'
    }
    
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler);
    }
    
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [caption:'Posting Type', name:'entity.postingtype', required:true, items:'postingtypes', expression:'#{item.caption}', allowNull:false, captionWidth:110]),
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, captionWidth:110]),
                new FormControl( "combo", [caption:'Type', name:'entity.rputype', required:true, items:'rputypes', expression:'#{item.caption}', allowNull:false, captionWidth:110]),
                new FormControl( "combo", [caption:'Classification', name:'entity.classification', required:true, items:'classifications', expression:'#{item.name}', allowNull:false, captionWidth:110]),
            ]    
        } 
    ] as FormPanelModel;
    
    def postingtypes = [
        [code:'byliq', caption:'By Liquidation Date'],
        [code:'byrem', caption:'By Remittance Date'],
    ]
    
    def rputypes = [
        [type:'land', caption:'Land'],
        [type:'bldg', caption:'Building'],
        [type:'mach', caption:'Machinery'],
        [type:'planttree', caption:'Plants/Trees'],
        [type:'misc', caption:'Miscellaneous'],
    ]
    
    List getClassifications(){
        svc.getClassifications()
    }
    
}