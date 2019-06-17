package com.rameses.gov.treasury.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class CashbookCollectorReportModel extends AsyncReportController {
    
    @Service('CashbookReportService') 
    def svc;
    
    @Script("User")
    def user;
    
    String title = 'Cashbook Report';
    String reportpath = 'com/rameses/gov/treasury/report/cashbook/';
    
    def data;
    def accounts = [];
    def funds = [];
    def months = [];
    def tag;
    
    def templates = [ 
        [code:'detail', name:'DETAIL'], 
        [code:'summary', name:'SUMMARY'] 
    ]; 
    def periods = [
        [code:'daily', name:'DAILY'],
        [code:'monthly', name:'MONTHLY'],
    ]; 
    
    def allow_multiple_fund_selection; 

    void setup( inv ) { 
        tag = inv?.properties?.tag; 
        init(); 
    } 
    
    def initReport() { 
        entity.template = templates.find{ it.code=='detail' }
        entity.period = periods.find{ it.code=='monthly' }

        def resp = svc.initReport([ tag: tag ]); 
        accounts = resp.users.collect{[ objid: it.objid, name: it.name, title: it.title, fullname: it.name, description: it.description ]} 
        accounts.unique(); 
        accounts.sort{ it.fullname } 
        funds = resp.funds; 
        months = resp.months; 
        entity.date = resp.date; 
        entity.year = resp.year; 
        
        allow_multiple_fund_selection = resp.allow_multiple_fund_selection; 
        return 'default'; 
    }

    boolean isDynamic() { 
        return true; 
    } 

    public String getReportName() {
        if ( entity.template?.code == 'summary' ) {
            return reportpath + 'cashbooksummary.jasper'
        }
        return reportpath + 'cashbook.jasper'; 
    }
  
    void buildReportData(entity, asyncHandler) {
        svc.generateReport( entity, asyncHandler );
    }
    
    void buildResult( o ) {
        data = o; 
    }

    
    def lookupFund = Inv.lookupOpener('report_fund:lookup', [multiSelect: true]); 

    def formControl = [
        getControlList: {
            def list = []; 
            list << [type:"combo", caption:'Template', name:'entity.template', required:true, items:'templates', expression:'#{item.name}', preferredSize:'100,20', captionWidth:100, allowNull: false]; 
            list << [type:"combo", caption:'Period', name:'entity.period', required:true, items:'periods', expression:'#{item.name}', preferredSize:'100,20', captionWidth:100, allowNull: false]; 
            list << [type:"date", caption:'Date', name:'entity.date', required:true, preferredSize:'100,20', captionWidth:100, depends:'entity.period', visibleWhen:'#{entity.period?.code == "daily"}']; 
            list << [type:"integer", caption:'Year', name:'entity.year', required:true, preferredSize:'100,20', captionWidth:100, depends:'entity.period', visibleWhen:'#{entity.period?.code == "monthly"}']; 
            list << [type:"combo", caption:'Month', name:'entity.month', required:true, items:'months', expression:'#{item.name}', preferredSize:'100,20', captionWidth:100, depends:'entity.period', visibleWhen:'#{entity.period?.code == "monthly"}']; 
            list << [type:"combo", caption:'Account', name:'entity.account', required:true, items:'accounts', expression:'#{item.description ? item.description : item.fullname}', preferredSize:'0,20', captionWidth:100]; 
            if ( allow_multiple_fund_selection ) {
                list << [type:"lookup", caption:'Fund', name:'entity.fund', required:true, handlerObject: lookupFund, expression:'#{item.title}', preferredSize:'0,20', captionWidth:100]; 
            } else {
                list << [type:"combo", caption:'Fund', name:'entity.fund', required:true, items:'funds', expression:'#{item.title}', preferredSize:'0,20', captionWidth:100];                 
            }
            return list;
        } 
    ] as FormPanelModel;
   
    Map getParameters() {
        return data.info;
    }    
} 