package com.rameses.enterprise.financial.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AccountIncomeTargetModel extends com.rameses.seti2.models.CrudFormModel {

    void afterCreate() {
        entity.maingroup = caller.selectedNode;
        
        try { 
            entity.year = caller.query.year; 
        } catch(Throwable t) {;} 
    }

    void afterOpen() {
    }

    def getCallerSelectedNode() {
        try {
            return caller.selectedNode;
        } catch(Throwable t) {
            return null; 
        }
    }
    
    def getMainGroup() {
        if ( !entity.maingroup?.objid ) {
            entity.maingroup = getCallerSelectedNode();
        } 
        return entity.maingroup?.objid; 
    }

    def getLookupHandler() { 
        def param = [
            'query.year'        : entity.year, 
            'query.maingroupid' : getMainGroup(), 
            'query._actionid'   : 'account_incometarget:lookup-account' 
        ];
        return Inv.lookupOpener("account:lookup", param);
    }
}