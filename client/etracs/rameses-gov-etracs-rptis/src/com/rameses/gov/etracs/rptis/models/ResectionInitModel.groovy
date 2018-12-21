package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class ResectionInitModel 
{
    @Binding
    def binding;
    
    @Service('LGUService')
    def lguSvc;
    
    @Service('ResectionService')
    def svc; 
    
    def entity;
    def pintypes = ['new', 'old'];
    
    String title = 'Resection (Initial)'

    void init() {
        entity = [:]
        entity.objid = 'R' + new java.rmi.server.UID();
        entity.state = 'DRAFT';
    }
    
    
    def process(){
        def newentity = svc.create(entity)
        init();
        return InvokerUtil.lookupOpener('resection:open', [entity:newentity]);
    }
    
    def getLgus(){
        def orgclass = OsirisContext.env.ORGCLASS
        def orgid = OsirisContext.env.ORGID

        if ('PROVINCE'.equalsIgnoreCase(orgclass)) {
            return lguSvc.lookupMunicipalities([:])
        }
        else if ('MUNICIPALITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupMunicipalityById(orgid)]
        }
        else if ('CITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupCityById(orgid)]
        }
        return []
    }
    
    def getBarangays(){
        if (! entity.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }
        
    
}