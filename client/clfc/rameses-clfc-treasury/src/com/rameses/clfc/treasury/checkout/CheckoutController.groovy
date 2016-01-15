package com.rameses.clfc.treasury.checkout;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.*;

class CheckoutController extends CRUDController
{
    @Binding
    def binding;
    
    @Service('DateService')
    def dateSvc;
    
    String serviceName = 'CheckoutService';
    String entityName = 'checkout';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def selectedDepositSlip, rep1, rep2;
    def prevdepositslip;
    
    Map createEntity() {
        return [
            objid   : 'CO' + new UID(), 
            txndate : dateSvc.getServerDateAsString().split(' ')[0],
            txnstate: 'DRAFT'
        ];
    }
    
    void afterOpen( data ) {
        checkEditable(data);
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
            
            entity.representative2 = o;
        }
        return assigneeImpl(handler);
    }
    
    void clearRep2() {
        entity?.representative2 = null;
        binding?.refresh('rep2');
    }
    
    def assigneeImpl( handler ) {
        def op = Inv.lookupOpener('vaultrepresentative:lookup', [onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    void afterCreate( data ) {
        listHandler?.reload();
    }
    
    void afterEdit( data ) {
        prevdepositslip = [];
        def item;
        entity.depositslips?.each{ o->
            item = [:];
            item.putAll(o);
            prevdepositslip << item;
        }
        rep1 = entity?.representative1;
        rep2 = entity?.representative2;
    }
    
    void beforeCancel() {
        entity.representative1 = rep1;
        entity.representative2 = rep2;
        entity.depositslips = [];
        entity.depositslips.addAll(prevdepositslip);
        listHandler?.reload();
        binding?.refresh('rep1|rep2');
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.depositslips) entity.depositslips = [];
            return entity.depositslips;
        }
    ] as BasicListModel;
    
    def addDepositSlip() {
        def handler = { o->
            def item = entity.depositslips.find{ it.depositslip.controlno == o.controlno }
            if (item) throw new Exception("This deposit slip has already been selected.");
           
            item = [
                objid       : 'COD' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                txndate     : o.txndate,
                depositslip : [
                    controlno   : o.controlno,
                    acctno      : o.passbook.acctno,
                    acctname    : o.passbook.acctname,
                    amount      : o.amount
                ]
            ]
            
            if (!entity._addedds) entity._addedds = [];
            entity._addedds << item;
            
            entity.depositslips << (item);
            listHandler?.reload();
        }
        
        def params = [
            state   : 'CLOSED',
            reftype : 'SAFE_KEEP',
            onselect: handler
        ];
        def op = Inv.lookupOpener("depositslip:lookup", params);
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
        if (!MsgBox.confirm('You are about to confirm this check-out. Continue?')) return;
        
        entity = service.confirm(entity);
        checkEditable(entity);
    }
    
}