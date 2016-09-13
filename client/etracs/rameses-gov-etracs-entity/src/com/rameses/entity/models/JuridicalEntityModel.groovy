package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JuridicalEntityModel extends CrudFormModel {
    
    @SubWindow 
    def window; 
    
    def create() {
        return Inv.lookupOpener("entityjuridical:create");
    }
    
    def changeName() {
        return "change-name";
    }
    
    String getTitle() {
        return (mode=='create'? 'New Juridical Entity': super.getTitle()); 
    }    
    
    void afterCreate() {
        if ( window ) { 
            window.update(); 
        }        
    }
    
    void afterSave() {
        if ( window ) { 
            window.update(); 
        }
    }    
}
