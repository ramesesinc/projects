package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*  

class CrosstabReportModel { 

    @Service('AccountingCrosstabReportService') 
    def reportSvc; 
    
    def title = 'Crosstab Report';
    def mode = 'init';
    
    def query;
    def rowFields;
    def columnFields;
    def measureFields;
    def reportFields; 
    def mainGroupList;
    
    def report = null;    
    def reportTemplates = [
        [caption:'Template-A', name:'accountingreport_crosstab.jasper'] 
    ];

    void init() {
        query = [:]; 
        
        def resp = reportSvc.init(); 
        mainGroupList = resp.accountgroups;
        rowFields = (resp.rowfields ? resp.rowfields : []); 
        columnFields = (resp.columnfields ? resp.columnfields : []); 
        measureFields = (resp.measurefields ? resp.measurefields : []); 

        resolveFieldTypes( rowFields );
        resolveFieldTypes( columnFields );
        resolveFieldTypes( measureFields );
        reportFields = [];        
       
        def list = []; 
        list.addAll( rowFields ); 
        list.addAll( columnFields ); 
        list.addAll( measureFields ); 
        list.groupBy{ it.name }.each{ k,v-> 
            reportFields << v.first(); 
        }
    }
    
    def back() {
        mode = 'init'; 
        return 'default'; 
    }
    
    def preview() { 
        if ( !reportFields ) throw new Exception('No available report fields'); 
        if ( !query.template?.name ) throw new Exception('Please select a template'); 

        //def filter = criteriaList.find{( it.field?.name )}
        //if ( !filter ) throw new Exception('Please specify at least one filter'); 
        
        //query.filters = criteriaList;
        query.maingroupid = query.maingroup?.objid; 
        def resp = reportSvc.getReport( query ); 
        if ( !resp.reportparam ) resp.reportparam = [:];
        
        resp.reportparam.TITLE = getPreferredReportTitle(); 
        
        def tbl = new com.rameses.osiris2.report.CrosstabReport();
        reportFields.each{ 
            tbl.addColumn(it.caption, it.name, it.clazz); 
        } 
        tbl.setRowGroup( query.rowfield.name ); 
        tbl.setColumnGroup( query.columnfield.name ); 
        tbl.setMeasure( query.measurefield.name ); 
        
        def b = new com.rameses.osiris2.report.CrosstabReportBuilder();
        def jrpt = b.buildReport( tbl );

        report = [
            getMainReport: { return jrpt; }, 
            getReportData: { return resp.reportdata; }, 
            getParameters: { return resp.reportparam; } 
        ] as com.rameses.osiris2.reports.BasicReportModel;
        report.viewReport();
        
        mode = 'preview';         
        return 'preview';
    } 
    
    private String getPreferredReportTitle() {
        if ( query.reporttitle ) {
            return query.reporttitle; 
        } else { 
            return (''+ query.maingroup?.title +' CROSSTAB REPORT'); 
        }
    }
    
    private void resolveFieldTypes( fields ) {
        fields.each{
            if (it.type == 'date') it.clazz = java.util.Date.class; 
            else if (it.type == 'integer') it.clazz = java.lang.Number.class; 
            else if (it.type == 'double') it.clazz = java.lang.Number.class; 
            else if (it.type == 'decimal') it.clazz = java.lang.Number.class; 
            else if (it.type == 'object') it.clazz = java.lang.Object.class; 
            else it.clazz = java.lang.String.class; 
        }
    }
} 

