package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class BillOfMaterialsModel {
    
    @Service("WaterworksBillOfMaterialService")
    def bomSvc;
    
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def handler;
    
    def selectedItem;
    def deletedItems = [];
    def bomItems = [];
    def bomtotal;
    def state;
    def payOption;
    
    def getEntity() {
        return caller.entity;
    }
    
    def load() {
        bomItems = bomSvc.getList( [objid: entity.objid] );
        bomtotal = bomItems.sum{ it.linetotal };
    }
    
    void initQty() {
        load();
        state = "qty";
    }
    
    void initAvailability() {
        load();
        state = "availability";
    }
    
    void initBilling() {
        load();
        state = "billing";
    }

    void refreshTotals() {
        if( !bomItems) {
            bomtotal = 0;
        }
        else {
            bomtotal = bomItems.sum{ it.linetotal };
        }
        binding.refresh("bomtotal");
    }
    
    def listHandler = [
        onAddItem: { o->
            bomItems.add( o );
            o.added = true;
            refreshTotals();
        },
        beforeColumnUpdate: {o, colName, newValue->
            if(colName == "item") {
                if( bomItems.findAll{it.item.objid == newValue.objid}) {
                    throw new Exception("Item already exists");
                }
            }
            else if(colName=="qtyissued" ) {
                if( o?.cwdsupplied == 0 )
                    throw new Exception("Qty issued cannot be edited");
                if(newValue>o.qty)
                    throw new Exception("Qty issued must be less than qty requested");
            }
            return true;
        },
        afterColumnUpdate: { o, colName ->
            if(colName == "item") {
                o.cwdsupplied = 1;                
                o.qty = 0;
                o.qtyissued = 0;
                o.linetotal = 0;
            } 
            else if( colName.matches("qtyissued") ) {
                o.linetotal = o.item.unitprice * o.qtyissued; 
            }
            else if( colName.matches("qty") ) {
                o.qtyissued = o.qty;
                o.linetotal = o.item.unitprice * o.qtyissued; 
            }
            else if( colName.matches("cwdsupplied") ) {
                if( o.cwdsupplied == 0 ) {
                    o.qtyissued = 0;
                    o.linetotal = 0; 
                }
                else {
                    o.qtyissued = o.qty;
                    o.linetotal = o.item.unitprice * o.qty; 
                }
            }
            refreshTotals();
        },
        onRemoveItem: { o->
           deletedItems.add(o);
           bomItems.remove(o);
           refreshTotals();
           return true; 
        },
        fetchList: { o->
            return bomItems;
        }
    ] as EditorListModel;

    def close() {
        return "_close";
    }
    
    def buildParam() {
        def m = [objid: entity.objid];
        m.materials = bomItems;
        m.put("materials::deleted", deletedItems);
        return m;
    }
    
    def save() {
        bomSvc.update(buildParam());
        return "_close";
    }
    
    def verifyMaterials() {
        bomSvc.verify(buildParam());
        return "_close";
    }
    
    def showPayOption() {
        payOption = [amount: bomtotal, type:'fullpayment', downpayment: 0]; 
        state = 'payoption';
        return state;
    }
    
    @PropertyChangeListener
    def updater = [
        "payOption.term": {
            payOption.amortization = payOption.amount / payOption.term;
            binding.refresh("payOption.amortization");
        }
    ]
    
    def postBill() {
        def m = buildParam();
        m.payOption = payOption;
        bomSvc.post(m);
        caller?.refresh();
        return "_close";
    }
    
    
    
}