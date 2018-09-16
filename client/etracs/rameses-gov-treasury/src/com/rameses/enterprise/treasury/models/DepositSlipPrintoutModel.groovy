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
    
    public def formatData( def data, def option ) {
        return data;
    };
    
    public abstract def getPrintCheckReport();
    public abstract def getPrintCashReport();
    public def getParameters() {return [:]}
    
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
        def printOption = null;
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
                printOption = it;
            } 
            p.title = 'Select Report Template'; 
            Modal.show('simple_list_lookup', p );   
            
        } else if ( v instanceof String ) {
            rptname = v; 
        }
        if ( !rptname ) return;
        
        def report = [
            getReportName : { return rptname; },
            getReportData : { return formatData( data, printOption ); }, 
            getParameters : { return getParameters(); }
        ] as ReportModel;

        report.viewReport(); 
        ReportUtil.print( report.report, true );
    }
    
    public def formatOldReport( def d ) {
        def m = [:];
        m.bankacctno  = d.bankaccount.code;
        m.branchname = d.bankaccount.bank?.branchname;
        m.bankacctname = d.bankaccount.title;
        m.txndate = d.depositdate;
        m.accttype = d.deposittype;
        m.noncash = d.totalcheck;
        m.checktype = d.checktype;
        m.depositedbyname = d.createdby.name;
        m.cash = d.totalcash;
        if(d.checks) {
            m.noncashpayments = d.checks.collect {
                [bank: it.bank?.name, checkno: it.refno, amount: it.amount ]
            }
        }
        if( d.cashbreakdown ) {
            m.dqty1000="0";
            m.dqty500="0";
            m.dqty200="0";
            m.dqty100="0";
            m.dqty50="0";
            m.dqty20="0";
            m.dqty5="0";
            m.dqty1="0";
            m.damt1000=0.0;
            m.damt500=0.0;
            m.damt200=0.0;
            m.damt100=0.0;
            m.damt50=0.0;
            m.damt20=0.0;
            m.damt5=0.0;
            m.damt1=0.0;
            m.damtCoins = 0.0;
            d.cashbreakdown.each {
                if( it.denomination == 1000 ) { m.dqty1000 = it.qty+""; m.damt1000 = it.amount; }
                else if( it.denomination == 500 ) { m.dqty500 = it.qty+""; m.damt500 = it.amount; }
                else if( it.denomination == 200 ) { m.dqty200 = it.qty+""; m.damt200 = it.amount; }
                else if( it.denomination == 100 ) { m.dqty100 = it.qty+""; m.damt100 = it.amount; }
                else if( it.denomination == 50 ) { m.dqty50 = it.qty+""; m.damt50 = it.amount; }
                else if( it.denomination == 20 ) { m.dqty20 = it.qty+""; m.damt20 = it.amount; }
                else if( it.denomination == 5 ) { m.dqty5 = it.qty+""; m.damt5 = it.amount; }
                else if( it.denomination == 1 ) { m.dqty1 = it.qty+""; m.damt1 = it.amount; }
                else {
                    m.damtCoins += it.amount;
                }    		
            }
        }        
        return m;
    } 
    
}