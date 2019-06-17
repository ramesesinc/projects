package com.rameses.gov.etracs.rpt.realproperty.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.util.MapBeanUtils;

public class RealPropertyController
{
    @Binding
    def binding;
          
    @Service('RealPropertyService')
    def svc;
    
    @Service('LGUService')
    def lguSvc;

    @Service('Var')
    def var;

    @Service('QueryService')
    def querySvc;
    
    String getTitle(){
        return 'Real Property'
    }
    
    def entity;
    def barangayid;
    def lands;
    
    def oncreate; //handler 
    
    def ryList;
    
    def sketchOpener; 
    
    @PropertyChangeListener
    def listener = [
        'entity.pintype|entity.isection|entity.iparcel' :{ buildPin() },
    ]
    
                
    void init(){
        ryList = svc.getRyList();
        entity = svc.init();
        entity.barangay = lguSvc.lookupBarangayById(barangayid);
        entity.isection = null;
        entity.iparcel = null;
        entity.ry = ryList.find{it == entity.ry}
        entity.rp = [:];
        if (lands) {
            updateRealPropertyInfo(entity);
        }
        sketchOpener = Inv.lookupOpener('sketch:rp', [entity: entity]);
    }
        
    def add(){
        updateLguInfo()
        entity.putAll(entity.rp);
        svc.checkDuplicatePin(entity);
        if (oncreate) oncreate(entity);
        return '_close';
    }
    
                
    void buildPin(){       
        entity.rputype = 'land';
        entity.suffix = 0;
        RPTUtil.buildPin(entity, var);
        binding?.refresh('entity.pin');
    }
    

    void updateLguInfo(){
        if ('DISTRICT'.equalsIgnoreCase(entity.barangay?.parent?.orgclass)){
            entity.lguid = entity.barangay.provcity.objid
            entity.lgutype = 'city'
        }
        else {
            entity.lguid = entity.barangay?.munidistrict.objid
            entity.lgutype = 'municipality'
        }
    }
    
    
    def getPinTypes(){
        return ['new','old'];
    }

    void updateRealPropertyInfo(entity) {
        def land = lands.last();
        if (land.newrpid) {
            def param = [_schemaname: 'realproperty'];
            param.findBy = [objid: land.newrpid];
            def rp = querySvc.findFirst(param)
            entity.rp.surveyno = rp.surveyno 
        }
    }
            
}
