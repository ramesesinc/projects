package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.client.*

public class RPTLedgerManualModel extends RPTLedgerModel
{
    @Service('LGUService')
    def lguSvc;
    
    @Service('QueryService')
    def qrySvc;
    
    @PropertyChangeListener
    def listener = [
        'entity.rputype' : {
            if (entity.rputype != 'land')
                entity.idleland = 0;
        },
        'entity.barangay' : {
            entity.fullpin = entity.barangay?.pin;
            binding?.refresh('entity.fullpin');
        }
    ]
    
    public void afterCreate(){
        entity.objid    = 'RLM' + new java.rmi.server.UID() 
        entity.state    = 'PENDING'
        entity.backtax  = false
        entity.taxable  = true
        entity.idleland  = 0
        entity.totalmv  = 0.0
        entity.totalav  = 0.0
        entity.totalareaha  = 0.0
        entity.partialbasicint = 0.0
        entity.partialbasic = 0.0
        entity.partialsef = 0.0
        entity.partialsefint = 0.0
        entity.effectivityqtr = 1;
        entity.lastyearpaid = 0;
        entity.lastqtrpaid= 0;
        entity.manualdiff = 0.0
        entity.subledger  = [:] 
        entity.administrator  = [:]
    }
    
    public void afterSave(){
        if (mode == 'create'){
            buildSections();
        }
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
    
    List getClassifications() {
        def p = [_schemaname:'propertyclassification']
        p.select = 'objid,code,name'
        p.where = "state='APPROVED'"
        p.orderBy = 'orderno'
        return qrySvc.getList(p)
    }
    
    List getQuarters(){
        return [1,2,3,4]
    }    
    
    List getRpuTypes(){
        return ['land','bldg','mach','planttree','misc']
    }
    
    List getTxntypes(){
        def p = [_schemaname:'faas_txntype']
        p.select = 'objid,name'
        p.where = ['1=1']
        return qrySvc.getList(p)
    }    
     
}

