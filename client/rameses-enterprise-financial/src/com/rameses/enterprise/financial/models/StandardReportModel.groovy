package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.* 

class StandardReportModel { 

    @Service('QueryService') 
    def querySvc; 

    @Service('AccountingStandardReportService') 
    def reportSvc; 
    
    def title = 'Standard Report';
    def query = [:];
    def fields = [];
    def mode = 'init';
    
    def report = null;    
    def reportpath = 'com/rameses/enterprise/accounting/reports/';
    
    def reportTypes = ['standard', 'extended', 'details'];
    
    def reportTemplates = [
        [caption:'Template-A', name:'accountingreport_standard_a.jasper'], 
        [caption:'Template-B', name:'accountingreport_standard_b.jasper'] 
    ]; 
    
    def postingTypes = [
        [caption: 'By Remittance Date', objid: 'BY_REMITTANCE_DATE'],
        [caption: 'By Liquidation Date', objid: 'BY_LIQUIDATION_DATE'] 
    ]; 

    void init() { 
        query = [:]; 
        query.hidenoactualvalue = true; 
        
        def resp = reportSvc.init(); 
        fields = ( resp.fields ? resp.fields : []); 
    } 
    

    def _maingroups;
    def getMainGroupList() {
        if ( _maingroups == null ) {
            _maingroups = querySvc.getList([ _schemaname:'account_maingroup', orderBy:'title', where:[' 1=1 '] ]); 
        }
        return _maingroups; 
    }
    
    
    def criteriaList = [];
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
    
    
    void show() {
        MsgBox.alert( criteriaList );
    }
    
    def preview() { 
        if ( !query.template?.name ) throw new Exception('Please select a template'); 

        def filter = criteriaList.find{( it.field?.name )}
        if ( !filter ) throw new Exception('Please specify at least one filter'); 
        
        query.filters = criteriaList;
        query.maingroupid = query.maingroup?.objid; 
        def resp = reportSvc.getReport( query ); 
        report = [
            getReportName: { return reportpath + query.template.name; },
            getReportData: { return resp.reportdata; }, 
            getParameters: { 
                if ( query.reporttitle ) {
                    resp.reportparam.TITLE = query.reporttitle; 
                }
                return resp.reportparam; 
            } 
        ] as ReportModel;
        report.viewReport();
        
        mode = 'preview';         
        return 'preview';
    } 
    def back() {
        mode = 'init'; 
        return 'default'; 
    }
} 

