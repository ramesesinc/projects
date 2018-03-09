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
    

}