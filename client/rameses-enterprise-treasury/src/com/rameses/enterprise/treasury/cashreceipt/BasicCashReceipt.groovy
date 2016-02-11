package com.rameses.enterprise.treasury.cashreceipt; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
        
public class BasicCashReceipt extends AbstractCashReceipt {

    void init() {
        super.init();
    }

    def itemListModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: {o-> 
            o.objid = 'RCTI' + new java.rmi.server.UID();
            entity.items << o;
            entity.amount += o.amount;
            updateBalances();
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
                updateBalances();
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
            updateBalances();
            return true;
        }
    ] as EditorListModel;
            
    def selectedItem;
    def getLookupItems() {
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup",[
            "query.txntype" : "cashreceipt",
            "query.collectorid" : entity.collector.objid,
            "query.fund" : entity.collectiontype.fund,
            "query.collectiontype": entity.collectiontype, 
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
    
    def getCollectionGroupHandler() {
        return InvokerUtil.lookupOpener("collectiongroup:lookup", [ 
            "query.txntype" : "cashreceipt", 
            "query.fund" : entity.collectiontype.fund, 
            "query.collectiontype": entity.collectiontype, 
            selectHandler: { o-> 
                    entity.items.addAll(o);
                    itemListModel.reload();
                    entity.amount = entity.items.sum{it.amount}
                    super.updateBalances(); 
            }]  );
    }
            
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
}