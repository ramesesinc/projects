package com.rameses.clfc.ledger;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*; 
import java.rmi.server.UID;
import java.text.*;

class LoanLedgerController extends ReportModel
{   
    @Binding
    def binding;

    @Service('LoanLedgerService')
    def svc;

    @Service('LoanLedgerReportService')
    def reportSvc;
    
    @FormId
    def getFormId() {
        if (!entity) return new UID();
        return entity.objid;
    }

    @FormTitle
    def getFormTitle() {
        def str = "";
        if (entity) str = entity.name + " - " + entity.appno;
        return str;
    }
    
    String title = "General Information";
    def entity, optionlist;
    def page = 'default';
    def rows = 30;
    
    def preview() {
        viewReport();
        page = 'preview'
        return page;
    }
    
    def back() {
        page = 'default';
        return page;
    }

    def close() {
        return '_close';
    }

    boolean getIsDefault() {
        if (page != 'default') return false;
        return true;
    }

    boolean getIsPreview() {
        if (page != 'preview') return false;
        return true;
    }
    
    def open() {
        entity.rows = rows;
        entity = svc.open(entity);
        if (!entity.rows) entity.rows = rows;
        //entity.rows = rows;
        //entity.lastpageindex = getLastPageIndex(entity);
        //println 'entity ' + entity;
        page = 'default';
        return page;
    }
    
    /*
    def getLastPageIndex( entity ) {
        def a = (entity.ledgercount/entity.rows);
        return new BigDecimal(a+'').setScale(0, BigDecimal.ROUND_CEILING);
    }
    */

    def getOpenerParams() {
        return [entity: entity];
    }
    
    def getOptionsList() {
        //println 'get options list' 
        getOpeners: {
            def list = Inv.lookupOpeners("loanledger-plugin",[entity: entity, data: entity.amnesty]);
            def item = list.find{ it.properties.plugintype == 'ledgerpreview' }
            
            def df = new DecimalFormat("#,##0.00");
            if (item && entity?.loanamount) {
                item.caption = "Ledger - " + df.format(entity.loanamount); 
            }
            
            item = list.find{ it.properties.plugintype == 'amnestypreview' }
            if (item ) {
                if (entity.amnesty) {
                    item.caption = 'Amnesty: ' + entity?.amnesty?.description;
                } else if (!entity.amnesty) {
                    list.remove(item);
                }
                /*
                if (entity.currentamnesty) {
                    def option = entity.currentamnesty.amnestyoption;
                    if (option=='FIX') {
                        item.caption = "Fix" 
                        if (entity.currentamnesty.grantedoffer.amount) {
                            item.caption += ' - ' + df.format(entity.currentamnesty.grantedoffer?.amount);
                        }
                    } else if (option=='WAIVER') {
                        item.caption = 'Waived';
                    }
                } else if (!entity.currentamnesty) {
                    list.remove(item);
                }
                */
            }
            
            /*
            def idx = 1;
            if (item?.properties?.index) idx = item.properties.index + 1;
            
            def xlist = entity?.amnestylist;
            if (!xlist) xlist = [];
            
            def opener;
            xlist.each{ o->
                opener = Inv.lookupOpener('fixpreview', [entity: entity, data: o]);
                if (o.grantedoffer?.amount) opener.caption = "Fix - " + df.format(o.grantedoffer.amount);
                list.add(idx, opener);
                idx++;
            }
            */
            /*
            list.each{ o->
                println 'index ' + o.properties.index;
                println 'caption ' + o.properties.caption;
            }
            
            list.add(2, [id: '0001', properties: [:]]);
            list.add(3, [id: '0002', properties: [:]]);
            
            println ''
            list.each{ o->
                println 'index ' + o.properties?.index;
                println 'caption ' + o.properties?.caption;
            }
            
            list = [];*/
            //println item?.properties?.caption;
            /*item = list.find{ it.properties.plugintype == 'fixpreview' }
            if (item) {
                if (!entity?.amnesty) {
                    list.remove(item);
                } else if (entity?.amnesty.grantedoffer) {
                    item.caption = "Fix - " + df.format(entity.amnesty.grantedoffer.amount);
                }
            }*/
            
            return list;
        }
    }

    def viewHistory() {
        return InvokerUtil.lookupOpener("loanledger:history", [ledgerid: entity.objid]);
    }    

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return reportSvc.getReportData(entity);
    }

    public String getReportName() {
        return "com/rameses/clfc/report/ledger/BorrowerLedger.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('BORROWERLEDGERPAYMENT', 'com/rameses/clfc/report/ledger/BorrowerLedgerPayment.jasper')
        ];
    }
}
