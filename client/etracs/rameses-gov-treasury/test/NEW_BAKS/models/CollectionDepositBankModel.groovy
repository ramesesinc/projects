package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 


class CollectionDepositBankModel extends CrudFormModel { 

    def getBankaccountLookup() {
        def h = { o->
            entity.bankaccount = o;
            binding.refresh();
        }
        return Inv.lookupOpener("bankaccount:lookup", [fundid: entity.fund.objid] );
    }

    void openEdit() {
        super.open();
        if(!entity.cashbreakdown) entity.cashbreakdown = [];
        //return edit();
    }
    
    
    def checkModel = [
        fetchList: { o->
            return entity.checks;
        }
    ] as BasicListModel;

    
    /*    
    def addDepositSlip() {
        def dep = [:];
        dep.refid = entity.objid;
        dep.totalnoncash = 100;
        dep.totalcash = 200;
        dep.cashbreakdown = [];
        dep.checks = [];
        dep.fund = entity.fund;
        return Inv.lookupOpener( "bankdepositslip:create", [params:dep ] );
    }
    */
    
    /*
    def listModel = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    */
} 