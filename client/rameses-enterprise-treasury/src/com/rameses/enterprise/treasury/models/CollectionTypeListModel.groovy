package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;
      
public class CollectionTypeListModel extends CrudListModel {
            
    @Service("OrgService")
    def orgSvc;

    @Service("OrgLookupService")
    def orgLookupSvc;

    /*
    String serviceName = "CollectionTypeService";
    String entityName  = "collectiontype"; 
    */
   
    def orgType;
    def orgTypes;
    def org;

    @PropertyChangeListener 
    def changes = [
        'orgType': { reload() }, 
        'org'    : { reload() }
    ]; 

    def getOrgList() {
        if(!orgType?.name) return [];
        if( orgType?.handler == 'root' ) return [];
        return orgLookupSvc.getList([orgclass:orgType.name]); 
    }

    void afterInit() {
         orgTypes = orgSvc.getOrgClasses();
    }

    public def getCustomFilter() {
        def conds = [];
        def m = [:];
        if( orgType?.name ) {
            if( orgType.handler == 'root') {
                conds << "org.orgclass IS NULL"
            }
            else {
                conds << "org.orgclass=:orgclass"
                m.orgclass = orgType.name;
            }
        }
        if( org?.objid ) {
            conds << "org.objid=:orgid"
            m.orgid = org.objid;
        }
        return [ conds.join(" AND "), m ];
    }
    
}