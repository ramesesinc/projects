package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*


public class FixLedgerModel 
{
    @Caller
    def caller;
    
    @Service('RPTLedgerService')
    def svc;
    
    
    @Binding
    def binding;
    
    def entity;
    
    String title = 'Fix Ledger Information'
    
    def info = [:]
    def quarters = [1,2,3,4];
    
    @PropertyChangeListener
    def listener = [
        'info.basicpaid' : {info.sefpaid = info.basicpaid},
        'info.basicintpaid' : {info.sefintpaid = info.basicintpaid},
        'info.basicdisctaken' : {info.sefdisctaken = info.basicdisctaken},
    ]
    
    void init(){
        info.objid = entity.objid
        info.taxpayer = entity.taxpayer
        info.lastyearpaid = entity.lastyearpaid
        info.lastqtrpaid = entity.lastqtrpaid
        info.tdno = entity.tdno;
        info.prevtdno = entity.prevtdno;
        info.fullpin = entity.fullpin;
        info.taxable = entity.taxable;
        info.basicpaid = 0;
        info.sefpaid = 0;
    }
    
    
    def fixLedger(){
        if (MsgBox.confirm('Fix this Ledger?')){
            svc.fixLedger(info)
            caller.reloadEntity();
            caller.refreshSections();
            return '_close'
        }
    }
        
    def getPartialperiod(){
        def year = info.lastqtrpaid == 4 ? info.lastyearpaid + 1 : info.lastyearpaid
        def qtr = info.lastqtrpaid == 4 ? 1 : info.lastqtrpaid + 1
        
        info.partialledyear = year;
        info.partialledqtr = qtr;
        
        def sqtr = '1st Quarter';
        if (qtr == 2) sqtr = '2nd Quarter';
        else if (qtr == 3) sqtr = '3rd Quarter';
        else if (qtr == 4) sqtr = '4th Quarter';
        
        def msg = 'Partial payment will be applied ';
        msg += 'on the ' + sqtr + ' of ' + year;
    }

}

