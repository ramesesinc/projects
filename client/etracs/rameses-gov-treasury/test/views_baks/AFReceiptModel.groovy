package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;


class AFReceiptModel extends CrudFormModel {
    
    @Service("AFReceiptService")
    def afRct;
    
    def txnTypes = ["PURCHASE", "FORWARD"];
    def selectedItem;
    
    String printFormName = "afris";
    
     def getPrintFormData() {
        def m = [:];
        m.putAll( entity );
        m.items.each {
            it.qty = it.qtyrequested;
            it.qtyout = it.qtyreceived;
        }
        return m;
    }
    
    @FormTitle
    String getTitle() {
        if( mode == 'create')
            return "New AF Receipt";
        else    
            return "AFR " + entity.controlno;
    }
    
    @FormId
    String getFormId() {
        if( mode == 'create')
            return "afreceipt";
        else    
            return entity.objid;
    }
    
    public void afterCreate() {
        entity.items = [];
    }
    
    public def getInfo() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/templates/AFReceiptDetail.gtpl", [entity:entity] );
    }
    
    def itemListHandler = [
        fetchList : { o->
            return entity.items;
        },
        onAddItem: { o->
            o.qtyreceived = 0;
            entity.items << o;
        },
        onRemoveItem: { o->
            entity.items.remove(o);
        },
        onColumnUpdate: { o,colName->
            if( colName == "item" ) {
                o.item.objid = o.item.itemid
                o.unit = o.item.unit
            }
        }
    ] as EditorListModel;
    
    
    def addEntry( def o ) {
        def h = { vv->
            afRct.addNewBatch(vv);
            reloadEntity();
        }
        selectedItem = entity.items.find{ it.item.objid == o.id };
        def e = [:];
        e.dtfiled = entity.dtfiled; 
        e.refno = entity.controlno;
        e.refid = entity.objid;
        e.item = selectedItem.item;
        e.unit = selectedItem.unit;
        e.qtyrequested = selectedItem.qtyrequested;
        e.qtyreceived = selectedItem.qtyreceived; 
        e.cost = selectedItem.cost;
        e.respcenter = entity.respcenter;
        e.txntype = selectedItem.txntype;
        if(!e.qtyreceived) e.qtyreceived = 0;
        return Inv.lookupOpener( "afreceiptitem_detail:add", [ entity:e, handler:h, itemid: selectedItem.objid ] );
    }
    
    void removeEntry(def o) {
        o.receiptid = entity.objid;
        afRct.removeBatch(o);
        reloadEntity();
    }
    
    void post() {
        if( MsgBox.confirm("You are about to update the AF inventory. Proceed?")) {
            afRct.post([objid:entity.objid]);
            reloadEntity();
        }
    }
    
}    