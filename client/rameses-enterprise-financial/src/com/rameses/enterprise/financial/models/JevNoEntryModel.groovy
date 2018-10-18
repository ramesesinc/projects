package com.rameses.enterprise.financial.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class JevNoEntryModel {

    @Service("JevService")
    def jevSvc;
    
    def handler;
    def entity;
    
    def doOk() {
        if(!MsgBox.confirm("You are about to assign a jev no to this entry. Proceed?")) return;
        
        def m = jevSvc.updateJevNo( entity );
        handler( m );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
}