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
                if ( valuetype.matches('FIXED|FIXEDUNIT')) return false; 
            }
            return true; 
        }, 
        onColumnUpdate: {o,name-> 
            recalcTotalAmount();
        },
        onUpdateItem: { o-> 
            recalcTotalAmount(); 
        }, 
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
                
            entity.items.remove( o );
            recalcTotalAmount(); 
            return true;
        }
    ] as EditorListModel;
            
    void recalcTotalAmount() {
        entity.amount = 0.0; 
        if ( entity.items ) {
            entity.amount = entity.items.sum{( it.amount ? it.amount : 0.0 )} 
            if ( entity.amount == null ) entity.amount = 0.0; 
        } 
        updateBalances();        
    }
    
    def selectedItem;
    def getLookupItems() {
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup",[
            "query.txntype" : "cashreceipt",
            "query.collectorid" : entity.collector.objid,
            "query.fund" : entity.collectiontype.fund,
            "query.collectiontype": entity.collectiontype, 
            onselect:{ o->                  
                def itm = [ item: o ];                 
                itm.amount = (o.defaultvalue ? o.defaultvalue : 0.0); 
                if ( o.valuetype == "FIXEDUNIT" ) {
                    def sqty = MsgBox.prompt( "Enter qty" );
                    if( !sqty || sqty == "null" ) throw new Exception("Please provide qty"); 
                    if( !sqty.isInteger() ) throw new Exception("Qty must be numeric");
                    
                    def nqty = Integer.parseInt( sqty );
                    itm.amount = itm.amount * nqty;
                    itm.remarks = "qty@"+ nqty;
                } 
                selectedItem.putAll( itm );                
            } 
        ]); 
    }
    
    def getCollectionGroupHandler() {
        return InvokerUtil.lookupOpener("collectiongroup:lookup", [ 
            "query.txntype" : "cashreceipt", 
            "query.fund" : entity.collectiontype.fund, 
            "query.collectiontype": entity.collectiontype, 
            selectHandler: { o-> 
                def fxunits = o.findAll{( it.item?.valuetype == 'FIXEDUNIT' )}
                if ( fxunits ) {
                    def sqty = MsgBox.prompt("Enter qty");
                    if( !sqty || sqty == "null" ) throw new Exception("Please provide qty"); 
                    if( !sqty.isInteger() ) throw new Exception("Qty must be numeric"); 
                    
                    def nqty = Integer.parseInt( sqty ); 
                    fxunits.each{ fxu-> 
                        fxu.amount = (fxu.amount ? fxu.amount : 0.0) * nqty; 
                        fxu.remarks = "qty@"+ nqty;
                    } 
                }
                entity.items.addAll(o);
                itemListModel.reload();
                entity.amount = entity.items.sum{(it.amount ? it.amount : 0.0)}
                super.updateBalances(); 
            }]  );
    }
            
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
}