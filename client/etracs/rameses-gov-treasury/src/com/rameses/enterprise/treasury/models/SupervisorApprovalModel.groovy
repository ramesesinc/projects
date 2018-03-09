package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class SupervisorApprovalModel  {

    @Script("User")
    def user;
    
    def username;
    def pwd;
    
    def handler;
    def entity = [:]
    
    void init() {
        username = "test";
    }
    
    def doOk() {
        entity.password = encodePwd( pwd, username );
        handler( entity );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
}    