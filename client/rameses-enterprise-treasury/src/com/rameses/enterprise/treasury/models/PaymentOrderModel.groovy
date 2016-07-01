package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;
        
public class PaymentOrderModel extends CrudFormModel {
    
    @Caller
    def caller;
    
    def txntype;
    def selectedItem;
    def trackingno;
    def barcode;
    
    void afterCreate() {
        entity.items = [];
        entity.amount = 0;
        entity.state = 'OPEN';
        entity.txntype = txntype?.objid;
    }

    def doOk() {
        super.save();
        trackingno = 'PMT:'+entity.controlno;
        return "end";
    }

    def doCancel() {
        return "_close";
    }
    
    def doClose() {
        return "_close";
    }

    def itemListModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: {o-> 
            o.objid = 'PMTI' + new java.rmi.server.UID();
            o.parent = entity;
            entity.items << o;
            entity.amount += o.amount;
        },
        isColumnEditable: { o,name-> 
            if ( name == 'amount' ) { 
                def valuetype = o?.item?.valuetype.toString().toUpperCase();
                if ( valuetype == 'FIXED' ) return false; 
            }
            return true; 
        }, 
        onColumnUpdate: {o,name-> 
            if(entity.items) {
                entity.amount = entity.items.sum{ it.amount };
            }
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
            entity.items.remove( o );
            if( entity.items ) {
                entity.amount = entity.items.sum{ it.amount };
            }
            else {
                entity.amount = 0;
            }
            return true;
        }
    ] as EditorListModel;


    def getLookupItems() {
        return InvokerUtil.lookupOpener("revenueitem:lookup",[
            onselect:{ o->
                selectedItem.item = o;
                selectedItem.amount = o.defaultvalue;
                if(o.valuetype == "FIXEDUNIT") {
                    def m = MsgBox.prompt( "Enter qty" );
                    if( !m || m == "null" ) throw new Exception("Please provide qty"); 
                    if( !m.isInteger() ) throw new Exception("Qty must be numeric"); 
                    selectedItem.amount = Integer.parseInt( m )*o.defaultvalue; 
                    selectedItem.remarks = "qty@"+Integer.parseInt( m ); 
                } 
            } 
        ]); 
    }
    
    def printTrackingNo() {
        return Inv.lookupOpener("trackingno:printout", [data: [barcode: trackingno]] );
    }
    
}  