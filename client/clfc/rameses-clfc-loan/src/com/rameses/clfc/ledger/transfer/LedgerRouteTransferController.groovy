package com.rameses.clfc.ledger.transfer;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LedgerRouteTransferController
{
    @Service("LoanLedgerService")
    def service;

    def sourceRouteLookupHandler = Inv.lookupOpener('route:lookup', [
        onselect: { r->
            list = service.getLedgers([routecode: r.code]);
            src = r;
        }
    ]);

    def destRouteLookupHandler = Inv.lookupOpener('route:lookup', [
        onselect: { r->
            if (r.code == src.code)
                throw new Exception("Destination route must not be the same with source route.");

            destination = r;
        }
    ]);
    def src;
    def destination;
    def list;   

    void init() {
        src = [:];
        destination = [:];
        list = [];
    }

    def listHandler = [
        fetchList: { o->
            if (!list) list = [];
            return list;
        }
    ] as EditorListModel;

    def close() {
        return "_close";
    }

    void selectAll() {
        list.each{ it.selected = true }
        listHandler.reload();
    }

    void deselectAll() {
        list.each{ it.selected = false } 
        listHandler.reload();
    }

    void transfer() {  
        String msg = "You are about to transfer selected ledgers from ${src.description} to ${destination.description}. Continue?";
        if (MsgBox.confirm(msg)) {
            service.routeTransfer([list: list, route: destination]);
            MsgBox.alert("Successfully transfered selected ledgers from ${src.description} to ${destination.description}.");
            init();
        }   
    }
}