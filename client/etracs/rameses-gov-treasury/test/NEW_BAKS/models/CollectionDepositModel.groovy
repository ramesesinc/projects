package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class CollectionDepositModel extends CrudFormModel { 

    @Service("CollectionDepositService")
    def service;
    
    @Caller 
    def caller; 

    String title = "Deposit";
    String schemaName = "deposit";

    def handler;

    @FormTitle
    public String getTitle() { 
        if( entity?.state.toString().equalsIgnoreCase('DRAFT') ) {
            return "New Deposit"; 
        } else { 
            return "Deposit " + entity.controlno;
        } 
    }

    @FormId
    public String getFormId() {
        return entity.objid;
    }
    
    public void afterCreate() { 
        entity = service.init();  
    }     

    
    void post() {
    }
    
    def delete() {
        if (MsgBox.confirm("You are about to delete this transaction. Proceed?")) {
            persistenceSvc.removeEntity([ _schemaname:schemaName, objid: entity.objid ]); 
            try { 
                return "_close"; 
            } finally {
                try {
                    if (caller) caller?.reload(); 
                } catch(Throwable t){;}
            }
        }
        return null; 
    }
} 