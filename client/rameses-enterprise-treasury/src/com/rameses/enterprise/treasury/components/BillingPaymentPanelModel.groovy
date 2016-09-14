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
public class BillingPaymentPanelModel extends ComponentBean {

    @Binding
    def binding;
    
    def handler;
    def total;
    
    def bill;
    def selectedItem;
    def payOption = [type:'FULL'];
    
    void reload() {
        def m = [:];
        m.putAll( getValue() );
        m.payOption = payOption;
        bill = handler.getBilling(m);
        getValue().putAll(bill);
        total = bill.total;
        binding.refresh("total");
        handler.afterLoad();  
        listModel.reload();
    }
    
    def listModel = [
        fetchList: { o->
            if( !bill) return [];
            return bill.items;
        }
    ] as BasicListModel;
    
    def applyPayment() {
        def h = { o->
            payOption = o;
            reload();
        };    
        return Inv.lookupOpener("billing:payoption", [handler:h]); 
    }
    
}