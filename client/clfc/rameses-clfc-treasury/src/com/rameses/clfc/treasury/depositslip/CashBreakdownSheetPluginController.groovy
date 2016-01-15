package com.rameses.clfc.treasury.depositslip;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;


class CashBreakdownSheetPluginController
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def entity, selectedCbs, mode = 'read';
    
    def listHandler = [
        fetchList: { o->
            if (!entity.cbs) entity.cbs = [];
            return entity.cbs
        }
    ] as BasicListModel;
    
    def getTotalamount() {
        if (!entity?.cbs) return 0;
        def items = entity.cbs.findAll{ it.isencashed == 0 } 
        return (items? items.amount.sum() : 0);
    }
    
    def addCbs() {
        def handler = { o->
            def i = entity.cbs.find{ it.cbsid == o.objid }
            if (i) throw new Exception("This CBS has already been selected.");
            
            def item = [
                objid       : o.objid,//'DSCBS' + new UID(),
                parentid    : entity.objid,
                cbsid       : o.objid,
                cbsno       : o.cbsno,
                txndate     : o.txndate,
                amount      : o.amount,
                isencashed  : o.isencashed,
                collector   : o.collector
            ];
            
            if (!entity._addedcbs) entity._addedcbs = [];
            entity._addedcbs.add(item);
            
            entity.cbs.add(item);
            listHandler?.reload();

            if (o.isencashed != null && o.isencashed == 0) {
                o.items.each{ c->
                    i = entity.items.find{ it.denomination == c.denomination };

                    if (!i) {
                        entity.items.add(c);
                    } else {
                        i.qty += c.qty;
                        i.amount += c.amount;
                    }
                }

                entity.amount += o.amount;
                binding.refresh('totalamount');
                caller?.refresh('entity.amount|cashbreakdown');
            }
        }
        def op = Inv.lookupOpener('depositslip:cbs:lookup', [onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    void removeCbs() {
        if (!MsgBox.confirm('You are about to remove this CBS. Continue?')) return;

        if (selectedCbs.isencashed!=null && selectedCbs.isencashed==0) {
            entity.amount -= selectedCbs.amount;

            def m;
            selectedCbs.items.each{ o->
                m = entity.items.find{ it.denomination == o.denomination }
                if (m) {
                    m.qty -= o.qty;
                    m.amount -= o.amount;
                }
            }
        }

        if (!entity._removedcbs) entity._removedcbs = [];
        entity._removedcbs.add(selectedCbs); 

        if (entity._addedcbs) entity._addedcbs.remove(selectedCbs);
        entity.cbs.remove(selectedCbs);
        listHandler?.reload();
        
        binding.refresh('totalamount');
        caller?.refresh('entity.amount|cashbreakdown');
    }
}
/*
class CashBreakdownSheetPluginController
{
    @Caller
    def caller;

    @Binding
    def binding;

    def entity, selectedCbs, mode;
    
    def cbsListHandler = [
        fetchList: { o->
            if (!entity.cbs) entity.cbs = [];
            return entity.cbs;
        },
        getOpenerParams: { o->
            o.entityid = o.objid;
            o.objid = o.cbsid;
            return o;
        }
    ] as BasicListModel;

    def addCbs() {
        def handler = { o->
            def i = entity.cbs.find{ it.cbsid == o.objid }
            if (i) throw new Exception("This CBS has already been selected.");
            
            def item = [
                objid       : 'DSCBS' + new UID(),
                parentid    : entity.objid,
                cbsid       : o.objid,
                cbsno       : o.cbsno,
                txndate     : o.txndate,
                amount      : o.amount,
                isencashed  : o.isencashed,
                collector   : o.collector
            ];
            entity.cbs.add(item);
            if (!entity._cbsadded) entity._cbsadded = [];
            entity._cbsadded.add(item);
            cbsListHandler.reload();

            if (o.isencashed != null && o.isencashed == 0) {
                o.items.each{ c->
                    i = entity.items.find{ it.denomination == c.denomination };

                    if (!i) {
                        entity.items.add(c);
                    } else {
                        i.qty += c.qty;
                        i.amount += c.amount;
                    }
                }

                entity.amount += o.amount;
                binding.refresh('totalamount');
                caller?.refresh('entity.amount|cashbreakdown');
            }
        }
        return Inv.lookupOpener('depositslip:cbs:lookup', [onselect: handler]);
    }

    void removeCbs() {
        if (MsgBox.confirm("You are about to remove this CBS. Continue?")) {
            if (selectedCbs.isencashed!=null && selectedCbs.isencashed==0) {
                entity.amount -= selectedCbs.amount;
            
                def m;
                selectedCbs.items.each{ o->
                    m = entity.items.find{ it.denomination == o.denomination }
                    if (m) {
                        m.qty -= o.qty;
                        m.amount -= o.amount;
                    }
                }
            }

            if (!entity._cbsdeleted) entity._cbsdeleted = [];
            entity._cbsdeleted.add(selectedCbs);
            if (entity._cbsadded) entity._cbsadded.remove(selectedCbs);
            entity.cbs.remove(selectedCbs);
            cbsListHandler.reload();
            binding.refresh('totalamount');
            caller?.refresh('entity.amount|cashbreakdown');
        }
    }

    def getTotalamount() {
        if (!entity?.cbs) return 0;
        def items = entity.cbs.findAll{ it.isencashed == 0 } 
        return (items? items.amount.sum() : 0);
    }
}
*/