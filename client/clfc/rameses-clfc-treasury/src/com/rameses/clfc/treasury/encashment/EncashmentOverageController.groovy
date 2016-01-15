package com.rameses.clfc.treasury.encashment

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.LoanUtil;

class EncashmentOverageController 
{
    @Binding
    def binding
    
    @ChangeLog
    def changeLog;
    
    String title = 'Overage';
    
    def entity, allowEdit = true, selectedItem; 
    
    void init() {
        if (!entity.overage) entity.overage = [];
    }
    
    def getDenominations() {
        def list = [];
        list.addAll(LoanUtil.denominations);
        list << [caption: 0.01, denomination: 0.01, qty: 0, amount: 0];
        
        list = list.sort{ it.caption }.reverse();
        
        return list;
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.overage) {
                entity.overage = [];
                getDenominations().each{
                    def map = [:];
                    map.putAll(it);
                    entity.overage << map;
                }
            }
            return entity.overage;
        },
        onColumnUpdate: { itm, colName->
            if (colName == 'qty') {
                if (!itm.qty) itm.qty = 0;
                
                itm.amount = itm.denomination * itm.qty;
                binding.refresh('total');
            }
        }
    ] as EditorListModel;
    
    def getTotal() {
        if (!entity.overage) return 0;
        def amt = entity?.overage?.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    void refresh() {
        binding?.refresh();
        listHandler?.reload();
    }
}