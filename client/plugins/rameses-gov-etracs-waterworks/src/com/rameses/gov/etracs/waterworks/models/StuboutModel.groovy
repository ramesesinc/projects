package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class StuboutModel extends CrudFormModel {
    
    
    void afterCreate() {
        if( !caller.selectedZone ) throw new Exception("Please select a zone");
        entity.zone = caller.selectedZone;
        entity.zoneid = entity.zone.objid;
    }
    
    void afterSave() {
        caller.stuboutListHandler.reload();
    }
    /*
    @Service('WaterworksStuboutService')
    def svc;
    
    def selectedItem;
    
    
    def getQuery() {
        return [stuboutid: entity.objid];
    }

    void addNodes() { 
        def p = [:]
        p.fields = [
            [name:'nodecount', type:'integer', caption:'No. of Nodes'],
            [name:'startnode', type:'integer', caption:'Start No'],
            [name:'interval', type:'integer', caption:'Interval'],
        ];
        p.data = [nodecount:1, startnode: 1, interval:1];
        p.handler = { o->
            o.stuboutid = entity.objid;
            svc.addNodes( o );
        }
        Modal.show("dynamic:form", p, [title:'Enter Nodes']);
    }    
    
    void editIndexNo() {
        if(!selectedItem) throw new Exception("Please select an item");
        def p = [:]
        p.fields = [
            [name:'value', type:'integer', caption:'Enter Index No']
        ];
        p.data = [value: selectedItem.indexno];
        p.handler = { o->
            svc.updateIndexNo( [objid:selectedItem.objid, indexno: o.value ] );
        }
        Modal.show("dynamic:form", p, [title:'Edit Index No']);
    }
    
    void removeNode() {
        if(!selectedItem) throw new Exception("Please select an item");
        if(!MsgBox.confirm('Are you sure you want to remove this entry?')) return;
        def m = [_schemaname: 'waterworks_stubout_node'];
        m.objid = selectedItem.objid;
        persistenceSvc.removeEntity( m );
    }
    */
    
}