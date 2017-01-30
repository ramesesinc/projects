package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class AddAuxiliaryPermitModel extends CrudFormModel {
    
    def app;
    def handler;
    
    boolean showConfirm = false;
    
    public def getLookupUser() {
        def r = entity.type?.toUpperCase();
        def p = [query: [domain:'OBO', role: r] ];
        return Inv.lookupOpener( "sys_user_role:lookup", p ); 
    }
    
    public def doOk() {
        entity.app = app;
        if(save()!=null) {
            handler(entity);
            return "_close"; 
        }
    }
    
    public def doCancel() {
        return "_close"; 
    }
    
}