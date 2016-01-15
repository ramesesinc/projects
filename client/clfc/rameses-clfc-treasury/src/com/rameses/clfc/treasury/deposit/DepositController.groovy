package com.rameses.clfc.treasury.deposit;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DepositController extends CRUDController
{
    @Binding
    def binding;
    
    @Service('DateService')
    def dateSvc;
    
    @PropertyChangeListener
    def listener = [
        'entity.txntype': { o->
            entity?.representative1 = null;
            entity?.representative2 = null;
            binding?.refresh('rep1|rep2');
        }
    ]
    
    String serviceName = 'DepositService';
    String entityName = 'deposit';
    
    String title = 'Deposit';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def selectedDepositSlip, rep1, rep2;
    def depositToList = [
        [caption: 'To Vault', value: 'vault'],
        [caption: 'To Bank', value: 'bank']
    ]
    
    Map createEntity() {
        return [
            objid       : 'D' + new UID(),
            txnstate    : 'DRAFT',
            txntype     : 'vault',
            txndate     : dateSvc.getServerDateAsString().split(" ")[0]
        ];
    }
    
    void afterCreate( data ) {
        listHandler?.reload();
    }
    
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
    
    def getAssigneeLookup() {
        def handler = { o->
            if (entity?.representative2?.objid == o.objid) {
                throw new Exception('Representative #1 must not be the same as Representative #2.');
            }
        
            entity.representative1 = o;
        }
        return assigneeImpl(handler);
    }
    
    void clearRep1() {
        entity?.representative1 = null;
        binding?.refresh('rep1');
    }
    
    def getAssignee2Lookup() {
        def handler = { o->
            if (entity?.representative1?.objid == o.objid) {
                throw new Exception('Representative #2 must not be the same as Representative #1.');
            }
        }
        return assigneeImpl(handler);
    }
    
    void clearRep2() {
        entity?.representative2 = null;
        binding?.refresh('rep2');
    }
    
    def assigneeImpl( handler ) {        
        println 'txntype ' + entity.txntype;
        def op;
        if (entity.txntype == 'bank') {
            op = Inv.lookupOpener('teller:lookup', [onselect: handler]);
        } else if (entity.txntype == 'vault') {
            op = Inv.lookupOpener('vaultrepresentative:lookup', [onselect: handler])
        }
        
        if (!op) return null;
        return op;
    }
  
    def listHandler = [
        fetchList: { o->
            if (!entity.depositslips) entity.depositslips = [];
            return entity.depositslips;
        }
    ] as BasicListModel;
        
    def addDepositSlip() {
        def handler = { o->
            def i = entity.depositslips.find{ it.depositslip.controlno == o.controlno }
            if (i) throw new Exception("This deposit slip has already been selected.");

            def item = [
                objid       : 'DD' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                txndate     : o.txndate,
                depositslip : [
                    controlno   : o.controlno,
                    acctno      : o.passbook.acctno,
                    acctname    : o.passbook.acctname,
                    amount      : o.amount
                ]
            ];
            
            if (!entity._addedds) entity._addedds = [];
            entity._addedds << item;
            
            //entity.depositslips.add(item);
            entity.depositslips << item;
            listHandler?.reload();
        }
        def op = Inv.lookupOpener("depositslip:lookup", [state: 'APPROVED', onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    void removeDepositSlip() {
        if (!MsgBox.confirm('You are about to remove this deposit slip. Continue?')) return;
        
        if (!entity._removedds) entity._removedds = [];
        entity._removedds << selectedDepositSlip;
        
        if (entity._addedds) entity._addedds.remove(selectedDepositSlip);
        
        entity.depositslips.remove(selectedDepositSlip);
        listHandler?.reload();
    }
    
    void confirm() {
        if (!MsgBox.confirm('You are about to this deposit. Continue?')) return;
        
        entity = service.confirm(entity);
        checkEditable(entity);
    }
    
}

/*
class DepositController 
{
    @Service("DepositService")
    def svc;

    @Service("DateService")
    def dateSvc;

    String title = "Deposit";
    String entityName = "deposit";

    def page = "default";
    def entity;
    def selectedDepositSlip;
    def assigneeLookup;
    def assignee2Lookup;
    def mode = 'read';

    void init() {
        entity = [
            objid       : 'D' + new UID(),
            state       : 'DRAFT',
            txntype     : 'vault',
            txndate     : dateSvc.getServerDateAsString().split(" ")[0],
            depositslips: []
        ];
        mode = 'create';
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.depositslips) entity.depositslips = [];
            return entity.depositslips;
        }
    ] as BasicListModel;

    def next() {
        page = "main";
        
        if (entity.txntype == 'bank') {
            assigneeLookup = Inv.lookupOpener("teller:lookup", [:]);
        } else if (entity.txntype == 'vault') {
            assigneeLookup = Inv.lookupOpener("vaultrepresentative:lookup", [:]);

            def handler = { o->
                if (entity.representative1.objid == o.objid)
                    throw new Exception("This representative has already been selected.");

                entity.representative2 = o;
            }
            assignee2Lookup = Inv.lookupOpener("vaultrepresentative:lookup", [onselect: handler]);
        }   
        return page;
    }

    def open() {
        entity = svc.open(entity);
        page = 'main';
        mode = 'read';
        listHandler.reload();
        return page;
    }

    def back() {
        page = "default"
        entity.depositslips = [];
        entity.representative1 = null;
        entity.representative2 = null;
        return page;
    }

    def close() {
        return "_close";
    }

    def save() {
        if (MsgBox.confirm("Ensure that all information is correct. Continue?")) {
            svc.create(entity);
            MsgBox.alert("Transaction successful!");
            init();
            listHandler.reload();
            return back();
        }
    }

    def addDepositSlip() {
        def handler = { o->
            def i = entity.depositslips.find{ it.depositslip.controlno == o.controlno }
            if (i) throw new Exception("This deposit slip has already been selected.");

            def item = [
                objid       : 'DD' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                txndate     : o.txndate,
                depositslip : [
                    controlno   : o.controlno,
                    acctno      : o.passbook.acctno,
                    acctname    : o.passbook.acctname,
                    amount      : o.amount
                ]
            ];
            entity.depositslips.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener("depositslip:lookup", [state: 'APPROVED', onselect: handler]);
    }

    def removeDepositSlip() {
        if (MsgBox.confirm("You are about to remove this deposit slip. Continue?")) {
            entity.depositslips.remove(selectedDepositSlip);
            listHandler.reload();
        }
    }    
}
*/