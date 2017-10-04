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
    
    def txnTypes = ["PURCHASE", "BEGIN_BALANCE"];
    def selectedItem;
    
    
    public def getUnitList() {
        return selectedItem.item.units;
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
        e.refno = entity.receiptno;
        e.refid = entity.objid;
        e.item = selectedItem.item;
        e.unit = selectedItem.unit;
        e.qtyrequested = selectedItem.qtyrequested;
        e.qtyreceived = selectedItem.qtyreceived; 
        if(!e.qtyreceived) e.qtyreceived = 0;
        return Inv.lookupOpener( "afreceiptitem_detail:add", [ entity:e, handler:h ] );
    }
    
    void removeEntry(def o) {
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