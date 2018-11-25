package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class CreditListModel extends CrudListModel {
    
    boolean includePaid = false;
    def total = 0;
    
    def selectedItem;
    boolean showVoid = false;
    
    def selectedPaymentItem;
    def _paymentSchemaName;
    
    public String getContextName() {
        def pfn = invoker.properties.contextName;
        if(pfn) return pfn;
        pfn = workunit?.info?.workunit_properties?.contextName;
        if ( pfn ) return pfn; 
        return super.getSchemaName(); 
    }
    
    public String getSchemaName() {
        String s = super.getSchemaName();
        if(!s) return getContextName() + "_credit";
    }
    
    def _parentkey = "parentid";
    public String getParentkey() {
        if(_parentkey ) return _parentkey;
        _parentkey = invoker.properties.parentkey;
        if(_parentkey) return _parentkey;
        _parentkey = workunit?.info?.workunit_properties?.parentkey;
        if ( _parentkey ) return _parentkey; 
        throw new Exception("Please indicate a parentkey")      
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

    public boolean isCaptureExist() {
        try {
            def op = Inv.lookupOpener(schemaName + ":create", [:] );
            if( op ) return true;
            return false;
        }
        catch(e) {
            return false;
        }
    }
    
    def getCustomFilter() {
        def arr = [];
        if(!caller.entity?.objid )
            throw new Exception("caller entity must not be null in CreditListModel.getCustomFilter")
        arr <<  parentkey + "= :parentid";
        if( includePaid  != true ) {
            arr << " amount - amtpaid > 0 ";
        } 
        def p = [ parentid : caller.entity.objid];
        return [arr.join(" AND "), p ]
    }
    
    public def open() {
        viewPayment();
        return null;
    }
    
    void viewPayment() {
        if(!selectedItem) throw new Exception("Please select a payment item");
        def m = [entity: [objid: selectedItem.refid ] ];
        def str = selectedItem.reftype + "info:open";
        def op = Inv.lookupOpener( str, m );
        Modal.show(op);
    }

    def applyPayment() {
        if(!selectedItem) throw new Exception("Please select an item");
        def m = [:];
        m.parent = [objid: caller.entity.objid ];
        m.entity = [refid: selectedItem.objid, refdate: selectedItem.refdate, reftype: 'creditpayment' ];
        m.amtpaid = selectedItem.amount - selectedItem.amtpaid;
        m.amount = m.amtpaid;
        String n = contextName + "_payment:create";
        
        return Inv.lookupOpener( n, m  );
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
            m.orderBy = "refdate DESC";
            return queryService.getList(m);
        },
        onOpenItem: { o,col->
            viewPayment();
        }
    ] as BasicListModel;

    void viewPaymentReceipt() {
        if(!selectedPaymentItem) throw new Exception("Please select a payment item");
        def m = [entity: [objid: selectedPaymentItem.parent.refid ] ];
        def str = selectedPaymentItem.parent?.reftype + "info:open";
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