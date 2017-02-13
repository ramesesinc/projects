package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class LobListModel extends CrudListModel {

    def getLookupAttribute() {
        return InvokerUtil.lookupOpener( "lobattribute:lookup", [
            onselect: { o->
                query.attribute = o;
                reload();
            }
        ])
    }
    
    void resetSearch() {
        query.attribute = null;
        reload();
    }
    
    /*
    private String PERM_KEY = 'lob';  
              
    public Map getCreatePermission() { 
        return [role:'MASTER', permission: PERM_KEY+'.create']; 
    } 
    public Map getReadPermission() { 
        return [role:'MASTER', permission: PERM_KEY+'.read']; 
    }   
    */
}