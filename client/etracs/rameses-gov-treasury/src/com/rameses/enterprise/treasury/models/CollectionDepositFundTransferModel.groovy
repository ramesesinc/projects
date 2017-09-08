package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class CollectionDepositFundTransferModel extends CrudFormModel { 

    def getBankaccountLookup() {
        def h = { o->
            entity.tobankaccount = o;
            binding.refresh();
        }
        return Inv.lookupOpener("bankaccount:lookup", [fundid: entity.fund.objid] );
    }
    
    /*    
    def listModel = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    */
}  