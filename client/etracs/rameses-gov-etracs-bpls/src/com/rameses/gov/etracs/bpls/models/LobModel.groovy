package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

/************************************************************************
* This abstract class is extended by 
*************************************************************************/
public class LobModel extends CrudFormModel {

    def selectedItem;    
    
    public void beforeSave(def mode){
        if( !entity.state ) entity.state = 'DRAFT'; 
    }
    
    public void afterCreate(){ 
        listHandler.reload(); 
    } 
    
    boolean isCanRemoveAttribute() {
        if ( selectedItem==null ) return false; 
        return (mode == 'read');
    }
    boolean isCanAddAttribute() {
        return (mode == 'read'); 
    }
    
    def addAttribute() {
        return InvokerUtil.lookupOpener( "lobattribute:lookup", [
            onselect: { o->
                if(!entity.attributes) entity.attributes = [];
                if( entity.attributes.find{ it.objid == o.objid } )
                    throw new Exception("You have already added this attribute.")
                    
                def m = [_schemaname: 'lob_lobattribute'];
                m.lobid = entity.objid;
                m.lobattributeid = o.objid;
                persistenceService.create( m );    
                listHandler.reload();
            }
        ]);
    }

    void removeAttribute() {
        if(!selectedItem) return;
        if( MsgBox.confirm("You are about to delete this attribute. Continue?") ) {
            def m = [_schemaname: 'lob_lobattribute'];
            m.lobid = entity.objid;
            m.lobattributeid = selectedItem.lobattributeid;
            persistenceService.removeEntity( m );    
            listHandler.reload();
       }
    }

    def listHandler = [
        fetchList: {o->
            def m = [_schemaname: 'lob_lobattribute'];
            m.findBy = [lobid: entity.objid];
            def list = queryService.getList(m);
            return list;
        }
    ] as BasicListModel; 
}