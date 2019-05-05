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
        
    public void initColumn( c ) { 
        if ( c.name == 'series' ) { 
            c.visible = false; 
            c.hidden = true; 
        } 
        else if ( c.name == 'formno') {
            c.width = 80; 
            c.maxWidth = 100; 
        }
        else if ( c.name == 'receiptno') {
            c.width = 100; 
            c.maxWidth = 150; 
        }
        else if ( c.name == 'receiptdate') {
            c.width = 100; 
            c.maxWidth = 150;
        }
        else if ( c.name == 'stub') {
            c.width = 60; 
            c.maxWidth = 80;
            c.type = 'integer';
        }
        else if ( c.name == 'amount') {
            c.width = 100; 
            c.maxWidth = 120;
        }
        else if ( c.name == 'voided') {
            c.width = 60; 
            c.maxWidth = 80;
        }        
        else if ( c.name == 'collector.name') {
            c.caption = 'Collector'; 
        } 
    }     
}