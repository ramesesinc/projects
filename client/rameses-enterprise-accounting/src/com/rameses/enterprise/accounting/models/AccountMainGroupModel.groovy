package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AccountMainGroupModel extends CrudFormModel {

    def selectedNode;
    def selectedItem;
    def acctListModel;
    
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
                [id: it.objid, name: it.objid, caption: "[" + it.code + "] " + it.title, item: it ];
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
        treeNodeModel.reloadSelectedNode();
    }
    
    def editGroup() {
        if( !selectedNode?.item ) throw new Exception("Please select an item");
        Modal.show("account_group:open", [entity: selectedNode.item.item ] );
    }

    def removeGroup() {
        if(!selectedNode?.item?.item?.objid ) throw new Exception("Please select an account group");
        if(!MsgBox.confirm("Are you sure you want to remove this group?")) return;
        def m = [_schemaname: "account_group"];
        m.objid = selectedNode?.item?.item?.objid;
        persistenceService.removeEntity( m );
        treeNodeModel.reloadSelectedNode();
    }
    
    void refreshNode() {
        treeNodeModel.reloadSelectedNode();
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