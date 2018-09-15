package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.reports.*;

abstract class DepositSlipPrintoutModel  {
    
    @Invoker 
    def invoker;
    
    @Controller 
    def workunit; 

    @Service('QueryService') 
    def querySvc;
    
    def entity;
    
    public abstract def getPrintCheckReport();
    public abstract def getPrintCashReport();
    
    public void print() {
        def m = [_schemaname: 'depositslip']; 
        m.findBy = [objid: entity?.objid ]; 
        def data = querySvc.findFirst( m ); 
        def v = null; 
        if ( data.deposittype == 'CASH' ) {
            v = getPrintCashReport(); 
            
        } else { 
            m._schemaname = 'checkpayment';
            m.findBy = [depositslipid: data.objid ];
            m.orderBy = "refno"; 
            data.checks = querySvc.getList( m ); 
            v = getPrintCheckReport(); 
        } 
        if ( v == null ) return; 
        
        def rptname = null; 
        if ( v instanceof List ) {
            def p = [:]; 
            p.listHandler = [
                fetchList: {
                    return v; 
                }, 
                getColumnList: {
                    return [
                        [name: 'title', caption:'Title'] 
                    ]; 
                }
            ] as BasicListModel; 

            p.onselect = { 
                rptname = it.name; 
            } 
            p.title = 'Select Report Template'; 
            Modal.show('simple_list_lookup', p );   
            
        } else if ( v instanceof String ) {
            rptname = v; 
        }
        data.each {k1,v1->
            println k1+"="+v1;
        }
        if ( !rptname ) return;
        /*
        def report = [
            getReportName : { return rptname; },
            getReportData : { return data; }, 
            getParameters : { return [:]; }
        ] as ReportModel;

        report.viewReport(); 
        ReportUtil.print( report.report, true );
        */
    }
}