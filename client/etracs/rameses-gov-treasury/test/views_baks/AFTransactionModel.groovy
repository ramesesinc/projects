package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFTransactionModel extends CrudFormModel {

    def txnTypes = ["TRANSFER","CANCEL","RETURN","REVERT_SALE","REVERT_CANCEL"];
    
    void afterCreate() {
        entity.items = [];
    }
    
    def itemHandler = [
        fetchList: {
            return entity.items;
        },
        onColumnUpdate: { o,colName ->
            if(colName == "af" ) {
                o.putAll( o.af );
            }
        },
        onAddItem: { o->
            entity.items << o; 
        }
    ] as EditorListModel;

    def getLookupItems() {
        if(!entity.txntype) {
            MsgBox.err(new Exception("Please select a transaction type"));
            return null;
        }
            
        String str = "af_control:lookup";
        if( entity.txntype?.matches('TRANSFER|RETURN')) {
            str = "af_control_issued:lookup";
        }
        return Inv.lookupOpener( str );
    }
    
    
}    