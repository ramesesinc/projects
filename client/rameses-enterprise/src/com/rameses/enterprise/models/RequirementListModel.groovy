package com.rameses.enterprise.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;

public class RequirementListModel {
    
    @Binding
    def binding;
    
    @Service("RequirementListService")
    def reqListSvc;

    @Caller 
    def caller;
    
    def refid;
    def schemaname;
    def selectedItem;
    def items;

    def openerMap = [:]    
    
    def getOpener() {
        if( !selectedItem) return null;
        def objid = selectedItem.objid;
        if( !openerMap.containsKey(objid) ) {
            def n = "sys_requirement_type:"+selectedItem.handler;
            openerMap.put(objid, Inv.lookupOpener( n, [schemaname:schemaname, entry: selectedItem] ));
        }
        return openerMap.get(objid);
    }

    void init() {
        //MsgBox.alert( "appno " + caller.entity?.appno );
        if( !refid && !schemaname ) 
            throw new Exception("error in app requirement list! refid and schemaname is required");
        items = reqListSvc.getList( _schemaname: schemaname, refid: refid ); 
    }

    def listHandler = [
        fetchList: { o->
            return items;
        }
    ] as BasicListModel;
    
    void refresh() {
        listHandler.refresh();
    }
    
    
    
}