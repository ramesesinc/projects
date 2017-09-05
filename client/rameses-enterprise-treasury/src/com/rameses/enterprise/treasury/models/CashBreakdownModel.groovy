package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
        
public abstract class CashBreakdownModel extends CrudFormModel {
    
    def total = 0;
    def cashremaining = 0;
    boolean editable = true;
    
    boolean showCreditMemos = true;
    boolean showCashBreakdown = true;
    
    def checkslist;
    def creditmemolist;
    
    abstract def getChecks();
    abstract def getCreditMemos();
    void afterUpdate() {}
    
    void afterOpen() {
        if(entity.totalcash == null) entity.totalcash = 0;
        if( entity.cashbreakdown == null ) entity.cashbreakdown = [];
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
        if(!editable) return "_close";
        def breakdown = 0;
        if( entity.cashbreakdown ) {
            breakdown = entity.cashbreakdown.sum{ it.amount };
        }
        def diff = (entity.totalcash - breakdown);
        if( diff  != 0 )
            throw new Exception("Cash breakdown must equal total cash");
        
        afterUpdate();
        return "_close";
    }
    
    def doCancel() {    
        return "_close";
    }
    
}       