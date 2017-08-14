package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 


class CollectionDepositBankModel extends CrudFormModel { 

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
    
    def listModel = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
} 