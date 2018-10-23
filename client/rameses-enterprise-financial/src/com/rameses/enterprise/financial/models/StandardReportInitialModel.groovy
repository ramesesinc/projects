package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.* 

class StandardReportInitialModel { 

    @Service('QueryService') 
    def querySvc; 

    @Service('AccountingStandardReportService') 
    def reportSvc; 
    
    def title = 'Standard Report';
    def query = [:];
    def fields = [];
    def mode = 'init';
    
    def report = null;    
    def reportTypes = ['standard', 'details', 'itemaccount'];
    
    def reportTemplate;
    def reportTemplates;
    def mainGroupList;
    
    def postingTypes = [
        [caption: 'By Liquidation Date', objid: 'BY_LIQUIDATION_DATE'] 
    ]; 

    void init() { 
        query = [:]; 
        query.hidenoactualvalue = true; 
        def resp = reportSvc.init(); 
        fields = ( resp.fields ? resp.fields : []); 
        reportTemplates = Inv.lookupOpeners( "financial:report_template:standard" ).collect{
            [name:it.properties.name, caption: it.caption]
        };
        mainGroupList = querySvc.getList([ _schemaname:'account_maingroup', orderBy:'title', where:[' 1=1 '] ]);
    } 
    
    def criteriaList = [];
    def criteriaHandler = [
        getFieldList: { 
            return fields; 
        },
        add: { o->
            criteriaList << o; 
        },
        removeItem: {
            def z = criteriaList.last();
            criteriaList.remove(z);
        },
        clear: { 
            criteriaList.clear(); 
        }
    ]; 
    
    
    void show() {
        MsgBox.alert( criteriaList );
    }
    
    def preview() { 
        def qry = [:];
        qry.putAll( query );
        if( criteriaList ) {
            def cl = [];
            criteriaList.each {
                if(it.field) {
                    def cmm = [:];
                    cmm.field = it.field;
                    cmm.operator = it.get( it.field.type + "operator" ).key;
                    cmm.value = it.value;
                    if(it.value2) cmm.value2 = it.value2;
                    cl << cmm;
                }
            }
            qry.criteriaList = cl;
        }
        if(!reportTemplate) throw new Exception("Please select a report template");
        return Inv.lookupOpener( "financial:report_template:standard:" + reportTemplate.name, [query: qry] );
    } 
    
    def back() {
        mode = 'init'; 
        return 'default'; 
    }
} 

