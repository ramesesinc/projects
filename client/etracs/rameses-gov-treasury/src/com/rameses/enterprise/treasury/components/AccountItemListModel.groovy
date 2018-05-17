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
    String totalsFieldName;
    def query;
    
    void updateTotal() {
        total = getValue().sum{ it.amount };
        binding.refresh("total");
        if(totalsFieldName) setValue( totalsFieldName, total );
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
        if(query) {
            p.put("query", query );
        }
        p.onselect = { o->
            selectedItem.item = o;
            selectedItem.amount = o.remove("amount");
            selectedItem.remarks = o.remove("remarks");
        };
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", p );
    }
    
}