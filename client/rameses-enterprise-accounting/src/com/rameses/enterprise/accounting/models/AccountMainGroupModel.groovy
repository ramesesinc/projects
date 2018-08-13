package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AccountMainGroupModel  {

    @Service("PersistenceService")
    def persistenceService;
    
    @Service("QueryService")
    def queryService;
    
    def selectedNode;
    def selectedItem;
    def entity;
    def acctListModel;
    
    void open() {
        
    }
    
    def treeNodeModel = [
        fetchNodes: { o->
            if( o.id == "root" ) {
                return [[ id: entity.objid, caption: entity.title, maingroup: true, expanded: true ]];
            }

            def m = [_schemaname: "account_group"];
            m.findBy = [maingroupid: entity.objid];
            m.orderBy = "code";
            
            if( o.properties.maingroup ) {
                m.where = ["groupid IS NULL"];
            }
            else {
                m.where = ["groupid = :groupid", [groupid: o.id ]];
            }
            return queryService.getList( m )?.collect {
                [id: it.objid, name: it.objid, caption: it.code + " " + it.title, item: it ];
            }
        },
        isRootVisible : { return false; },
        openFolder : { node->
            acctListModel.moveFirstPage();    
        }
    ] as TreeNodeModel;
    
    def addGroup() {
        def parent = null;
        if( !selectedNode?.properties.maingroup ) {
            parent = selectedNode.item.item;
        }
        Modal.show("account_group:create", [maingroup: entity, parent: parent] );
    }
    
    def editGroup() {
        if( !selectedNode?.item ) throw new Exception("Please select an item");
        Modal.show("account_group:open", [entity: selectedNode.item.item ] );
    }

    def removeGroup() {
        //Modal.show("account_group:open", [ maingroup: entity ] );
    }
    
    Map getAcctQuery() {
        def q = [:];
        q.maingroupid = entity.objid;
        if(selectedNode?.properties.maingroup) {
            q.groupid = "%";
        }
        else {
            q.groupid = selectedNode.id;
        }
        return q;
    }
    
    
    
}