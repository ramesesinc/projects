package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;


class FAASInitChangePinModel extends FAASInitTxnModel
{
    @Service('BarangayLookupService')
    def brgySvc;
    
    @Service('RPUService')
    def rpuSvc;
    
    @Service('LGUService')
    def lguSvc;
        
    @Service('Var')
    def var; 
        
    @Service('FAASRevisionService')
    def revisionSvc;
    
    @Service('RealPropertyService')
    def rpSvc;
    
    @Invoker
    def inv;
    
    def pinTypes = ['new', 'old']
    def rpuTypes;
    
    @PropertyChangeListener
    def listener = [
        'entity.*' : { buildPin(); }
    ]
    
                
    void afterInit(){
        entity.ry = var.get('current_ry');
        entity.rputype = 'land';
        entity.suffix = 0;
        rpuTypes = rpuSvc.getRpuTypes();
    }
    
    void buildPin(){
        RPTUtil.buildPin(entity, var);
        binding?.refresh('entity.pin');
    }
    
    
    void afterLookupFaas(){
        entity.rputype = entity.faas.rputype;
        entity.isection = RPTUtil.toInteger(entity.faas.section);
        entity.iparcel = RPTUtil.toInteger(entity.faas.parcel);
        entity.suffix = entity.faas.suffix;
        entity.barangay = barangays.find{it.objid == entity.faas.barangayid }
        entity.rp = rpSvc.open([objid:entity.faas.realpropertyid]);
        buildPin();
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
        
    def process(){
        entity.txntype = inv.properties.txntype;
        entity.lgu = getLgu()
        def faas = revisionSvc.createFaasRevision(entity);
        faas.lgu = entity.lgu 
        return InvokerUtil.lookupOpener('faas:open', [entity:faas]);            
    }
    
    
    def getLgus(){
        return lguSvc.getLgus();
    }

    def barangays;
    
    def getBarangays(){
        barangays = lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
        return barangays;
    }
    
    def getLgu(){
        def city = lguSvc.lookupCityById(entity.barangay.provcity.objid);
        if (city) return city;
        def muni = lguSvc.lookupMunicipalityById(entity.barangay.munidistrict.objid);
        if (muni) return muni;
        throw new Exception('Parent LGU for barangay ' + entity.barangay.name + ' does not exist.');
    }

}
