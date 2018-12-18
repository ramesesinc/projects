package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JevModel  extends CrudFormModel { 

    @Service('JevService')
    def jevSvc; 
    
    def selectedFund;
    String printFormName = 'jev';

    def numformat = new java.text.DecimalFormat('#,##0.00'); 
    
    def getFormattedAmount() {
        if ( !(entity.amount instanceof Number )) {
            entity.amount = 0.0; 
        }
        return numformat.format( entity.amount ); 
    }
    
    def listModel = [
        fetchList: { o->
            return entity.items;
        },
        openItem: { item, colName ->
            return Inv.lookupOpener("jevfund:open", [entity: [objid: item.objid ]]);
        }
    ] as BasicListModel;
    
    def viewFund() {
        if(!selectedFund) throw new Exception("Please select an entry");
        def op = Inv.lookupOpener("jevfund:open", [entity: [objid: selectedFund.objid] ] );
        op.target = "popup";
        return op;
    }
    
    def viewRef() {
        def op = Inv.lookupOpener(entity.reftype + ":open", [entity: [objid: entity.refid] ] );
        op.target = "popup";
        return op;
    } 
    
    def post() {
        def h = { o->
            entity.putAll(o);
            binding.refresh();
        }
        return Inv.lookupOpener("jevno_entry", [entity: [objid: entity.objid ], handler: h])
    } 
    
} 