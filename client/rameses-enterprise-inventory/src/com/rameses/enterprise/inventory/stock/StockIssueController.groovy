package com.rameses.enterprise.inventory.stock;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;

public abstract class StockIssueController {

    @Binding
    def binding

    @Service("StockInventoryService")
    def service;
        
    @Service("StockIssueService")
    def issueSvc;

    @Service("StockRequestService")
    def stockReqSvc;

    def entity;
    def request;
    def selectedItem;
    def mode = "initial";

    def create() {
        entity = [items:[]];
        entity.objid = "STKISS"+new UID();
        return "initial";
    }

    def open() {
        entity = issueSvc.open( entity ) 
        if( entity.items )  selectedItem = entity.items[0];

        getRenderer();
        mode = 'read';
        return "default";
    }

    def enterQty() {
        def r = stockReqSvc.open([objid: request.objid]);
        entity.request = request;
        entity.reqtype = r.reqtype;
        entity.itemclass = r.itemclass;
        entity.issueto = request.requester;
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
        fetchList: { o->
            return entity.items;
        },         
        onColumnUpdate: { o,col->
            if(o.qtyissued > o.qtyrequested )
                throw new Exception("Qty issued must be less or equal to qty requested");
        },
        onOpenItem: { o,colname-> 
            def opts = [data: o];
            opts.handler = { x-> 
                binding.refresh(); 
            }
            def opener = Inv.lookupOpener('stockissue_'+ o.handler + ':edit', opts); 
            return (opener? opener: null); 
        }
    ] as EditorListModel;

   

    def process() {
        if( !entity.items.find{ it.qtyissued > 0  })
            throw new Exception("Please process at least one item");
        entity = service.getAvailable(entity);   
        itemModel.reload();
        mode = "process";
        return "default";
    }

    def revert() {
        mode = "enterqty";
        return "enterqty";
    }

    void save() {
        if(MsgBox.confirm("You are about to submit this transaction. Continue?")) {
            issueSvc.create(entity);
            entity = issueSvc.open( entity ) 
            mode ="read";
        }
    }

    String getRenderer() {
        if(!selectedItem) return "";
        return TemplateProvider.instance.getResult( "com/rameses/handlers/stockissue/" + selectedItem.handler +".gtpl", [entity:selectedItem] );            
    }
}
