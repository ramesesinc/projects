package com.rameses.gov.etracs.rpt.util;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.gov.etracs.rptis.util.*;

public class ModifyPinModel 
{
    @Binding
    def binding
    
    @Caller
    def caller
    
    @Service('RPTUtilityService') 
    def svc 
    
    @Service('LGUService')
    def lguSvc

    @Service('Var')
    def varSvc 

    @Service('FAASLookupService')
    def faasLookupSvc 
    
    @PropertyChangeListener
    def listener = [
        '.*' : { 
            if (faas){
                if (faas.rputype == 'land'){
                    RPTUtil.buildPin(entity, varSvc);
                }
                else{
                    entity.fullpin = landfaas.fullpin + '-' + (entity.suffix ? entity.suffix : '0000');
                }
                entity.newpin = entity.fullpin;
                binding?.refresh('entity.newpin');
            }
        } 
    ]
    
    def entity
    def faas
    def landfaas
    def lgutype
    
    def onUpdate = {};
    def externalCall = false;
    
    void init(){
        entity = [
            lgutype : OsirisContext.env.ORGCLASS.toLowerCase(),
            pintype : 'new',
            section : 0,
            parcel  : 0,
            suffix  : 0,
            useoldpin : false,
        ]
    }
    
    void create() {
        def faases = faasLookupSvc.lookupFaas([objid: entity.objid]);
        if (!faases) throw new Exception('FAAS does not exist or has already been deleted.');
        faas  = faases[0];
        init();
        updateFaas(faas);
        externalCall = true;
    }
    
    def updatePin() {
        if( MsgBox.confirm('Update existing PIN with new information?') ) {
            entity.faasid   = faas.objid
            entity.landfaas = landfaas
            entity.rpuid    = (faas.rpuid ? faas.rpuid : faas.rpu.objid)
            entity.rputype  = (faas.rputype ? faas.rputype : faas.rpu.rputype)
            entity.state    = faas.state 
            entity.oldpin   = (faas.fullpin ? faas.fullpin : faas.rpu.fullpin)
            entity.ry       = faas.ry 
            if (entity.barangay){
                entity.barangay.provcity = entity.provcity 
                entity.barangay.munidistrict = entity.munidistrict
            }
            
            def rp = svc.updatePin(entity)
            if (externalCall && caller ){
                def callerentity = caller.entity;
                if (callerentity) {
                    callerentity.fullpin = entity.newpin
                    callerentity.rpu?.fullpin = entity.newpin
                    if ( rp ) {
                        callerentity.rp.pintype = rp.pintype 
                        callerentity.rp.pin = rp.pin
                        callerentity.rp.section = rp.section
                        callerentity.rp.parcel = rp.parcel
                    }
                }
            }
            
            //invoke callback
            onUpdate();
            
            if (externalCall) {
                caller?.binding?.refresh();
                return '_close';
            } else {
                clearInfo()
                binding?.focus('faas')
            }
        }
    }
    
    void clearInfo() {
        entity.clear();
        entity.pintype    = 'new';
        entity.suffix     = 0;
        entity.useoldpin  = false;
        faas = null;
        landfaas = null;
    }
    
    
    void updateFaas(f) {
        this.faas = f; 
        this.landfaas = null;
        entity.rputype = f.rputype;
        entity.pintype = (f.pintype ? f.pintype : 'new');

        def tokens = f.fullpin.tokenize('-');
        
        if (f.rputype == 'land'){
            entity.suffix = 0
            entity.munidistrict = getMuniDistrictList().find{it.indexno == tokens[1]}
            entity.barangay = getBarangayList().find{it.objid == f.barangayid}
        }
        else {
            try {
                def ssuffix = tokens[5].replace('(','').replace(')','')
                entity.suffix = Integer.parseInt(ssuffix)
            }
            catch( e ) {
                entity.suffix = null
            }
        }

        RPTUtil.buildPin(entity, varSvc);
        binding?.refresh('.*');
    }
    
    def getLookupFaas() {
        return InvokerUtil.lookupOpener('faas:lookup',[
            onselect : { 
                updateFaas(it);
            },
            onempty : {
                this.faas = null;
                this.landfaas = null;
                binding?.refresh('.*');
            }
        ]);
    }
    
    
    void updateLandFaas(f) {
        this.landfaas = f; 
        entity.fullpin = f.fullpin + '-' + (entity.suffix ? entity.suffix : '0000');
        entity.newpin = entity.fullpin;
        binding?.refresh('entity.newpin');
    }
    
    def getLookupLandFaas() {
        return InvokerUtil.lookupOpener('faas:lookup',[
            rputype : 'land', 
            onselect : { 
                updateLandFaas(it); 
            },
            onempty : {
                this.landfaas = null;
            }
        ])
    }
    
    List getPinTypeList() {
        return ['new', 'old']
    }
    
    
    List getProvcityList() {
        def list = []
        list += lguSvc.lookupCities([:]);
        list += lguSvc.lookupProvinces([:]);
        return list;
    }
    
    List getMuniDistrictList() {
        def list = []
        list += lguSvc.lookupMunicipalities([:])
        list += lguSvc.lookupDistricts([:])
        return list;
    }
    
    List getBarangayList() {
        return lguSvc.lookupBarangaysByParentid(entity.munidistrict?.objid);
    }
    
}

