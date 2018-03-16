package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class AccountModel extends CrudFormModel {
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    
    void afterCreate() {
        entity.address = [:];
        entity.attributes = [];
    }

    
    def assignNode() {
        def h = { o ->
            entity.stuboutnode = o;
            def m = [_schemaname:'waterworks_account'];
            m.findBy = [objid: entity.objid];
            m.stuboutnodeid = o.objid;
            persistenceService.update( m );
        }
        Modal.show("vw_waterworks_stuboutnode_unassigned:lookup", [onselect: h] );  
    }
    
    void attachMeter() { 
        def params = [:];
        params.handler = { o-> 
            MsgBox.alert( "entity is " + o );
            /*
            def m = [_schemaname:'waterworks_account'];
            m.findBy = [objid: entity.objid];
            m.meterid = o.objid;
            persistenceService.update( m );
            entity.meter = o;
            */
            binding.refresh();
        }
        Modal.show('waterworks_assign_meter', params);
    }
    
    void detachMeter() { 
        if(!MsgBox.confirm('This action will remove the meter from this account. Proceed?')) return;
        def m = [_schemaname:'waterworks_account'];
        m.findBy = [objid: entity.objid];
        m.meterid = "{NULL}";
        persistenceService.update( m );
        entity.meter = null;
        binding.refresh();
    }
    
    def selectedAttribute;
    def attributeList = [
        fetchList: { o->
            def m = [_schemaname:'waterworks_account_attribute'];
            m.findBy = [parentid: entity.objid];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    void addAttribute() {
        def p = [:]
        p.onselect = { o->
            def m = [_schemaname:'waterworks_account_attribute'];
            m.parent = entity;
            m.attribute = o;
            persistenceService.create( m );
            attributeList.reload();
        }
        Modal.show( "waterworks_attribute:lookup", p );
    }
    
    void removeAttribute() {
        if(!selectedAttribute) throw new Exception("Please select an attribute");
        def m = [_schemaname:'waterworks_account_attribute'];
        m.findBy = [objid: selectedAttribute.objid];
        persistenceService.removeEntity( m );
        attributeList.reload();
    }
    
}