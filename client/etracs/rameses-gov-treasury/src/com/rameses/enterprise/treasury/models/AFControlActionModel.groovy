package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFControlActionModel  {

    @Service("AFControlService")
    def service;
    
    @Caller
    def caller;
    
    def entity;
    def actionType;
    
    void activate() {
        if(entity.active==1)
            throw new Exception("Entry is already active");
        service.activate( [objid: entity.objid ] );
        if(caller) caller.reloadEntity();
    }

    void deactivate() {
        if(entity.active==0)
            throw new Exception("Entry is already inactive");
        service.deactivate( [objid: entity.objid ] );
        if(caller) caller.reloadEntity();
    }
    
     def assignFund() {
        if( entity.fund?.objid ) {
            throw new Exception("Fund is already assigned");
        }
        def h = { o->
            service.assignFund( [objid: entity.objid, fund: o ] );
            if(caller) caller.reloadEntity();
        };
        return InvokerUtil.lookupOpener('fund:lookup', [onselect: h]);
    }

    def unassignFund() {
        if( !entity.fund?.objid ) {
            throw new Exception("Fund is already unassigned");
        }
        service.unassignFund( [objid: entity.objid ] );
        if(caller) caller.reloadEntity();
    }

    def assignSubcollector() {
        def h = { o->
            service.assignSubcollector( [objid: entity.objid, assignee: o ] );
            if(caller) caller.reloadEntity();
        };
        return InvokerUtil.lookupOpener('subcollector:lookup', [onselect: h]);
    }

    def unassignSubcollector() {
        service.unassignSubcollector( [objid: entity.objid, owner: entity.owner ] );
        if(caller) caller.reloadEntity();
    }

    def changeMode() {
        actionType = "changemode";
        return "changemode";
    }

    def doOk() {
        if( actionType == "changemode" ) {
            service.changeMode( [objid: entity.objid, txnmode: entity.txnmode ] );
        }
        if(caller) caller.reloadEntity();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }


    
}    