package com.rameses.clfc.treasury.checkout;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.*;

class XCheckoutController 
{
    @Binding
    def binding;

    @Service("CheckoutService")
    def service;
    
    @Service("DateService")
    def dateSvc;
    
    String title = "Check-out";
    String entityName = "checkout";
    
    def entity, mode = 'read';
    def selectedDepositSlip;
    def assigneeLookup = Inv.lookupOpener("vaultrepresentative:lookup" , [:]);
    def assignee2Lookup = Inv.lookupOpener("vaultrepresentative:lookup", [
        onselect: { o->
            if (entity.representative1.objid == o.objid) 
                throw new Exception("This representative has already been selected.");

            entity.representative2 = o;
        }   
    ]);

    void init() {
        entity = [
            objid   : 'CO' + new UID(),
            txndate : dateSvc.getServerDateAsString(),
            depositslips: []
        ];
        mode = 'create';
    }

    def listHandler = [
        fetchList: { o->
            if (!entity?.depositslips) entity.depositslips = [];
            return entity.depositslips;
        }
    ] as BasicListModel;

    def checkout() {
        if (MsgBox.confirm("You are about to check out this deposit slips. Continue?")) {
            service.create(entity);
            MsgBox.alert("Successfully check-out deposit slips!")
            init();
            listHandler.reload();
        }
    }
    
    void open() {
        entity = service.open(entity);
        mode = 'read';
    }

    def close() {
        return "_close";
    }

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
            
            entity.depositslips.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener("depositslip:lookup", [state: 'CLOSED', reftype: 'SAFE_KEEP', onselect: handler]);
    }

    def removeDepositSlip() {
        if (MsgBox.confirm("You are about to remove this deposit slip. Continue?")) {
            entity.depositslips.remove(selectedDepositSlip);
            listHandler.reload();
        }
    }
}