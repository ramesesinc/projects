package com.rameses.enterprise.inventory.stock;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;

public class StockRequestController {

    @Binding
    def binding;
    
    @Service("StockRequestService")
    def service;
        
    @Invoker
    def invoker;

    def entity;
    def entityName = "stockrequest";
    String title;
    String mode = "create";
    def selectedItem;

    void create() {
        entity.objid = "STKREQ"+new UID();
        mode = "create";
    }

    void open() {
        entity = service.open( entity );
        title = "Stock Request ( " +entity.reqtype + " )";
        entityName = "stockrequest:"+entity.reqtype.toLowerCase();
        mode = "read";
    }


    def popupReports(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
            
    public def getLookupItems() {
        return InvokerUtil.lookupOpener( "stockitem:lookup", ['query.itemclass': entity.itemclass,
            onselect: { o->
                if( entity.items.findAll{( it.item.objid==o.objid && it.unit==o.unit)})
                    throw new Exception("Item is already selected");
                    
                selectedItem.item = o;
                selectedItem.unit = o.unit;
                selectedItem.handler = o.type?.toLowerCase();
            }
        ] );
    }

    def getUnitList() {
        def items = [];
        items << selectedItem.item?.baseunit;
        return items;
    }

    def itemModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: { o->
            entity.items << o;
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("Delete item? ")) return false;

            entity.items.remove(o)
            return true;
        }
    ] as EditorListModel;

    def save() {
        if(!entity.items)
            throw new Exception("Please indicate at least one item");

       if(MsgBox.confirm("You are about to submit this form. Continue?")) {
            entity = service.create(entity);
            mode = "read";
       }
       return null;
    }

    def print() {
        return InvokerUtil.lookupOpener("stockrequest:ris", [entity: entity]);
    }
}