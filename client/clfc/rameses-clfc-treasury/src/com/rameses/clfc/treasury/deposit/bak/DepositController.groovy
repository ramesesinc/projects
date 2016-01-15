package com.rameses.clfc.treasury.deposit;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

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