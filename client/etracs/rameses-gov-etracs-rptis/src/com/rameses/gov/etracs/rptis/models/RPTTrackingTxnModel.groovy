package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class RPTTrackingTxnModel 
{
    @Service('PersistenceService')
    def persistence;

    @Invoker 
    def invoker;
    
    def entity;
    
    def init() {
        def tracking = loadTracking();
        if (tracking) {
            return Inv.lookupOpener('rpttracking:open', [entity: tracking]);
        } else {
            return Inv.lookupOpener('rpttracking:create', [refentity: entity, filetype: invoker.properties.filetype]);
        }
    }
    
    def loadTracking() {
        def param = [_schemaname: 'rpttracking'];
        param.objid = entity.objid;
        return persistence.read(param);
    }
}


