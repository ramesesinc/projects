package com.rameses.enterprise.inventory.stock;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;

public abstract class StockSaleController {

    @Binding
    def binding

    @Service("StockInventoryService")
    def service;
        
    @Service("StockSaleService")
    def saleSvc;

    @Service("StockRequestService")
    def stockReqSvc;

    def entity;
    def request;
    def selectedItem;
    def mode = "initial";

    def create() {
        entity = [items:[]];
        entity.objid = "STKSL"+new UID();
        return "initial";
    }

    def open() {
        entity = saleSvc.open( entity ) 
        if( entity.items )  selectedItem = entity.items[0] 
        mode = 'read'
        return "default"
    }

    def enterQty() {
        def r = stockReqSvc.open([objid: request.objid]);
        entity.request = request;
        entity.reqtype = r.reqtype;
        entity.itemclass = r.itemclass;
        entity.soldto = request.requester;
        r.items.each {
            def o = [:];
            o.item = it.item;
            o.handler = it.handler;
            o.unit = it.unit;
            o.qtyrequested = it.qty;
            o.qtyissued = 0;
            entity.items << o;
        }
        mode = "enterqty";
        title = "Specify Qty to Issue";
        return "enterqty";
    }

    def itemModel = [
        onColumnUpdate: { o,col->
            if(o.qtyissued > o.qtyrequested )
                throw new Exception("Qty issued must be less or equal to qty requested");
        },
        fetchList: { o->
            return entity.items;
        }
    ] as EditorListModel;

    def getSaleHandler() {
        if(!selectedItem) return null;
        if(selectedItem.handler ) {
            String n = "stocksaleitem:" + selectedItem.handler.toLowerCase() 
            try {
                return InvokerUtil.lookupOpener( n, [item:selectedItem] );
            } catch(e) { println e.message; }
        }
        else {
            return null;
        }
    }

    def process() {
        if( !entity.items.find{ it.qtyissued > 0  })
            throw new Exception("Please process at least one item");
        entity = service.getAvailable(entity);   
        itemModel.reload();
        mode = "process";
        title = "Specify Sale Cost";
        return "default";
    }

    def revert() {
        mode = "enterqty";
        return "enterqty";
    }

    void save() {
        if(MsgBox.confirm("You are about to submit this transaction. Continue?")) {
            saleSvc.create(entity);
            entity = saleSvc.open(entity);
            mode ="read";
        }
    }

    
    def print() {
        entity.reqno = entity.request.reqno;
        entity.dtfiled = entity.request.dtfiled;
        entity.requester = entity.request.requester;
        entity.items.each { itm -> 
            itm.qty = itm.qtyrequested
            itm.qtyreceived = itm.qtyissued     
            def srs = []
            itm.items.each{
                if( it.startseries )
                    srs <<  it.startseries.toString().padLeft(it.serieslength, '0') + " - " + it.endseries.toString().padLeft(it.serieslength, '0') + " ( " + (it.startstub == it.endstub ? it.startstub+"" : ( it.startstub + " - " + it.endstub )) + " ) "
                else 
                    srs << " Stub No: " + (it.startstub == it.endstub ? it.startstub+ "" : ( it.startstub + " - " + it.endstub ))
               
            }
            itm.series = srs.join(',')
        }   

        return InvokerUtil.lookupOpener("stockrequest:ris", [entity: entity]);
    }
}

        
      