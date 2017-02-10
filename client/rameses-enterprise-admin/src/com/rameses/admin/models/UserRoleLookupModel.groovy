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
        
    public def getCustomFilter() {
        def f = [];
        def p = [:];
        if( query.orgid ) {
            f << "orgid=:orgid";
            p.orgid = query.orgid;
        }
        if( query.domain ) {
            f << "domain=:domain";
            p.domain = query.domain;
        }
        if(query.role) {
            f << "role=:role";
            p.role = query.role;
        }
        if(!f) return null;
        return [ f.join(" AND "), p ]; 
    }
}