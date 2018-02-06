package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class PaymentListModel extends CrudListModel {
    
    boolean showVoid = false;
    
    def selectedItem;
    
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
        if(!caller.entity?.objid )
            throw new Exception("caller entity must not be null in PaymentListModel.getCustomFilter")
        arr <<  parentkey + "= :parentid";
        if( showVoid  != true ) {
            arr << " voided = 0 ";
        } 
        def p = [ parentid : caller.entity.objid];
        return [arr.join(" AND "), p ]
    }
    
    public void onOpenItem( def o, def colName ) {
        viewPayment();
    }
    
    void viewPayment() {
        if(!selectedItem) throw new Exception("Please select a payment item");
        def m = [entity: [objid: selectedItem.refid ] ];
        def str = selectedItem.reftype + "info:open";
        def op = Inv.lookupOpener( str, m );
        Modal.show(op);
    }

    def capturePayment() {
        return Inv.lookupOpener( schemaName + ":create" , [parent: caller.entity ]  );
    }
    

}