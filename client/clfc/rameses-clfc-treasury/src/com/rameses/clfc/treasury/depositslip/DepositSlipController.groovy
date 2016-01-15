package com.rameses.clfc.treasury.depositslip;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DepositSlipController extends CRUDController
{
    @Caller
    def caller;

    @Binding
    def binding;

    /*
    @PropertyChangeListener
    def listener = [
        "entity.type": { o->
            if (o == 'cash') {
                entity.checks = [];
                //listHandler.reload();
                //binding.refresh('totalcheck');
            } else if (o == 'check') {
                entity.cbs = [];
                entity.items = [];
                //cbsListHandler.reload();
                //binding.refresh('totalamount');
            }
            entity.amount = 0;
            binding.refresh('entity.amount');
        }
    ];
    */

    @Service("DateService")
    def dateSvc;

    String serviceName = "DepositSlipService";
    String entityName = "depositslip";

    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    Map createParams = [domain: 'TREASURY', role: 'ACCT_ASSISTANT'];
    Map editParams = [domain: 'TREASURY', role: 'ACCT_ASSISTANT'];

    def typeList = ["cash", "check"];
    def passbook, currencytype, accounttype, deposittype, items = [];
    def passbookLookup = Inv.lookupOpener("passbook:lookup", [
        onselect: { o->
            entity.passbook = o;
            binding.refresh("passbook");
        },
        state   : 'ACTIVE'
    ]);    
    def currencyTypeLookup = Inv.lookupOpener("currencytype:lookup", [
        onselect: { o->
            entity.currencytype = o;
            binding.refresh('currencytype');
        },
        state   : 'ACTIVATED'
    ]);
    def accountTypeLookup = Inv.lookupOpener("accounttype:lookup", [
        onselect: { o->
            entity.accounttype = o;
            binding.refresh('accounttype');
        },
        state   : 'ACTIVATED'
    ]);
    def depositTypeLookup = Inv.lookupOpener("deposittype:lookup", [
        onselect: { o->
            entity.deposittype = o;
            binding.refresh('deposittype');
        },
        state   : 'ACTIVATED'
    ]);

    def totalbreakdown;

    Map createEntity() {
        totalbreakdown = 0;
        return [
            objid   : 'DS' + new UID(), 
            state   : 'DRAFT',
            checks  : [],
            cbs     : [],
            txndate : dateSvc.getServerDateAsString(),
            amount  : 0
            //cashbreakdown   : createCashBreakdown()
        ];
    }

    private def createCashBreakdown() {
        return [
            objid   : 'CB' + new UID(),
            items   : []
        ];
    }

    void afterOpen( data ) {
        /*if (!data.cashbreakdown) {
            data.cashbreakdown = [objid: 'CB' + new UID(), items: []];
        }
        if (data.cashbreakdown?.items) {
            totalbreakdown = data.cashbreakdown.items.amount.sum();
        }
        if (!totalbreakdown) totalbreakdown = 0;*/

        checkEditable(data);
        //if (data.state != 'DRAFT') allowEdit = false;
    }
    
    void afterSave( data ) {
        data._addedcbs = [];
        data._removedcbs = [];
        data._addedcheck = [];
        data._removedcheck = [];
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.state == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    def prevcbs, prevcheck;
    void afterEdit( data ) {
        def item;
        prevcbs = [];
        prevcheck = [];
        if (data.cbs) {
            data.cbs.each{ o->
                item = [:];
                item.putAll(o);
                prevcbs.add(item);
            }
        }
        
        if (data.checks) {
            data.checks.each{ o->
                item = [:];
                item.putAll(o);
                prevcheck.add(item);
            }
        }
    }
    
    void afterCancel() {
        entity.cbs = [];
        if (prevcbs) {
            entity.cbs.addAll(prevcbs);
        }
        
        entity.checks = [];
        if (prevcheck) {
            entity.checks.addAll(prevcheck);
        }
        binding?.refresh();
    }

    def getPluginList() {
        getOpeners: {
            return Inv.lookupOpeners("depositslip-plugin", [entity: entity, mode: mode]);
        }
    }

    def getCashbreakdown() {
        if (!entity.items) entity.items = [];
        totalbreakdown = 0;
        if (entity.items) {
            totalbreakdown = entity.items.amount.sum();
        }
        def params = [
            entries         : entity.items,//entity.cashbreakdown.items,
            totalbreakdown  : totalbreakdown,
            editable        : false,//((mode != 'read' && entity.type == 'cash')? true : false),
        ];
        return Inv.lookupOpener('clfc:denomination', params);
    }
    
    /*
    void beforeSave( data ) {
        if (data.type == 'check') {
            def total = data.checks.amount.sum();
            if (data.amount != total)
                throw new Exception("Total for checks does not match amount.");
        } else if (data.type == 'cash') {
            if (data.amount != totalbreakdown)
                throw new Exception("Total denomination does not match amount.");
        }
    }
    */

    boolean getAllowReinstate() {
        def date = dateSvc.getServerDateAsString().split(" ")[0];
        if (entity.state == 'CLOSED' && entity.txndate == date) return true;
        return false;
    }

    void refresh() {
        binding.refresh();
    }

    void refresh( text ) {
        binding.refresh(text);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm('You are about to approve this document. Continue?')) return;
        
        entity = service.approveDocument(entity);
        checkEditable(entity);
    }

    /*
    void safekeep() {
        if (MsgBox.confirm("You are about to safe keep this record. Continue?")) {
            entity = service.safekeep(entity);
            afterOpen(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller.reload(); }); 
        }
    }
    
    void reinstate() {
        if (MsgBox.confirm("You are about to re-instate this record. Continue?")) {
            entity = service.reinstate(entity);
            afterOpen(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller.reload(); }); 
        }
    }

    void checkout() {
        if (MsgBox.confirm("You are about to check out this record. Continue?")) {
            entity = service.checkout(entity);
            afterOpen(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller.reload(); }); 
        }
    }

    def deposit() {
        def handler = { o->
            entity = service.deposit([objid: o.objid, teller: o.teller]);
            afterOpen(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller.reload(); }); 
        }
        return Inv.lookupOpener('deposit:create', [entity: entity, handler: handler]);
    }
    */
}