package com.rameses.clfc.treasury.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.clfc.util.*;

class EncashmentBreakdownController
{
    @Binding
    def binding;
    
    @Service('EncashmentService')
    def service;

    @ChangeLog
    def changeLog;

    String title = 'Breakdown';
    
    def entity, allowEdit = true, selectedCbs;

    void init() {
        if (!entity.cbs) entity.cbs = [];
        if (!entity.breakdown) entity.breakdown = [];
        if (!entity.references) entity.references = [];
        /*
        if (!entity.cashbreakdown) {
            entity.cashbreakdown = [
                objid   : 'ECB' + new UID(),
                parentid: entity.objid
            ];
        }
        */
    }

    def addCbs() {
        def handler = { o->
            if (!entity.cbs) entity.cbs = [];
            def item = entity?.cbs?.find{ it.refid == o.objid }
            if (item) throw new Exception('CBS has already been selected.');
            
            item = [
                objid           : 'ECBS' + new UID(),
                parentid        : entity.objid,
                refid           : o.objid,
                cbsno           : o.cbsno,
                txndate         : o.txndate,
                collectiontype  : o.collection.type,
                amount          : o.amount,
                cbsid           : 'CB' + new UID(),
                reference       : service.getCbsDetails([objid: o.objid, encashmentid: entity?.objid])
            ];
            
            if (!entity._addedcbs) entity._addedcbs = [];
            entity._addedcbs.add(item);
            entity.cbs.add(item);
            
            if (!entity.references) entity.references = [];
            
            def m;
            item?.reference?.each{ i->
                m = entity.references.find{ it.denomination == i.denomination };
                if (!m) {
                    m = [
                        denomination: i.denomination,
                        qty         : i.qty
                    ];
                    m.amount = m.denomination * m.qty;
                    entity.references.add(m);
                } else {
                    m.qty += i.qty;
                    m.amount = m.denomination * m.qty;
                }
            }
            
            cbsListHandler?.reload();
            referenceListHandler?.reload();
            binding?.refresh('totalreference');
            
            /*
            if (!entity.cbs) entity.cbs = [];
            def item = entity?.cbs.find{ it.refid == o.objid }
            if (item) throw new Exception("CBS has already been selected");
            
            item = [
                objid           : 'ECBS' + new UID(),
                parentid        : entity.objid,
                refid           : o.objid,
                cbsno           : o.cbsno,
                txndate         : o.txndate,
                collectiontype  : o.collection.type,
                amount          : o.amount,
                cbsid           : 'CB' + new UID(),
                items           : service.getCbsDetails([objid: o.objid])
            ];
            
            if (!entity._addedcbs) entity._addedcbs = [];
            entity._addedcbs.add(item);
            entity.cbs.add(item);

            if (!entity.references) entity.references = [];
            def m;
            item.items.each{ i->
                m = entity.references.find{ it.denomination == i.denomination };
                if (!m) {
                    m = [
                        denomination: i.denomination,
                        qty         : i.qty
                    ];
                    m.amount = m.denomination * m.qty;
                    entity.references.add(m);
                } else {
                    m.qty += i.qty;
                    m.amount = m.denomination * m.qty;
                }
            }

            cbsListHandler.reload();
            referenceListHandler.reload();
            binding.refresh('totalreference');
            */
        }
        def params = [
            onselect    : handler,
            txndate     : entity?.txndate,
            encashmentid: entity?.objid
        ]
        def op = Inv.lookupOpener('encashment:cbs:lookup', params);
        if (!op) return null;
        return op;
    }

    /*
    void removeCbs() {
        if (MsgBox.confirm("You are about to remove the selected CBS. Continue?")) {
            def item;
            selectedCbs.items.each{ o->
                item = entity.references.find{ it.denomination == o.denomination }
                if (item) {
                    item.qty -= o.qty;
                    item.amount = item.denomination * item.qty;
                }
            }
            
            if (!entity._removedcbs) entity._removedcbs = [];
            entity._removedcbs.add(selectedCbs);
            
            if (entity._addedcbs) entity._addedcbs.remove(selectedCbs);
            
            entity.cbs.remove(selectedCbs);
            cbsListHandler.reload();
            referenceListHandler.reload();
            binding.refresh('totalreference');
        }
    }
    */
   
    void removeCbs() {
        if (!MsgBox.confirm('You are about to remove this CBS. Continue?')) return;

        def item;
        selectedCbs?.reference?.each{ o->
            item = entity.references?.find{ it.denomination == o.denomination }
            if (item) {
                item.qty -= o.qty;
                item.amount = item.denomination * item.qty;
            }
        }
        
        if (!entity._removedcbs) entity._removedcbs = [];
        entity._removedcbs.add(selectedCbs);
        
        if (entity._addedcbs) entity._addedcbs.remove(selectedCbs);
        entity.cbs.remove(selectedCbs);
        
        
        cbsListHandler?.reload();
        referenceListHandler?.reload();
        binding?.refresh('totalreference');
    }

    public void refresh() {
        binding?.refresh();
        cbsListHandler?.reload();
        referenceListHandler?.reload();
        encashmentListHandler?.reload();
    }
    
    def openCbs() {
        return openCbsImpl(selectedCbs);
    }
    
    def openCbsImpl( cbs ) {
        if (!cbs) return null;
        
        def op = Inv.lookupOpener('encashment:cbs:open', [entity: cbs]);
        if (!op) return null;
        
        return op;
    }

    def cbsListHandler = [
        fetchList: { o->
            if (!entity.cbs) entity.cbs = [];
            return entity.cbs;
        },
        onOpenItem: { itm, colName->
            return openCbsImpl(itm);
        }
    ] as BasicListModel;

    def referenceListHandler = [
        fetchList: { o->
            if (!entity.references) {
                entity.references = getDenominations();
            }
            return entity.references;
        }
    ] as BasicListModel;

    def encashmentListHandler = [
        fetchList: { o->
            /*
            if (!entity.encashments) {
                entity.encashments = [];
                LoanUtil.denominations.each{
                    def map = [:];
                    map.putAll(it);
                    entity.encashments << map;
                }
            }
            return entity.encashments;
            */
           if (!entity.breakdown) {
                entity.breakdown = getDenominations();
           }
           return entity.breakdown;
        },
        onColumnUpdate: { itm, colName->
            if (colName == 'qty') {
                if (!itm.qty) itm.qty = 0;
                def ref = entity.references.find{ it.denomination == itm.denomination }
                
                if (itm.qty > ref.qty) 
                    throw new Exception("Qty inputted exceeds reference qty.");
                    
                itm.amount = itm.denomination * itm.qty;
                binding.refresh('totalencashment');
            }
        }
    ] as EditorListModel;

    def getTotalencashment() {
        if (!entity.breakdown) return 0;
        def amt = entity.breakdown?.amount?.sum();
        //if (!entity.encashments) return 0;
        //def amt = entity.encashments.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }

    def getTotalreference() {
        if (!entity.references) return 0;
        def amt = entity.references.amount.sum();
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
