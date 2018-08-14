package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;


class FAASInitNewDiscoveyModel extends FAASInitTxnModel
{
    @Service('BarangayLookupService')
    def brgySvc;
    
    @Service('RealPropertyService')
    def rpSvc;
    
    @Service('RPUService')
    def rpuSvc;
    
    @Service('LGUService')
    def lguSvc;
        
    @Service('Var')
    def var; 
        
    @Service('FAASService')
    def faasSvc;
        
    
    def pinTypes = ['new', 'old']
    def rpuTypes;
    
    @PropertyChangeListener
    def listener = [
        'entity.rputype' : {
            entity.barangay = null;
            entity.suffix = null;
            if (entity.rputype == 'land')
                entity.suffix = 0
            entity.isection = null;
            entity.iparcel = null;
        },
        
        'entity.pintype' :{
            entity.isection = null;
            entity.iparcel = null;
            binding.refresh('entity.(isection|iparcel)');
        },
        
        'entity.*' : { buildPin(); }
    ]
    
                
    void afterInit(){
        entity.ry = var.get('current_ry');
        entity.rputype = 'land';
        entity.suffix = 0;
        rpuTypes = rpuSvc.getRpuTypes();
        entity.attributes = faasSvc.getTxnTypeAttributes([objid:'ND'])
    }
    
    def getLookupBarangay(){
        return InvokerUtil.lookupOpener('barangay:lookup', [
            onselect: { 
                entity.barangay = it;
                entity.rp = null;
                buildPin();
            },
            onempty : { entity.barangay = null },
        ])
    }
    
    
    def getLookupRealProperty(){
        return InvokerUtil.lookupOpener('realproperty:lookup', [
            onselect: { 
                if (it.state == 'CANCELLED')
                    throw new Exception('Cancelled Property is not allowed.');
                entity.rp = it; 
                entity.barangay = brgySvc.getById(entity.rp.barangayid);
                entity.barangayid = entity.barangay.objid;
                entity.isection = RPTUtil.toInteger(entity.rp.section);
                entity.iparcel = RPTUtil.toInteger(entity.rp.parcel);
                buildPin();
            },
            onempty : { entity.rp = null; },
        ])
    }
    
    void buildPin(){
        if (entity.rputype != 'land'){
            validateSuffix();
        }
        RPTUtil.buildPin(entity, var);
        binding?.refresh('entity.pin');
    }
    
    void validateSuffix() {
        if (entity.isection && entity.iparcel && entity.suffix){
            try {
                rpuSvc.validateSuffix(entity.rputype, entity.suffix);
            }
            catch(e){
                entity.suffix = null;
                binding.focus('entity.suffix');
                MsgBox.alert(e.message);
            }
        }
    }    
    
    def process(){
        entity.txntype = [objid:'ND'];
        def faas = svc.initNewDiscovery(entity);
        faas.lgu = entity.lgu 
        return InvokerUtil.lookupOpener('faas:open', [entity:faas]);
    }
    
    
    def getLgus(){
        return lguSvc.getLgus();
    }

    def getBarangays(){
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }
        
    def listHandler = [
        fetchList : { return entity.attributes }
    ] as EditorListModel;
             
    
}
