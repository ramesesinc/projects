package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class LobListModel extends CrudListModel {

    boolean autoResize = false;
    
    public void initColumn( def c ) { 
        if ( c.name == 'classification.objid' ) {
            c.width = 150;
        } 
        else if ( c.name == 'psic.objid' ) {
            c.width = 120;
        } 
        else {
            c.width = 300;
        }
    }     
    
    def getLookupAttribute() {
        return InvokerUtil.lookupOpener( "lobattribute:lookup", [
            onselect: { o->
                query.attribute = o;
                binding.refresh('lobattribute'); 
                reload();
            }
        ])
    }
    
    void resetSearch() {
        query.attribute = null; 
        binding.refresh('lobattribute'); 
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