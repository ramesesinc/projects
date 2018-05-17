package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class CashTicketListModel extends com.rameses.seti2.models.CrudListModel {
    
    boolean isCreateAllowed() { 
        if ( !super.isCreateAllowed() ) return false; 
        
        def tag = invoker?.properties?.tag; 
        return tag.toString().matches('collector|subcollector'); 
    }  
    
    def create() { 
        def op = Inv.lookupOpener('cashticket:create', [:]); 
        op.target = 'window';
        return op;
    }
}