package com.rameses.clfc.treasury.encashment

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.LoanUtil;

class EncashmentChangeController 
{
    @Binding
    def binding;
    
    @ChangeLog
    def changeLog;
    
    String title = "Change";
    
    def entity, allowEdit = true, selectedItem;
    
    void init() {
        if (!entity.change) entity.change = [];
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.change) {
                entity.change = [];
                LoanUtil.denominations.each{
                    def map = [:];
                    map.putAll(it);
                    entity.change << map;
                }
            }
            return entity.change;
        },
        onColumnUpdate: { itm, colName->
            if (colName == 'qty') {
                if (!itm.qty) itm.qty = 0;
                
                itm.amount = itm.denomination * itm.qty;
                binding.refresh('totalchange');
            }
        }
    ] as EditorListModel;
        
    def getTotalchange() {
        if (!entity.change) return 0;
        def amt = entity.change.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }
}

