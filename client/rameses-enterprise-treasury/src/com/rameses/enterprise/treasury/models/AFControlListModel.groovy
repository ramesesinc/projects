package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class AFControlListModel extends ListController {

    @Service("AFService")
    def afSvc;

    @Service("AFControlService")
    def service;

    @Script("User")
    def user;

    String serviceName = "AFControlService";

    def entity = [:];
    def selectedEntity;
    def list;

    String title = "Manage Accountable Forms";
    def queryForm = new Opener(outcome:'queryform');
    boolean allowCreate = false;
    boolean allowOpen = false;

    def afTypes;
    def respCenterType;

    void init( invo ) { 
        afTypes = afSvc.getList([:]); 
        respCenterType = invo?.properties.role; 
    }

    @PropertyChangeListener
    def listener = [
        "entity.formno": { o->
            reload();
        }
    ]

    void activate() {
        if(selectedEntity.active==1)
            throw new Exception("Entry is already active");
        if( user.userid != selectedEntity.assignee.objid )
            throw new Exception("Cannot activate since you are not the current assignee");                    
        def z = [:];
        z.objid = selectedEntity.objid;
        z.txnmode = selectedEntity.txnmode;
        z.formno = selectedEntity.afid;
        z.fund = selectedEntity.fund 
        service.activateControl(z);
        listHandler.reload();    
    }

    void deactivate() {
        if(selectedEntity.active==0)
            throw new Exception("Entry is not active");
        if( user.userid != selectedEntity.assignee.objid )
            throw new Exception("Cannot activate since you are not the current assignee");                    
        service.deactivateControl(selectedEntity);
        listHandler.reload();
    }

    def assign() {
        if(selectedEntity.active==1) {
            throw new Exception("Entry is currently active. Deactivate first");
        }   
        
        def p = [:]; 
        p.onselect = { x->
            if( MsgBox.confirm("You are about to assign this stub to " + x.name + ". Continue?")) { 
                service.assignToSubcollector( [objid:selectedEntity.objid, assignee:x] ); 
                listHandler.reload(); 
            } 
        } 
        return InvokerUtil.lookupOpener( "subcollector:lookup", p ); 
    }

    def unassign() {
        if(!selectedEntity) throw new Exception("Select an item");
        service.unassign( selectedEntity );
        listHandler.load();
    }

    def changeMode() {
        if(!selectedEntity) throw new Exception("Select an item");
        return InvokerUtil.lookupOpener("af:changemode", 
            [entity:selectedEntity, saveHandler: { listHandler.reload(); }] 
        );
    }

    def assignFund() {
        if(!selectedEntity) throw new Exception("Select an item");
        def h = { o->
            selectedEntity.fund = o;
            service.assignFund( selectedEntity );
            listHandler.reload();
        }
        return InvokerUtil.lookupOpener('fund:lookup', [onselect: h]);
    }

    def unassignFund() {
        if(!selectedEntity) throw new Exception("Select an item");
        service.unassignFund( selectedEntity );
        selectedEntity.find = [:];
        listHandler.reload();
    }

    def cancelseries() {
        if(!selectedEntity) throw new Exception("Select an item");

        return InvokerUtil.lookupOpener('af:cancelseries', [entity: selectedEntity, handler:{ o-> listHandler.load() }])
    }

    def doExport(){
        return InvokerUtil.lookupOpener('afcontrol:export', [:])
    }

    def doImport(){
        return InvokerUtil.lookupOpener('afcontrol:import', [:])
    }

    public List fetchList( Map m ) {
        m.formno = entity.formno; 
        m.respcentertype = respCenterType; 
        return service.getOpenList(m); 
    }
}   