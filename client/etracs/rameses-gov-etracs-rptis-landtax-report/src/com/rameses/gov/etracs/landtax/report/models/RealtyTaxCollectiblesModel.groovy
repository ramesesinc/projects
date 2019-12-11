package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.report.models.*;

class RealtyTaxCollectiblesModel extends AsyncReportModel 
{
    @Service('LandTaxReportRealtyTaxCollectibleService') 
    def svc
    
    String title = 'Real Property Tax Collectibles';
    String getReportName(){
        return 'com/rameses/gov/etracs/rpt/report/landtax/realty_tax_collectibles.jasper'
    } 
    
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler);
    }
    
    def initReport(){
        entity.basicrate = 1.0
        entity.basicdiscrate = 10.0
        entity.sefrate = 1.0
        entity.sefdiscrate = 10.0
        return 'default'
    }
    

    def formControl = [
         getFormControls: {
             return [
                 new FormControl( "label", [caption:'Year', name:'entity.year', required:true, enabled:false, captionWidth:100]),
                 new FormControl( "decimal", [caption:'Basic (%)', name:'entity.basicrate', required:true, captionWidth:100, scale:4, pattern:'##0.0000']),
                 new FormControl( "decimal", [caption:'Discount(%)', name:'entity.basicdiscrate', required:true, captionWidth:100, scale:4, pattern:'##0.0000']),
                 new FormControl( "decimal", [caption:'SEF (%)', name:'entity.sefrate', required:true, captionWidth:100, scale:4, pattern:'##0.0000']),
                 new FormControl( "decimal", [caption:'Discount (%)', name:'entity.sefdiscrate', required:true, captionWidth:100, scale:4, pattern:'##0.0000']),
             ]    
         },
    ] as FormPanelModel;

}