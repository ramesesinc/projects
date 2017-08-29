package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
        
public abstract class CashBreakdownModel2 extends CrudFormModel {
    
    def total = 0;
    def cashremaining = 0;
    def handler;
    boolean editable;
    
    def oldbreakdown;
    boolean showCreditMemos = true;
    boolean showCashBreakdown = true;
    
    def checkslist;
    def creditmemolist;
    
    abstract def getChecks();
    abstract def getCreditMemos();
    
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
    
    void afterInit() {
        copyBreakdown();
        checkslist = getChecks();
        creditmemolist = getCreditMemos();
        if( !creditmemolist ) {
            showCreditMemos = false;
        }
        if( entity.totalcash <=0 && !checkslist  ) {
            showCashBreakdown = false;
        }
    }

    def checkModel = [
        fetchList: { o->
            return checkslist;
        }
    ] as BasicListModel;

    def creditMemoModel = [
        fetchList: { o->
            return creditmemolist;
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
        
        update(entity);
        return "_close";
    }
    
    def doCancel() {
        entity.cashbreakdown = oldbreakdown;
        return "_close";
    }
    
}       