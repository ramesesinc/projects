package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CollectionGroupModel extends CRUDController {

    @Service("CollectionTypeService")
    def svc;

    String serviceName = "CollectionGroupService";
    String entityName = "collectiongroup";
    String title = "Collection Group";
    
    def valueTypes = [ "ANY", "FIXED", "FIXEDUNIT" ];
    def deletedItems = [];
    def selectedItem;

    def listModel = [
        isMultiSelect: {
            return true; 
        }, 
        fetchList: {         
            if (entity.revenueitems == null) {
                entity.revenueitems = []; 
            }
            return entity.revenueitems;
        },
        createItem: {
            return [valuetype:'ANY', defaultvalue:0.0];
        },
        addItem: {
            entity.revenueitems << it; 
        }  
    ] as EditorListModel;

    void afterOpen( o ) {
        listModel.reload();
    }
    
    void afterUpdate( o ) {
        listModel.reload();
    }
    
    def getFormTypes() {
        return svc.getFormTypes()*.objid;
    }
    
    def getLookupAccount() {
        def params = [:]; 
        params.onselect = {o-> 
            if ( selectedItem==null ) return;
            
            selectedItem.revenueitemid = o.objid;
            selectedItem.code = o.code;                    
            selectedItem.title = o.title; 
            selectedItem.orderno = 0; 
        } 
        return Inv.lookupOpener('revenueitem:lookup', params);
    } 
        
    void removeAccounts(){ 
        def values = listModel.getSelectedValue(); 
        values = values.findAll{( it )} 
        if( values ) {
            values.each{ o-> 
                entity.revenueitems.remove( o ); 
            } 
            listModel.reload(); 
        } else {
            MsgBox.alert("Please select at least one item"); 
        } 
    } 
    void selectAll() {
        listModel.selectAll();
    }
    void deselectAll() {
        listModel.deselectAll();
    }
    boolean isAllowSelections() { 
        if ( mode == 'read' ) return false; 
        
        return ( entity.revenueitems ? true: false );
    }
}      
