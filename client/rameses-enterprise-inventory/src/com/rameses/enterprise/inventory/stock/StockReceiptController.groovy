package com.rameses.enterprise.inventory.stock;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;

public abstract class StockReceiptController {

    @Binding
    def binding

    @Service("StockReceiptService")
    def service;

    @Service("StockRequestService")
    def stockReqSvc;

    def entity;
    def request;
    def selectedItem;
    def mode = "initial";

    def create() {
        entity = [items:[]];
        entity.objid = "STKRCT"+new UID();
        return "initial";
    }
    
    def open() {
        mode = 'read';
        entity = service.open( entity? entity: [:] ); 
        selectedItem = (entity.items? entity.items[0]: null);  
        getDetailHandler()
        return 'default'; 
    }

    def process() {
        def r = stockReqSvc.open([objid: request.objid]);
        entity.request = request;
        entity.reqtype = r.reqtype;
        entity.itemclass = r.itemclass;
        entity.requester = request.requester;
        r.items.each {
            def o = [:];
            o.item = it.item;
            o.handler = it.handler;
            o.unit = it.unit;
            o.qtyrequested = it.qty;
            o.qtyreceived = 0;
            entity.items << o;
        }
        mode = "create";
        return "default";
    }

    def itemModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as EditorListModel;

    def getDetailHandler() {
        if(!selectedItem) return null;
        if(selectedItem.handler ) {
            String n = "stockreceiptitem:" + selectedItem.handler
            try {
                return InvokerUtil.lookupOpener( n, [item:selectedItem] );
            }
            catch(e) {
                println e.message;
            }
        }
        else {
            return null;
        }
    }

    def save() {
        if( !entity.items.findAll{ it.qtyreceived > 0 } )
            throw new Exception("Please receive at least one item");

        if( MsgBox.confirm("You are about to submit this transaction. Proceed?")) {
            entity = service.create(entity);
            mode = "read";
            MsgBox.alert("Record successfully saved!");
        }
    }

    def print() {
        entity.reqno = entity.request.reqno;
        entity.dtfiled = entity.request.dtfiled;
        entity.requester = entity.request.requester;
        entity.items.each { itm -> 
            itm.qty = itm.qtyrequested
            itm.series = itm.remarks 
        }   
        return InvokerUtil.lookupOpener("stockrequest:ris", [entity: entity]);
    }
}
      