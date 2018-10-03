package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class CollectionGroupModel extends CrudFormModel {

    def selectedItem;
    def listModel;
    def orgListHandler;

    void addOrg() {
        def h = { arr-> 
            try { 
                arr.each {o-> 
                    def item = [ _schemaname: "collectiongroup_org" ];
                    item.objid = entity.objid + ":" + o.objid;
                    item.collectiongroupid = entity.objid;
                    item.org = o;
                    item.org.type = o.orgclass;
                    persistenceService.save( item );
                }
            } finally {  
                orgListHandler.reload(); 
            }  
        } 
        Modal.show( "org:lookup", [onselect: h, multiSelect: true] );
    }
}      
