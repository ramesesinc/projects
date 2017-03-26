package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*


class FAASInitGeneralRevisionModel extends FAASInitTxnModel 
{
    @Service('GeneralRevisionService')
    def grSvc;
    
    boolean generalRevision = true;
    
    
    def getRevisionyears(){
        return grSvc.getRyList(entity.faas?.lguid, entity.faas?.ry, entity.faas?.rputype );
    }
    
    void afterLookupFaas(){
        binding.refresh('entity.newry');
    }
        
    def process(){
        def faas = grSvc.initOnlineGeneralRevision( entity );
        return InvokerUtil.lookupOpener('faas:open', [entity:faas]);
    }
    
}