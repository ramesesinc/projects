package com.rameses.clfc.treasury.cashexchange

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.clfc.util.*;

class CashExchangeController extends CRUDController 
{
    @Binding
    def binding;
    
    String serviceName = 'CashExchangeService';
    String entityName = 'cashexchange';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createEntity() {
        return [
            objid   : 'CE' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
    
    void afterCreate( data ) {
        cashinListHandler?.reload();
        cashoutListHandler?.reload();
        breakdownListHandler?.reload();
    }
    
    def cbsLookup = Inv.lookupOpener('cashexchange:cbs:lookup', [
         onselect: { o->
             entity.cbs = o;
             binding?.refresh('entity.cbs.*');
             
             def breakdown = service.getBreakdown([cbsid: o.objid]);
             if (breakdown) {
                 entity.breakdown = breakdown;
                 breakdownListHandler?.reload();
                 binding?.refresh('totalbreakdown');
             }
         }
    ]);

    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    def breakdownListHandler = [
        fetchList: { o->
            if (!entity.breakdown) entity.breakdown = getDenominations();
            return entity.breakdown;
        }
    ] as BasicListModel;
    
    def getTotalbreakdown() {
        if (!entity.breakdown) return 0;
        
        def amt = entity?.breakdown?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def cashoutListHandler = [
        fetchList: { o->
            if (!entity.cashout) entity.cashout = getDenominations();
            return entity.cashout;
        },
        beforeColumnUpdate: { itm, colName, newVal->
            def i = entity?.breakdown?.find{ it.denomination == itm.denomination }
            if (i) {
                i.qty += itm.qty;
                i.amount = i.qty * i.denomination;
            }
            return true;
        },
        onColumnUpdate: { itm, colName->
            if (colName=='qty') {
                if (!itm.qty) itm.qty = 0;
                def i = entity?.breakdown?.find{ it.denomination == itm.denomination }
                
                if (itm.qty > i.qty) {
                    throw new Exception('Qty inputted exceeds qty available.');
                }
                
                itm.amount = itm.denomination * itm.qty;
                
                if (i) {
                    i.qty -= itm.qty;
                    i.amount = i.qty * i.denomination;
                }
                
                binding?.refresh('totalcashout|totalbreakdown');
            }
        }
    ] as EditorListModel;
    
    def getTotalcashout() {
        if (!entity.cashout) return 0;
        
        def amt = entity?.cashout?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def cashinListHandler = [
        fetchList: { o->
            if (!entity.cashin) entity.cashin = getDenominations();
            return entity.cashin;
        },
        beforeColumnUpdate: { itm, colName, newVal->
            def i = entity?.breakdown?.find{ it.denomination == itm.denomination }
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
                
                def i = entity?.breakdown?.find{ it.denomination == itm.denomination }
                if (i) {
                    i.qty += itm.qty;
                    i.amount = i.qty * i.denomination;
                }
                
                binding?.refresh('totalcashin|totalbreakdown');
            }
        }
    ] as EditorListModel;
    
    def getTotalcashin() {
        if (!entity.cashin) return 0;
        
        def amt = entity?.cashin?.amount?.sum();
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
    
    void submitForApproval() {
        if (!MsgBox.confirm('You are about to submit this document for approval. Continue?')) return;
        
        entity = service.submitForApproval(entity);
        checkEditable(entity);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm('You are about to approve this document. Continue?')) return;
        
        entity = service.approveDocument(entity);
        checkEditable(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm('You are about to disapprove this document. Continue?')) return;
        
        entity = service.disapprove(entity);
        checkEditable(entity);
    }
}

