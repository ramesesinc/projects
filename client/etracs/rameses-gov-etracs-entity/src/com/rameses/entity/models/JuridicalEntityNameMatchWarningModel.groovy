package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class JuridicalEntityNameMatchWarningModel  {
    
    def selectedItem;
    def list;
    def handler;
    
    def doCancel() { 
        return "_close"; 
    } 
    
    def doProceed() {
        if ( list.find{it.match == 100 }) { 
            boolean approved = false; 
            try {
                if ( Inv.lookup("entityjuridical:matchname:approve")) {
                    approved = true; 
                }
            } catch(Throwable t) {;}
            
            if ( !approved ) 
                throw new Exception("Adding is not permitted. There is an exact match of the name. Please verify"); 
        }

        handler([ ignore_warning: true ]); 
        return "_close"; 
    }
}
