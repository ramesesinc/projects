package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


abstract class AFTxnHandler {

    @Binding
    def binding;

    @Invoker
    def invoker;
    
    @Service("AFTxnService")
    def svc;
    
    @Service("QueryService")
    def queryService;
    
    @Service("PersistenceService")
    def persistenceService;

    def entity;
    def handler;
    def afrequest;
    def mode = "create";
    
    def init() { 
        return (mode == "create") ? "create" : "open"; 
    }
    
    public def getInfo() {
        return null;
    }

    
    def saveCreate() {
        if(mode!='create') throw new Exception("Save only applicable for create");
        def b = MsgBox.confirm( "You are about to save the draft document. Proceed?" );
        if ( !b ) throw new BreakException();
        entity._schemaname = "aftxn";
        def e = persistenceService.create(entity);
        entity.objid = e.objid;
        reloadEntity();
        mode = "open";
        return "open";
    }

    String getTitle() {
        return invoker.caption;
    }

    def close() {
        handler.back();
        return "_close";
    }
    
    def exit()  {
        handler.exit();
        return "_close";
    }
    
    def forward() {
        handler.forward();
        return "_close";
    }
    
    def post() {
        if(!MsgBox.confirm("You are about to post this to inventory. Please verify if this is correct")) return false;
        svc.post([objid:entity.objid ]);
        return forward();
    }

    void reloadEntity() {
        def e = persistenceService.read( [_schemaname:'aftxn', objid:entity.objid ] );
        entity.clear();
        entity.putAll( e );
        binding.refresh();
    }
   
}    