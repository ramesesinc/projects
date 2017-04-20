package com.rameses.gov.etracs.rptis.report.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class UnrestrictedPropertyModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('RPTReportRestrictedPropertyService') 
    def svc 
    
    String title = 'List of Unrestricted Properties'
    String reportName = 'com/rameses/gov/etracs/rptis/report/unrestricted_properties.jasper';
    
    void buildReportData(entity, asyncHandler){
        svc.getUnrestrictedProperties(entity, asyncHandler)
    }
    
    Map getParameters(){
        data.parameters.BARANGAY = entity.barangay?.name
        return data.parameters;
    }
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', preferredSize:'0,21', required:true, allowNull:false]),
                new FormControl( "combo", [caption:'Barangay', name:'entity.barangay', items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, preferredSize:'0,21', emptyText:'ALL']),
                new FormControl( "text", [caption:'Section', name:'entity.section', preferredSize:'50,19', depends:'entity.barangay', visibleWhen:'#{entity.barangay != null}']),
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Quarter', name:'entity.qtr', items:'quarters', required:true, immediate:true, preferredSize:'100,19']),
                new FormControl( "combo", [caption:'Month', name:'entity.month', items:'months', depends:'entity.qtr', expression:'#{item.name}', dynamic:true, preferredSize:'100,19']),
            ]    
        },
   ] as FormPanelModel;
}

