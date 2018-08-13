package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AccountGroupModel extends CrudFormModel {

    def maingroup;
    def parent;
    
    void afterCreate() {
        entity.maingroup = maingroup;
        entity.group = null;
        if( parent ) {
            entity.group = parent;
        }
    }    
 
}