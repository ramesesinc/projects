package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*
        
public class CapturePaymentModel  {
    
    @Service("CapturePaymentService")
    def svc;        

    def entity;
    def handler;
    
    String schemaName;
    
    public void init() {
        if(!schemaName) throw new Exception("schemaName is required in CapturePaymentModel");
        entity = svc.init( [_schemaname: schemaName] );
    }
    
    def doOk() {
        def paidItems = entity.items.findAll{it.selected == true };
        def unpaidItems = entity.items.findAll{it.selected == false};
        //check that the paymentpriority must be in order.
        
        def maxPriority = paidItems.max{ it.paymentpriority }.paymentpriority;
        def hasLessPriority = unpaidItems.find{ it.paymentpriority < maxPriority };
        if(hasLessPriority) {
            throw new Exception("Please ensure payment priority is in order");
        }
        def m = [_schemaname:schemaName];
        m.putAll(entity);
        m.items = paidItems;
        svc.post( m );
        if(handler)handler();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
    def itemHandler = [
        fetchList: {o->
            return entity.items;
        },
        isMultiSelect: {
            return true;
        }
    ] as BasicListModel;
}       