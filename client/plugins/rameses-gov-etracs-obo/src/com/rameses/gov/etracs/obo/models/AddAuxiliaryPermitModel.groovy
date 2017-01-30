package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class AddAuxiliaryPermitModel  {
    
    @Service("PersistenceService")
    def persistenceService;
    
    @Script("Lov")
    def lov;
    
    def app;
    def handler;
    def entity;
    
    boolean showConfirm = false;
    
    public void create() {
        entity = [:];
        entity._schemaname = 'obo_auxiliary_permit';
        entity.app = app;
        entity.state = 'PENDING';
    }
    
    public def getLookupUser() {
        def r = entity.type?.toUpperCase();
        def p = [query: [domain:'OBO', role: r] ];
        return Inv.lookupOpener( "sys_user_role:lookup", p ); 
    }
    
    public def doOk() {
        persistenceService.create( entity );
        handler(entity);
        create();
    }
    
    public def doCancel() {
        return "_close"; 
    }
    
}