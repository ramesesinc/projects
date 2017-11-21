package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class FAASRequirementsModel 
{
    @Binding
    def binding;

    @Service('RPTRequirementService')
    def reqSvc;
    
    String title = 'Requirements'
    String entityName = 'rptrequirement';
    
    def entity;
    def requirements;
    def selectedItem;
        
    void init(){
        requirements = reqSvc.getRequirements(entity.objid);
        listHandler?.load();
    }
    
    def onupdateRequirement = {
        listHandler.refresh();
    }
    
    def getOpener(){
        if (!selectedItem)
            return null;
        def openers = InvokerUtil.lookupOpeners('rptrequirementtype:handler', [entity:selectedItem, svc:reqSvc, onupdate:onupdateRequirement]);
        return openers.find{it.properties.name == selectedItem.handler}
    }
    
    
    void refresh(){
        init();
    }
    
    
    def listHandler = [
        getRows   : { return (requirements.size() + 1) },
        fetchList : { return requirements },
    ] as BasicListModel
    
    
    
    boolean getShowViewer(){
        if (requirements.size() > 0 )
            return true;
        return false;
    }
    
            
    def openViewer(){
        return Inv.lookupOpener('rptrequirement:viewer', [entity : entity, requirements:requirements])
    }
    
    
}    
    