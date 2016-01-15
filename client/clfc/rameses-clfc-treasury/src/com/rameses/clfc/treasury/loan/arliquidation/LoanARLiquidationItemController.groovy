package com.rameses.clfc.treasury.loan.arliquidation

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanARLiquidationItemController 
{   
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def data, list, handler, selectedBreakdown;
    def mode = 'read', addedbreakdown, removedbreakdown;
    
    String title = "AR Liquidation Item";
    
    void init() {
        //if (!data.items) data.items = [];
        list = [];
        if (data.items) {
            def item;
            data.items.each{ o->
                item = [:];
                item.putAll(o);
                list.add(item);
            }
        }//list.addAll(data.items);
        addedbreakdown = [];
        removedbreakdown = [];
    }
    
    def breakdownHandler = [
        fetchList: { o->
            if (!list) list = [];
            return list;
        },
        onOpenItem: { itm, colName->
            def handler = { o->
                selectedBreakdown.putAll(o);
                selectedBreakdown._edited = true;
                validateBreakdown();
                breakdownHandler?.reload();
                binding?.refresh('total');
            }
            return Inv.lookupOpener('arliquidationbreakdown:open', [data: itm, handler: handler, mode: mode]);
        }
    ] as BasicListModel;
    
    def close() {
        return '_close';
    }
    
    void validateBreakdown( amt = null ) {
        def t = getTotal() + (amt? amt : 0);
        if (t > data.amount)
            throw new Exception("Total for breakdown must not be greater than amount allocated.");
    }
    
    def getTotal() {
        if (!list) return 0;
        return list.amount.sum();
    }
    
    def addBreakdown() {
        def handler =  { o->
            validateBreakdown(o.amount);
            
            if (!o.arid) o.arid = data.parentid;
            if (!o.parentid) o.parentid = data.objid;
            if (!o.liquidationid) o.liquidationid = data.liquidationid;
            
            if (!addedbreakdown) addedbreakdown = [];
            addedbreakdown.add(o);
            
            list.add(o);
            breakdownHandler?.reload();
            binding?.refresh('total');
        }
        return Inv.lookupOpener('arliquidationbreakdown:create', [handler: handler, mode: mode]);
    }
    
    void removeBreakdown() {
        if (!MsgBox.confirm("You are about to remove this item. Continue?")) return;
        
        if (!removedbreakdown) removedbreakdown = [];
        removedbreakdown.add(selectedBreakdown);
        
        if (addedbreakdown) addedbreakdown.remove(selectedBreakdown);
        
        list.remove(selectedBreakdown);
        breakdownHandler?.reload();
        binding?.refresh('total');
    }
    
    def save() {
        if (handler) {
            def params = [
                list    : list,
                added   : addedbreakdown,
                removed : removedbreakdown
            ]
            handler(params);
        }
        return '_close';
    }    
    
    /*
    @Service("LoanARLiquidationService")
    def service;
    
    def entity, handler, mode = 'read';
    def typeList = [], loanapp;
    
    def loanappLookup = Inv.lookupOpener('loanapp:lookup', [
         onselect: { o->
             if (o.state=='CLOSED') 
                throw new Exception("Loan ${o.appno} for ${o.borrower.name} is already closed.");
             
             entity.loanapp = [objid: o.objid, appno: o.appno];
             entity.borrower = o.borrower;
         }
    ]);
    
    void init() {
        entity = [objid: 'LALD' + new UID()];
        typeList = service.getTypes();
    }
    
    void open() {
        typeList = service.getTypes();
    }
    */
    
    /*
    def doOk() {
        if (handler) handler(list);
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    */
}

