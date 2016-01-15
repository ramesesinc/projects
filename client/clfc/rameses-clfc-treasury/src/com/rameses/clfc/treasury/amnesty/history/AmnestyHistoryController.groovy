package com.rameses.clfc.treasury.amnesty.history

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AmnestyHistoryController 
{
    @Service("AmnestyHistoryService")
    def service;
    
    def entity;
    
    void open() {
        entity = service.open(entity);
    }
    
    def getOpener() {
        def params = [
            entity          : entity,
            allowEdit       : false,
            forApprovalMode : 'read',
            approvalMode    : 'read',
            allowAmend      : false
        ];
        return Inv.lookupOpener(entity.amnestyoption.toLowerCase() + ":option", params);
    }
}

