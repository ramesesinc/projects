package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*  

class CrosstabReportModel { 

    @Service('AccountingCrosstabReportService') 
    def reportSvc; 
    
    @Binding 
    def binding;
    
    def title = 'Crosstab Report';
    def mode = 'init';
    
    def query;
    def rowFields;
    def columnFields;
    def measureFields;
    def reportFields; 
    def mainGroupList;
    
    def report;    
    def option = [:]; 
    def selectedRowField;
    def orientationList = ["Landscape", "Portrait"];

    def fields = [];
    def criteriaList = [];

    void init() {
        query = [:]; 
        option = [rowfields: []]; 
        
        def resp = reportSvc.init(); 
        mainGroupList = resp.accountgroups;
        fields = (resp.fields ? resp.fields: []);
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
    
    def criteriaHandler = [
        getFieldList: { 
            return fields; 
        },
        add: { o->
            criteriaList << o; 
        },
        remove: {
            criteriaList.remove(o);
        },
        clear: { 
            criteriaList.clear(); 
        }
    ];     
    
    def listHandler = [
        fetchList: { o-> 
            return option.rowfields; 
        },
        removeItem: { o-> 
            option.rowfields.remove(o); 
        }
    ] as ListPaneModel;
    
    void addRowField() {
        if ( !option.rowfield ) return; 
        
        def o = option.rowfields.find{( it.name==option.rowfield.name )}
        if ( o ) return;
        
        option.rowfields << option.rowfield;
        listHandler.reload();
    }
    
    def back() {
        mode = 'init'; 
        return 'default'; 
    }
    
    def preview() { 
        if ( !reportFields ) throw new Exception('No available report fields'); 
        if ( !option.rowfields ) throw new Exception('Please specify at least one row field'); 
        
        def filter = criteriaList.find{( it.field?.name )}
        if ( !filter ) throw new Exception('Please specify at least one filter'); 
        
        
        query.filters = criteriaList;
        query.maingroupid = query.maingroup?.objid; 
        def resp = reportSvc.getReport( query ); 
        if ( !resp.reportparam ) resp.reportparam = [:];
        
        resp.reportparam.TITLE = getPreferredReportTitle(); 
        
        def tbl = new com.rameses.osiris2.report.CrosstabReport();
        tbl.orientation = option.orientation;
        tbl.title = getPreferredReportTitle();
        
        reportFields.each{ 
            tbl.addColumn(it.caption, it.name, it.clazz); 
        } 
        
        option.rowfields.each{
            tbl.addRowGroup( it.name ); 
        }
        
        tbl.addColumnGroup( option.columnfield.name ); 
        tbl.addMeasure( option.measurefield.name ); 
        
        def ftype = option.measurefield.type;
        def conf = tbl.getMeasureGroup( option.measurefield.name );    
        if ( ftype.toString().matches('decimal|double')) {
            conf.alignment = 'Right'; 
            conf.pattern = '#,##0.00'; 
        } else if ( ftype == 'integer' ) {
            conf.alignment = 'Center';
            conf.pattern = '#,##0';
        } else if ( ftype == 'date' ) {
            conf.alignment = 'Center';
        }
        
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
        if ( option.reporttitle ) {
            return option.reporttitle; 
        } else { 
            return (''+ query.maingroup?.title +' CROSSTAB REPORT'); 
        }
    }
    
    private void resolveFieldTypes( fields ) {
        fields.each{
            if (it.type == 'date') it.clazz = java.util.Date.class; 
            else if (it.type == 'integer') it.clazz = java.lang.Integer.class; 
            else if (it.type == 'double') it.clazz = java.lang.Double.class; 
            else if (it.type == 'decimal') it.clazz = java.math.BigDecimal.class; 
            else if (it.type == 'object') it.clazz = java.lang.Object.class; 
            else it.clazz = java.lang.String.class; 
        }
    }
} 

