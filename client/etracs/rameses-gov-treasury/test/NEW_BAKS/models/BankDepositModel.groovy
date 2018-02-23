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
    
    boolean editable = true;
    def selectedCheck;
    
    void afterOpen() {
        if(entity.totalcash == null) entity.totalcash = 0;
        if(entity.totalcheck == null) entity.totalcheck = 0;
        if( entity.cashbreakdown == null ) entity.cashbreakdown = [];
        if(entity.checks == null ) entity.checks = [];
    }

    def checkModel = [
        fetchList: { o->
            return entity.checks;
        }
    ] as BasicListModel;
    
    def addCheck() {
        def h = { o-> 
            o.each {
               entity.checks << it;
            }
            entity.totalcheck = entity.checks.sum{ it.amount };
            checkModel.reload();
            binding.refresh("entity.totalcheck");
        }
        return Inv.lookupOpener("paymentcheck_undeposited:lookup", [onselect:h]);
    }
    
    void removeCheck() {
        if(!selectedCheck) throw new Exception("Select a check first");
        if(!entity.removedchecks) entity.removedchecks = [];
        entity.removedchecks << selectedCheck;
        entity.checks.remove(selectedCheck);
        entity.totalcheck = entity.checks.sum{ it.amount };
        checkModel.reload();
        binding.refresh("entity.totalcheck");
    }
    
    def getBankAccountLookup() {
        def h = { o->
            entity.bankaccount = o;
        }
        return Inv.lookupOpener("bankaccount:lookup", [onselect:h, fundid: fundid ]);
    }

    def doOk() {
        entity.schemaname = schemaName;
        if(entity.cashbreakdown) {
            entity.totalcash = entity.cashbreakdown.sum{it.amount};
        }
        if( entity.checks ) {
            entity.totalcheck = entity.totalcheck;
        } 
        if( entity.amount != entity.totalcash + entity.totalcheck) {
            throw new Exception("Amount to deposit must be equal to total cash and total check");
        }
        persistenceService.update( entity );
        handler( entity );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
    
    
} 