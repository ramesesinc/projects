package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class CapturePaymentModel  {
    
    @Service("CapturePaymentService")
    def svc;        

    @Controller
    def workunit;
        
    @Binding
    def binding;
    
    @Invoker
    def invoker;
    
    @Caller
    def caller;
    
    String _schemaName_;
    
    def entity;
    def handler;
    def selectedItem;
    def query = [:];
    
    public String getSchemaName() {
        if( _schemaName_ )
            return _schemaName_;
        else    
            return workunit?.info?.workunit_properties?.schemaName;
    }
    
    public void setSchemaName( String s ) {
        this._schemaName_ = s;
    }
    
    public void init() {
        if(!schemaName) throw new Exception("schemaName is required in CapturePaymentModel");
        query._schemaname = schemaName;
        entity = svc.init( query );
    }
    
    def doOk() {
        def paidItems = entity.items.findAll{it.selected == true };
        if( entity.amount != entity.total) {
            throw new Exception("amount must be equal to the total items");
        }
        
        def unpaidItems = entity.items.findAll{it.selected == false};
        //check that the paymentpriority must be in order.
        
        def maxPriority = paidItems.max{ it.priority }.priority;
        def hasLessPriority = unpaidItems.find{ it.priority < maxPriority };
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
        onColumnUpdate: { o, colName->
            if(colName == 'selected') {
                if( o.selected == false ) {
                    o.amount = 0;
                    o.surcharge = 0;
                    o.interest = 0;
                    o.total = 0;
                }
            }
            else if(colName.matches('amount|surcharge|interest')) {
                o.total = o.amount + o.surcharge + o.interest;
            }
            entity.total = entity.items.sum{ it.total };
            binding.refresh("entity.total");
        }
    ] as EditorListModel;
}       