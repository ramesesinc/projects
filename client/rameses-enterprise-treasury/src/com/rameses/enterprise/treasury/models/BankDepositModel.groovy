package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
        
class BankDepositSlipModel extends CrudFormModel {
    
    def params;
    
    def getBankaccountLookup() {
        def h = { o->
            entity.bankaccount = o;
            binding.refresh();
        }
        return Inv.lookupOpener("bankaccount:lookup", [fundid: entity.fund.objid] );
    }

    void afterCreate() {
        entity.putAll( params );
        if( entity.totalcash == null || entity.cashbreakdown == null )
           throw new Exception("Total cash is null. Please run migration for fund");
    }

    def checkModel = [
        fetchList: { o->
            return entity.checks;
        }
    ] as BasicListModel;

    
}
        