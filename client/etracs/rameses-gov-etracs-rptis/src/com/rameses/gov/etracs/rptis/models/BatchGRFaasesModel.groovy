package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*


class BatchGRFaasesModel
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    @Service('FAASService')
    def faasSvc;
    
    @Service('BatchGRService')
    def svc; 
    
    def entity; //batchgr
    def selectedItem;
    def items;
    def faas;
    
    void init(){
        items = svc.getItems([objid: entity.objid]);
    }
    
    def getOpener(){
        if (! selectedItem) 
            return null;
        faas = [objid:selectedItem.newfaasid];    
        def params = [entity:faas, svc:faasSvc, taskstate:entity.taskstate, assignee:entity.assignee]
        return InvokerUtil.lookupOpener('faasdata:open', params);
    }
    
    def listHandler = [
        getRows      : {items.size()},
        fetchList    : {return items},
    ] as BasicListModel;
    
    void addMessage(msg){
        caller.addMessage(msg);
    }
    
    void clearMessages(msg){
        caller.clearMessages(msg);
    }
    
    def getCount(){
        return items.size();
    }
}
