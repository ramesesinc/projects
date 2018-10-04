package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class CashTicketListModel extends CrudListModel {
    
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