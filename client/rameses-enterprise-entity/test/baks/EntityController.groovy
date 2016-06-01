package com.rameses.entity.controller;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
        
public class EntityController extends CRUDController { 
        
    String serviceName = 'EntityService'; 
    boolean allowDelete = false;

    def selectedContact;
    def contactTypes = [
        [key: 'MOBILE', value:'Mobile Number'], 
        [key: 'HOME', value:'Home Telephone Number'], 
        [key: 'WORK', value:'Office Telephone Number'], 
        [key: 'EMAIL', value:'Email'] 
    ]; 
    def contactListHandler = [
        fetchList: {o-> 
            if (!entity.contacts) entity.contacts = [];

            return entity.contacts;
        },

        onAddItem: {item-> 
            item.objid = 'CONT'+new UID();
            item.entityid = entity.objid; 
            entity.contacts.add(item); 
        }, 

        onRemoveItem: {item-> 
            if (!MsgBox.confirm('Are you sure you want to remove this item?')) return false;

            entity.contacts.remove(item); 
            return true;
        }                
    ] as EditorListModel;    
}
