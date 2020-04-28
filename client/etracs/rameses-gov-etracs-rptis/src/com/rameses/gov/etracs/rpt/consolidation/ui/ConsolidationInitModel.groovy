package com.rameses.gov.etracs.rpt.consolidation.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class ConsolidationInitModel {
    @Service('ConsolidationService')
    def svc;
    
    @Service('FAASService')
    def faasSvc;

    def entity = [:]
    def attributes;
    
    String getTitle(){
        return 'Consolidation: Initial';
    }
    
    void init(){
        attributes = faasSvc.getTxnTypeAttributes([objid:'CS'])
    }

    def process(){
        entity.objid = 'CS' + new java.rmi.server.UID();
        entity.filetype = 'consolidation';
        entity.attributes = listHandler.selectedValue;
        def newentity = svc.create(entity)
        return InvokerUtil.lookupOpener('consolidation:open', [entity:newentity]);
    }
    
    def listHandler = [
        fetchList : { attributes },
        isMultiSelect: { true },
    ] as BasicListModel;
}