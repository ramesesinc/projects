package com.rameses.enterprise.financial.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AccountMainGroupModel extends CrudFormModel {
    
    @Service("AccountIndexerService")
    def indexerSvc;
    
    void addNode(def type, def parent ) {
        Modal.show("account:create", [type:type, group:parent?.item?.item, maingroup: entity ]);
    }
    
    void editNode(def node ) {
        if(node==null) throw new Exception("Please select an item");
        Modal.show("account:open", [entity:node.item.item]);
    }

    def formatTitle( def o ) {
        return "[" + o.code + "] " + o.title; //+ " :" + o.level + ";;" + o.leftindex + "," + o.rightindex;
    }
    
    boolean removeNode(def node ) {
        if(node==null) throw new Exception("Please select an item");
        if(!MsgBox.confirm("You are about to remove this node. Continue?")) return false;
        def m = node.item.item;
        m._schemaname = "account";
        try {
            persistenceService.removeEntity( m );
            return true;
        }catch(e) {
            return false;
        }
    }

    /************************************************************************
    * ROOT
    *************************************************************************/
    def selectedRoot;
    def rootListModel = [
        isRootVisible: {
            return  false;
        },
        fetchNodes : { o->
            if( o.id!='root' ) return null;
            def m = [_schemaname: "account"];
            m.where = ["maingroupid = :id AND groupid IS NULL AND type='root' ", [id:entity.objid]];
            m.orderBy = "code";
            return queryService.getList(m).collect {
                [id: it.objid, name: it.objid, caption: formatTitle( it ) , item: it ];
            }
        },
        openFolder : { node->
            selectedRoot = node;
            groupListModel.reloadTree();
            itemListModel.reloadTree();
            detailListModel.reloadTree();
        }
    ] as TreeNodeModel;
    
    void addRoot() {
        addNode("root", null);
        rootListModel.reloadTree();
    }
    
    void editRoot() {
        editNode(selectedRoot);
        rootListModel.reloadTree();
    }
    
    void removeRoot() {
        if( removeNode(selectedRoot)) {
            rootListModel.reloadTree();
            groupListModel.reloadTree();
            itemListModel.reloadTree();
            detailListModel.reloadTree();
        }
    }
    
    /************************************************************************
    * GROUP
    *************************************************************************/
    def selectedGroup;
    def groupListModel = [
        isRootVisible: {
            return  false;
        },
        fetchNodes : { o->
            if( !selectedRoot ) return [];
            def parentid = null;
            if(o.id == "root" ) {
                parentid = selectedRoot.id;
            }
            else {
                parentid = o.id;
            }
            def m = [_schemaname: "account"];
            m.where = ["maingroupid = :mainid AND groupid = :groupid AND type='group' ", [mainid:entity.objid, groupid:parentid]];
            m.orderBy = "code";
            return queryService.getList(m).collect {
                [id: it.objid, name: it.objid, caption: formatTitle(it), item: it ];
            }
        },
        openFolder : { node->
            selectedGroup = node;
            itemListModel.reloadTree();
            detailListModel.reloadTree();
        }
    ] as TreeNodeModel;
    
    void addGroup() {
        addNode("group", selectedRoot);
        groupListModel.reloadTree();
    }
    
    void addSubGroup() {
        if( selectedGroup == null ) throw new Exception("Please select a group");
        addNode("group", selectedGroup);
        groupListModel.reloadSelectedNode();
    }
    
    void editGroup() {
        editNode(selectedGroup);
        groupListModel.reloadTree();
    }
    
    void removeGroup() {
        if(removeNode(selectedGroup)) {
            groupListModel.reloadTree();
            itemListModel.reloadTree();
            detailListModel.reloadTree();
        }
    }

     /************************************************************************
    * ITEMS
    *************************************************************************/
    def selectedItem;
    def itemListModel = [
        isRootVisible: {
            return  false;
        },
        fetchNodes : { o->
            if( !selectedGroup ) return [];
            if(o.id != "root") return null;
            def parentid = selectedGroup.id;
            def m = [_schemaname: "account"];
            m.where = ["maingroupid = :mainid AND groupid = :groupid AND type='item' ", [mainid:entity.objid, groupid:parentid]];
            m.orderBy = "code";
            return queryService.getList(m).collect {
                [id: it.objid, name: it.objid, caption: formatTitle( it ), item: it, folder: false ];
            }
        },
        openFolder : { node->
            selectedItem = node;
            detailListModel.reloadTree();
        }
    ] as TreeNodeModel;
    
    void addItem() {
        addNode("item", selectedGroup);
        itemListModel.reloadTree();
    }
    
    void editItem() {
        editNode(selectedItem);
        itemListModel.reloadTree();
    }
    
    void removeItem() {
        if(removeNode(selectedItem)) {
            itemListModel.reloadTree();
            detailListModel.reloadTree();
        }
    }
    
    /************************************************************************
    * DETAIL
    *************************************************************************/
    def selectedDetail;
    def detailListModel = [
        isRootVisible: {
            return  false;
        },
        fetchNodes : { o->
            if( !selectedItem ) return [];
            if(o.id!="root") return null;
            def parentid = null;
            def m = [_schemaname: "account"];
            m.where = ["maingroupid = :mainid AND groupid = :groupid AND type='detail' ", [mainid:entity.objid, groupid:parentid]];
            m.orderBy = "code";
            return queryService.getList(m).collect {
                [id: it.objid, name: it.objid, caption: formatTitle(it), item: it ];
            }
        },
        openFolder : { node->
            selectedDetail = node;
        }
    ] as TreeNodeModel;
    
    void addDetail() {
        addNode("detail", selectedItem);
        detailListModel.reloadTree();
    }
    
    void editDetail() {
        editNode(selectedDetail);
        detailListModel.reloadTree();
    }
    
    void removeDetail() {
        if(removeNode(selectedDetail)) {
            detailListModel.reloadTree();
        }
    }

    void reindex() {
        indexerSvc.reindex( [maingroupid:entity.objid ]);
        MsgBox.alert( "reindex finished");
        rootListModel.reloadTree();
    }
    
}