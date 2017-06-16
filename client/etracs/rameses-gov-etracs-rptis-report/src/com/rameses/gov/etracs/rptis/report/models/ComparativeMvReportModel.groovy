package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class ComparativeMvReportModel extends AsyncReportModel
{
    @Service('RPTReportComparativeMVService') 
    def svc 
    
    String title = 'Comparative Data on Market Value'
    
    String reportName = 'com/rameses/gov/etracs/rptis/reports/comparativemv.jasper';
    
        
    void buildReportData(entity, asyncHandler){
        svc.generateComparativeMV(entity, asyncHandler)
    }
    
    Map getParameters(){
        return data.parameters;
    }
    
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "combo", [caption:'Barangay', name:'entity.barangay', items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, preferredSize:'0,21', emptyText:'ALL']),
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Quarter', name:'entity.qtr', items:'quarters', required:true, immediate:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Month', name:'entity.month', items:'months', depends:'entity.qtr', expression:'#{item.caption}', dynamic:true, preferredSize:'100,19']),
            ]    
        },
   ] as FormPanelModel;
}