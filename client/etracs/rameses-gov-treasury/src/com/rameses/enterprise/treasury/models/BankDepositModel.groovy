package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class BankDepositModel extends CrudFormModel { 

    def depositid;
    def fundid;
    def handler;
    def checks = [];
    
    void afterOpen() {
        if(entity.totalcash == null) entity.totalcash = 0;
        if(entity.totalcheck == null) entity.totalcheck = 0;
        if( entity.cashbreakdown == null ) entity.cashbreakdown = [];
    }

    def checkModel = [
        fetchList: { o->
            return entity.checks;
        }
    ] as BasicListModel;
    
    def addCheck() {
        def h = { o-> 
            o.each {
                checks << o;
                entity.checks << o;
            }
            entity.totalcheck = entity.checks.sum{ it.amount };
            checkModel.reload();
        }
        return Inv.lookupOpener("cashreceipt_check_undeposited:lookup", [onselect:h]);
    }
    
    def getBankAccountLookup() {
        def h = { o->
            entity.bankaccount = o;
        }
        return Inv.lookupOpener("bankaccount:lookup", [onselect:h, fundid: fundid ]);
    }

    def doOk() {
        def m = [_schemaname: schemaName];
        m.objid = entity.objid;
        m.bankaccount = entity.bankaccount;
        m.amount = entity.amount;
        if(entity.cashbreakdown) {
            m.cashbreakdown = entity.cashbreakdown;
            m.totalcash = m.cashbreakdown.sum{it.amount};
        }
        m.checks = checks;
        persistenceService.update( m );
        handler( entity );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
} 