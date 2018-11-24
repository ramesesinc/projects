package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class AccountModel extends CrudFormModel {
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    def meterStates;
    
    
    void afterCreate() {
        entity.address = [:];
        entity.attributes = [];
        entity.units = 1;
        entity.meter = [:];
        entity.stuboutnode = [:];
    }

    def edit() {
        return showDropdownMenu("edit");
    }
    
    //***************************************************************************
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