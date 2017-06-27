package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class FAASInitCaptureGeneralRevisionModel extends FAASInitTxnModel
{
        
    @Service('GeneralRevisionService')
    def grSvc;
    
    
    boolean generalRevision = true;
    
    
    void afterInit(){
        
    }
        
    def process(){
        entity.datacapture = true;
        entity = grSvc.createDataCaptureGeneralRevision( entity );
        return InvokerUtil.lookupOpener('faas:capture:gr', [entity:entity]);
    }
    
    def getRevisionyears(){
        return grSvc.getRyList(entity.faas?.lguid, entity.faas?.ry, entity.faas?.rputype );
    }
    
    void afterLookupFaas(){
        binding.refresh('entity.newrysetting');
    }
        
        
    def doRevise(){
        def faas = entity;
        
        if (faas.state == 'CANCELLED')
        throw new Exception('FAAS has already been cancelled.');
        
        entity = [:]
        super.init();
        entity.faas = faas;
        def rys = getRevisionyears()
        if (!rys) throw new Exception('FAAS has already been revised or no revision setting defined for year ' + faas.ry + '.');
        entity.newry = rys[0];
        
        if (entity.faas.ry == entity.newry) 
        throw new Exception('FAAS has already been revised for Revision Year ' + entity.newry + '.')
        
        return process();
    }
    
    def isShowRevision(){
        return false;
    }
      
    
}
        
