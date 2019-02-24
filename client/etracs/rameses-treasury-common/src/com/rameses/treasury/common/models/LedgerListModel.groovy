package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class LedgerListModel extends CrudListModel {
    
    def showOption = 0
    def showOptionList = [
        [id:0, title:'Show only unpaid items'],
        [id:1, title:'Show only paid items'],
        [id:2, title:'Show all'],
    ];
    
    boolean showVoid = false;
    
    def selectedLedgerItem;
    def selectedPaymentItem;
    
    def _paymentSchemaName;
    
    public String getContextName() {
        def pfn = invoker.properties.contextName;
        if(pfn) return pfn;
        pfn = workunit?.info?.workunit_properties?.contextName;
        if ( pfn ) return pfn; 
        return super.getSchemaName(); 
    }
    
    public def getMasterEntity() {
        return caller.entity;
    }
    
    public String getPaymentSchemaName() {
        if(_paymentSchemaName ) return _paymentSchemaName;
        _paymentSchemaName = invoker.properties.paymentSchemaName;
        if(_paymentSchemaName) return _paymentSchemaName;
        _paymentSchemaName = workunit?.info?.workunit_properties?.paymentSchemaName;
        if ( _paymentSchemaName ) return _paymentSchemaName; 
        _paymentSchemaName = getContextName() + "_payment_item";
        return _paymentSchemaName;  
    }
    
    def _parentkey;
    public String getParentkey() {
        if(_parentkey ) return _parentkey;
        _parentkey = invoker.properties.parentkey;
        if(_parentkey) return _parentkey;
        _parentkey = workunit?.info?.workunit_properties?.parentkey;
        if ( _parentkey ) return _parentkey; 
        throw new Exception("Please indicate a parentkey")      
    }
    
    def getCustomFilter() {
        def arr = [];
        def p = [:];
        if(caller?.entity) {
            arr << parentkey + "= :parentid";
            p.parentid = caller.entity.objid;
        }
        if( showOption == 0 ) arr << "amount -amtpaid -discount > 0";
        else if( showOption == 1 ) arr << "amount - amtpaid - discount = 0";
        if( arr ) {
            return [ arr.join(" AND "), p ];
        }
        return null;
    }
    
    def paymentListModel = [
        fetchList: { o->
            if( !selectedItem ) return [];
            def m = [_schemaname: paymentSchemaName ];
            def arr = [];
            def p = [:];
            arr << " refid = :refid"
            p.refid = selectedItem.objid; 
            if( showVoid  != true ) {
                arr << " parent.voided = 0 ";
            } 
            m.where = [ arr.join(" AND "), p ];
            return queryService.getList(m);
        },
        onOpenItem: { o,col->
            viewPayment();
        }
    ] as BasicListModel;

    void viewPayment() {
        if(!selectedPaymentItem) throw new Exception("Please select a payment item");
        def m = [entity: [objid: selectedPaymentItem.parent.refid ] ];
        def str = selectedPaymentItem.parent?.reftype + ":open";
        def op = Inv.lookupOpener( str, m );
        Modal.show(op);
    }
    
    def addNewEntry() {
        return Inv.lookupOpener( schemaName + ":create" , [parent: caller.entity ]  );
    }
    
    def editEntry() {
        return openEntry(); 
    }
    
    def openEntry() {
        return Inv.lookupOpener( schemaName + ":open" , [entity: selectedItem ]);
    }

    void removeEntry() {
        if(!selectedItem) throw new Exception("Please select an item first");
        if(selectedItem.amtpaid > 0 ) throw new Exception("Cannot delete item if amtpaid is not zero");
        
        if ( MsgBox.confirm('You are about to remove the selected item?')) {
            def m = [_schemaname: schemaName ];
            m.findBy = [objid:selectedItem.objid];
            persistenceService.removeEntity( m );
            reload();
        }
    }
}