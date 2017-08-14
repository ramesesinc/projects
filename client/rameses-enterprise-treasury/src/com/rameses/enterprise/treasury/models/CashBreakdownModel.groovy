package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class CashBreakdownModel  {
    
    //@Service("RemittanceService")
    //def service;
    
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
    
    void init() {
        if( entity.totalcash == null || entity.cashbreakdown == null )
            throw new Exception("Total cash is null. Please run migration for fund");
        oldbreakdown = entity.cashbreakdown;
        
        entity.cashbreakdown = handler.getCashBreakdown();
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
        if( diff  > 0.05 )
            throw new Exception("Cash to remit is insufficient. Please review your collection");
        if( diff < -0.05 )
            throw new Exception("Please review your collection. You have over declared the cash breakdown");
        
        handler.update(entity);
        return "_close";
    }
    
    def doCancel() {
        entity.cashbreakdown = oldbreakdown;
        return "_close";
    }
    
}       