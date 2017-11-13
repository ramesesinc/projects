package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

class SelectAFModel extends CrudLookupModel {

    @Service("AFControlService")
    def service;
    
    @Script( "User" )
    def user;

    def entity;

    String title = "Select Stub to use";

    def getCustomFilter() {
        return [ 
              "afid = :formno AND assignee.objid = :uid AND txnmode=:mode AND currentseries <= endseries AND active=0", 
            [formno: entity.formno, uid: user.userid, mode: entity.txnmode ]  
        ];
    }
    
    def doOk() {
        def obj = listHandler.getSelectedValue();
        service.activateSelectedControl( [objid: obj.objid ] );
        return "_close";
        //return doOk();
    }
    
}