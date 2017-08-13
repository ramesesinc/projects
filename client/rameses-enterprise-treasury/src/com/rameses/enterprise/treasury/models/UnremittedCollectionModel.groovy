package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class UnremittedCollectionModel {
    
    @Binding
    def binding

    @Service("UnremittedCollectionService")
    def svc

    def df = new java.text.DecimalFormat("#,##0.00")

    def totalamount = "0.00"
    def params=[:];
    def list
    def selectedItem

    void init() {
        search();
    }

    def listHandler = [
        fetchList: { o ->
            return list
        }
    ] as BasicListModel

    def search() {
        list = svc.getList(params)
        totalamount = list ? df.format( list.amount.sum() ): "0.00"
    }

    def open() {
        if(!selectedItem) return;

        def o = InvokerUtil.lookupOpener( "cashreceiptinfo:open",[entity:selectedItem]);
        o.target =  "popup"
        return o;
    }

    def refresh() {
        search();
        listHandler.load()
        binding.refresh("totalamount")
    }

    void fix() {
        if ( MsgBox.confirm('You are about to fix your accountable forms.\nDo you want to continue?') ) {
            svc.fixInventory([:]);  
            MsgBox.alert('Successfully processed.'); 
        } 
    } 

    def remit() {
        return Inv.lookupOpener( "remittance:create" );
    }
}
 