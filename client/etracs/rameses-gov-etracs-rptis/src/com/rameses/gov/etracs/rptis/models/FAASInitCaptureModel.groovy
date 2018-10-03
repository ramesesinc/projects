package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;

class FAASInitCaptureModel
{
    @Binding
    def binding;
    
    @Service('FAASService')
    def svc;
    
    @Service('RPUService')
    def rpuSvc;
    
    @Service('LGUService')
    def lguSvc;
    
    @Service('Var')
    def var;
    
    
    String title = 'Data Capture FAAS Initial Information';
    
    def entity = [:]
    
    def pinTypes = ['new', 'old']
    def rpuTypes;
    def txnTypes;
    
    
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
            binding.refresh('entity.(isection|iparcel)');
        },
        
        'entity.*' : { buildPin(); }
    ]
    
                
    void init(){
        entity.ry = var.get('current_ry');
        entity.rputype = 'land';
        entity.suffix = 0;
        entity.datacapture = true;
        rpuTypes = rpuSvc.getRpuTypes();
        txnTypes = svc.getTxnTypes();
        
        entity.pintype = var.get('pin_type');
        if (!entity.pintype)
            entity.pintype = 'new';
    }
    
    
    def process(){
        def faas = svc.initCaptureAndCreate(entity);
        faas.lgu = entity.lgu;
        faas.prevpin = entity.fullpin;
        def filetype = 'faas:capture:create'
        
        entity.rputype = null;
        entity.isection = null;
        entity.iparcel = null;
        entity.subsuffix = null;
        entity.claimno = null;
        
        return InvokerUtil.lookupOpener(filetype, [entity:faas])
    }
    
    
    def getLookupBarangay(){
        return InvokerUtil.lookupOpener('barangay:lookup', [
            onselect: { 
                entity.barangay = lguSvc.lookupBarangayById(it.objid);
                entity.lgu = entity.barangay.lgu;
                entity.rp = null;
                buildPin();
            },
            onempty : { entity.barangay = null },
        ])
    }
    
    
    def getLookupRealProperty(){
        return InvokerUtil.lookupOpener('realproperty:lookup', [
            onselect: { 
                entity.rp = it; 
                entity.barangay = lguSvc.lookupBarangayById(it.barangayid);
                entity.lgu = entity.barangay.lgu 
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
    

    def getLgus(){
        return lguSvc.getLgus();
    }

    def getBarangays(){
        if (! entity.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }

}
