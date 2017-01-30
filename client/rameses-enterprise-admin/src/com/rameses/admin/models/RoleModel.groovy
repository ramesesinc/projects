package com.rameses.admin.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;
import com.rameses.osiris2.common.*;
         
public class RoleModel extends CRUDController {
        
    String serviceName = "RoleService";
    String entityName = "role";
    String title = "Role";
    boolean allowApprove = false

    def selectedItem 

    @Service("RoleService")
    def svc 
    
    @PropertyChangeListener
    def listener = [
        "entity.(domain|role)" : { o->
            entity.title = (((entity.domain)?entity.domain:" ") + " " + ((entity.role)?entity.role:"")).trim();
        }
    ];

    def listModel = [
           fetchList: {
                if(! entity.permissions) entity.permissions = []

                return  entity.permissions 
           },
           createItem: {
                return [ 
                    objid: "PER" + new java.rmi.server.UID(), 
                    newitem : false, 
                ]
           }, 
           onAddItem: { o-> 
               if( entity.permissions.find{ (it.object == o.object && it.permission == o.permission) } )  
                        throw new Exception("Permission already exist.    ")

                entity.permissions << o 
           }, 

           onRemoveItem: { o ->
                if(! MsgBox.confirm('Remove item? ')) return false;

                entity.permissions.remove(o);
                return true;
            }
    ] as EditorListModel
}
        