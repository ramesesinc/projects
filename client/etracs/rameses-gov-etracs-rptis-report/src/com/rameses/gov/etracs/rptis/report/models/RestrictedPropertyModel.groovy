package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class RestrictedPropertyModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('RPTReportRestrictedPropertyService') 
    def svc 

    @Service('QueryService') 
    def querySvc 
    
    String title = 'List of Restricted Properties'
    String reportName = 'com/rameses/gov/etracs/rptis/reports/restricted_properties.jasper';

    def initReport(){
        entity.month = null;
        entity.qtr = null;
        return 'default'
    }
    
    void buildReportData(entity, asyncHandler){
        svc.getRestrictedProperties(entity, asyncHandler)
    }
    
    Map getParameters(){
        data.parameters.BARANGAY = entity.barangay?.name
        return data.parameters;
    }
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', preferredSize:'0,21', required:true, allowNull:false]),
                new FormControl( "combo", [captionWidth:110, caption:'Barangay', name:'entity.barangay', items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, preferredSize:'0,21', emptyText:'ALL']),
                new FormControl( "combo", [captionWidth:110, caption:'Restriction', name:'entity.restrictiontype', items:'restrictions', expression:'#{item.name}', preferredSize:'0,21', emptyText:'ALL']),
            ]    
        },
   ] as FormPanelModel;


   def getRestrictions() {
        return querySvc.getList([
            _schemaname: 'faas_restriction_type',
            orderBy: 'idx',
            where: ['1=1']
        ])
   }
}

