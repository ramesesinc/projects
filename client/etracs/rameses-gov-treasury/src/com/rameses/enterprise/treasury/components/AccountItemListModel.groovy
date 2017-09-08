package com.rameses.enterprise.treasury.components;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.common.ComponentBean;

/****
* This component is used in the ff. cases:
*     Billing Panel in Account
*     Cash Receipt payment
*     Capture Amount Payment
*      
*     getValue() here refers to the entity 
*/
public class AccountItemListModel extends ComponentBean {

    def total = 0;
    
    void updateTotal() {
        total = getValue().sum{ it.amount };
        binding.refresh("total");
    }
    
    def itemListModel = [
        fetchList: { o->
            return getValue();
        },
        onAddItem: {o-> 
            o.objid = 'RCTI' + new java.rmi.server.UID();
            getValue() << o;
            updateTotal();
        },
        isColumnEditable: { o,name-> 
            if ( name == 'amount' ) { 
                def valuetype = o?.item?.valuetype.toString().toUpperCase();
                if ( valuetype == 'FIXED' ) return false; 
            }
            return true; 
        }, 
        onColumnUpdate: {o,name-> 
            updateTotal();
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
            getValue().remove( o );
            updateTotal();
            return true;
        }
    ] as EditorListModel;
            
    def selectedItem;
    def getLookupItems() {
        def p = [:];
        p.put("query.txntype", "cashreceipt" );
        //p.put("query.collectorid",entity.collector.objid);
        //p.put("query.fund",entity.collectiontype.fund);
        //p.put("query.collectiontype", entity.collectiontype);
        p.onselect = { o->
            selectedItem.item = o;
            selectedItem.amount = o.defaultvalue;
            if(o.valuetype == "FIXEDUNIT") {
                def m = MsgBox.prompt( "Enter qty" );
                if( !m || m == "null" ) throw new Exception("Please provide qty"); 
                if( !m.isInteger() ) throw new Exception("Qty must be numeric"); 
                selectedItem.amount = Integer.parseInt( m )*o.defaultvalue; 
                selectedItem.remarks = "qty@"+Integer.parseInt( m ); 
            } 
        };
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", p );
    }
    
    def getCollectionGroupHandler() {
        def p = [:];
        p.put("query.txntype","cashreceipt"); 
        //p.put("query.fund",entity.collectiontype.fund); 
        //p.put("query.collectiontype", entity.collectiontype); 
        p.selectHandler = { o-> 
            getValue().addAll(o);
            itemListModel.reload();
            updateTotal(); 
        };
        return InvokerUtil.lookupOpener("collectiongroup:lookup", p); 
    }
            
    
}