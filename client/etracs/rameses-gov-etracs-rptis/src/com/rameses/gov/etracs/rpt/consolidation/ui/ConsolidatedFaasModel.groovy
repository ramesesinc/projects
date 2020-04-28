package com.rameses.gov.etracs.rpt.consolidation.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class ConsolidatedFaasModel
{
    @Binding
    def binding;

    @Service('FAASService')
    def faasSvc;

    @Service('LGUService')
    def lguSvc;
    
    @Service('Var')
    def var;
    
    def svc;
    def entity;
    def faas;
    def info;

    def init(){
    }


    def onCreateConsolidatedFaas  = {
        binding?.refresh('opener');
    }

    def getOpener(){
        if (!entity.newfaasid) {
            def params = [:]
            params.svc = svc;
            params.entity = entity;
            params.oncreate = onCreateConsolidatedFaas;
            return InvokerUtil.lookupOpener('consolidation:newconsolidatedfaas', params);
        } else {
            faas = [objid:entity.newfaasid];    
            def params = [entity:faas, svc:faasSvc, taskstate:entity.taskstate, assignee:entity.assignee]
            return InvokerUtil.lookupOpener('faasdata:open', params);
        }
    }



    /*========================================
    * PIN SUPPORT   
    *========================================*/
    
    def pinTypes = ['new', 'old']
    
    @PropertyChangeListener
    def listener = [
        'entity.rputype' : {
            entity.barangay = null;
            entity.rp = null;
            entity.suffix = null;
            if (entity.rputype == 'land')
                entity.suffix = 0
            entity.isection = null;
            entity.iparcel = null;
            
        },
        
        'entity.pintype' :{
            entity.isection = null;
            entity.iparcel = null;
        },
        
        'entity.*' : { buildPin(); }
    ] 
        
    void buildPin(){
        RPTUtil.buildPin(entity, var);
        binding?.refresh('entity.pin');
    }
    

    def getLgus(){
        return lguSvc.getLgus();
    }

    def getBarangays(){
        if (! entity.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }        
    
    

}

