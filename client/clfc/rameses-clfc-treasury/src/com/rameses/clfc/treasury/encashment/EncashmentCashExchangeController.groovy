package com.rameses.clfc.treasury.encashment

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class EncashmentCashExchangeController 
{
    @Binding
    def binding;
    
    @ChangeLog
    def changeLog;
        
    String title = 'Cash Exchange';
    
    def entity, allowEdit = true, selectedCbs;
    def list = [];
    
    void init() {
        list = getDenominations();
        refresh();
    }
    
    void refresh() {
        binding?.refresh();
        listHandler?.reload();
        cashinListHandler?.reload();
        cashoutListHandler?.reload();
        breakdownListHandler?.reload();
    }
    
    void setSelectedCbs( selectedCbs ) {
        this.selectedCbs = selectedCbs;
        cashinListHandler?.reload();
        cashoutListHandler?.reload();
        breakdownListHandler?.reload();
        binding?.refresh('totalbreakdown|totalcashin|totalcashout');
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.cbs) entity.cbs = [];
            return entity.cbs;
        }
    ] as BasicListModel;
    
    def breakdownListHandler = [
        fetchList: { o->
            if (selectedCbs) {
                if (!selectedCbs.reference) {
                    selectedCbs.reference = getDenominations();
                }
                return selectedCbs.reference;
            }
            return list;
        }
    ] as BasicListModel;
    
    def cashoutListHandler = [
        fetchList: { o->
            if (selectedCbs) {
                if (!selectedCbs.cashout) {
                    selectedCbs.cashout = getDenominations();
                }
                return selectedCbs.cashout;
            }
            return list;
        },
        beforeColumnUpdate: { itm, colName, newVal->
            def i = selectedCbs?.reference?.find{ it.denomination == itm.denomination }
            if (i) {
                i.qty += itm.qty;
                i.amount = i.qty * i.denomination;
            }
            return true;
        },
        onColumnUpdate: { itm, colName->
            if (colName=='qty') {
                if (!itm.qty) itm.qty = 0;
                def i = selectedCbs?.reference?.find{ it.denomination == itm.denomination }
                
                if (itm.qty > i.qty) {
                    throw new Exception('Qty inputted exceeds qty available.');
                }
                
                itm.amount = itm.denomination * itm.qty;
                
                if (i) {
                    i.qty -= itm.qty;
                    i.amount = i.qty * i.denomination;
                }
                binding?.refresh('totalcashout|totalbreakdown');
                rebuildReference();
            }
        }
    ] as EditorListModel
    
    def cashinListHandler = [
        fetchList: { o->
            if (selectedCbs) {
                if (!selectedCbs.cashin) {
                    selectedCbs.cashin = getDenominations();
                }
                return selectedCbs.cashin;
            }
            return list;
        },
        beforeColumnUpdate: { itm, colName, newVal->
            def i = selectedCbs?.reference?.find{ it.denomination == itm.denomination }
            if (i) {
                i.qty -= itm.qty;
                i.amount = i.qty * i.denomination;
            }
            return true;
        },
        onColumnUpdate: { itm, colName->
            if (colName=='qty') {
                if (!itm.qty) itm.qty = 0;

                itm.amount = itm.denomination * itm.qty;
                
                def i = selectedCbs?.reference?.find{ it.denomination == itm.denomination }
                if (i) {
                    i.qty += itm.qty;
                    i.amount = i.qty * i.denomination;
                }
                binding?.refresh('totalcashin|totalbreakdown');
                
                if (getTotalcashin() > getTotalcashout()) {
                    throw new Exception('Total cash in exceeds total cash out.');
                }
                rebuildReference();
            }
        }
    ] as EditorListModel;
    
    void rebuildReference() {
        entity.references = getDenominations();
        entity?.cbs?.each{ o->
            o.reference?.each{ b->
                def i = entity?.references?.find{ it.denomination == b.denomination }
                if (i) {
                    i.qty += b.qty;
                    i.amount = i.qty * i.denomination;
                }
            }
        }
    }
    
    def getTotalcashout() {
        if (!selectedCbs) return 0;
        def amt = selectedCbs?.cashout?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def getTotalcashin() {
        if (!selectedCbs) return 0;
        def amt = selectedCbs?.cashin?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def getTotalbreakdown() {
        if (!selectedCbs) return 0;
        def amt = selectedCbs?.reference?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def getDenominations() {
        def list = [];
        LoanUtil.denominations.each{
            def map = [:];
            map.putAll(it);
            list << map;
        }
        return list;
    }
}

