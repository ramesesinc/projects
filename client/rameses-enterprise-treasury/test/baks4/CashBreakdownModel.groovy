package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class CashBreakdownModel  {
    
    def entity;
    def total = 0;
    def cashremaining = 0;
    def handler;
    boolean editable;
    
    def oldbreakdown;
    boolean showCreditMemos = true;
    boolean showCashBreakdown = true;
    
    def checks;
    def creditMemos;
    
    void copyBreakdown() {
        if(entity.totalcash == null) entity.totalcash = 0;
        if( entity.cashbreakdown == null ) entity.cashbreakdown = [];
        //make a copy of the brakdown
        oldbreakdown = [];
        entity.cashbreakdown.each {
            def m = [:];
            m.putAll(it);
            oldbreakdown << m;
        }
    }
    
    void init() {
        copyBreakdown();
        checks = handler.getChecks();
        creditMemos = handler.getCreditMemos();
        if( !creditMemos ) {
            showCreditMemos = false;
        }
        if( entity.totalcash <=0 && !checks  ) {
            showCashBreakdown = false;
        }
    }

    def checkModel = [
        fetchList: { o->
            return checks;
        }
    ] as BasicListModel;

    def creditMemoModel = [
        fetchList: { o->
            return creditMemos;
        }
    ] as BasicListModel;
    
    def doOk() {
        def breakdown = 0;
        if( entity.cashbreakdown ) {
            breakdown = entity.cashbreakdown.sum{ it.amount };
        }
        def diff = (entity.totalcash - breakdown);
        if( diff  != 0 )
            throw new Exception("Cash breakdown must equal total cash");
        
        handler.update(entity);
        return "_close";
    }
    
    def doCancel() {
        entity.cashbreakdown = oldbreakdown;
        return "_close";
    }
    
}       