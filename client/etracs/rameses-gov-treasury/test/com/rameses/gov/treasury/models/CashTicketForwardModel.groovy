package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;
import java.rmi.server.*;

class CashTicketForwardModel {
   
    @Binding
    def binding

    @Service("CashTicketCollectorMigrationService")
    def svc;
    
    String title = "Forward CashTicket";

    def entity;
    def formTypes;
    def formtype;
    def mode;
    def endseries;

    def _aftypes; 
    def _collectors;
    
    public void init() { 
        mode = 'create';
        entity = [:];

        def result = svc.init(); 
        if ( result ) { 
            entity.refdate = result.txndate; 
            _collectors = result.collectors; 
            _aftypes = result.aftypes; 
        } 
    }
    
    public void create() {
        init();
        binding.refresh();
    } 

    public void save() { 
        new java.math.BigDecimal( entity.qty.toString() ); 

        if (MsgBox.confirm('You are about to submit this transaction. Continue?')) { 
            entity.reason = 'FORWARD BALANCE'; 
            entity.remarks = 'FORWARD BALANCE'; 
            entity.reftype = 'SYSTEM'; 
            entity.refid = 'SYSTEM'; 
            svc.post( entity ); 
            mode = 'posted'; 
        } 
    }

    List getCollectorlist( ) {
        return _collectors; 
    } 

    List getFormTypes() { 
        return _aftypes; 
    } 
} 