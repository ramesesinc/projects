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

public class UserRoleLookupModel extends CrudLookupModel {
        
    def orgid;
    def domain;
    def role;

    public def getCustomFilter() {
        def f = [];
        def p = [:];
        if( orgid ) {
            f << "orgid=:orgid";
            p.orgid = orgid;
        }
        if( domain ) {
            f << "domain=:domain";
            p.domain = domain;
        }
        if(role) {
            f << "role=:role";
            p.role = role;
        }
        if(!f) return null;
        return [ f.join(" AND "), p ]; 
    }
}