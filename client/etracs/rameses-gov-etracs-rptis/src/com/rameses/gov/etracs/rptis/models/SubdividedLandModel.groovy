package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*


class SubdividedLandModel
{
    @Caller
    def caller;
    
    
    @Binding
    def binding;
    
    @Service('FAASService')
    def faasSvc;
    
    @Service('SubdivisionService')
    def svc; 
    
    def entity; //subdivision 
    def selectedItem;
    def lands;
    def faas;
    def motherlands;
    def assistant;
    
    void init(){
        lands = svc.getSubdividedLands(entity.objid);
        motherlands = svc.getMotherLands([objid:entity.objid]);
        assistant = getAssistant();
    }
    
    def getOpener(){
        if (! selectedItem) 
            return null;
        faas = [objid:selectedItem.newfaasid, assistant: assistant];    
        def params = [
            entity:faas, 
            svc:faasSvc, 
            taskstate:entity.taskstate, 
            assignee:entity.assignee,
            assistant: assistant
        ]
        return InvokerUtil.lookupOpener('faasdata:open', params);
    }
    
    def listHandler = [
        getRows      : {lands.size()},
        
        fetchList    : {return lands},
        
        onRemoveItem : {item ->
            if (MsgBox.confirm('Remove selected item?')){
                doRemoveItem();
                return true;
            }
            return false;
        },
    ] as EditorListModel;
    
    void doRemoveItem(){
        selectedItem.assistant = assistant
        svc.deleteSubdividedLand(selectedItem);
        lands.remove(selectedItem);
        listHandler.load();
        binding.refresh('count');
    }
    
    void removeItem(){
        if (MsgBox.confirm('Remove selected item?')){
            doRemoveItem();
        }
    }
    
    def add(){
        motherlands = svc.getMotherLands([objid:entity.objid]);
        if (!motherlands) 
            throw new Exception('Mother Land(s) to subdivide is/are not yet specified.');
            
        return InvokerUtil.lookupOpener('realproperty:create', [
            entity     : entity,
            barangayid : motherlands[0].barangayid,
            lands      : lands,
            
            oncreate : { rp -> 
                def land = [
                    objid           : 'SL' + new java.rmi.server.UID(),
                    subdivisionid   : entity.objid, 
                    newfaasid       : rp.objid,
                    newpin          : rp.pin,
                    rp              : rp,
                    assistant       : assistant,
                ]
                lands << svc.createSubdividedLand(land, entity);
                listHandler.load();
                binding.refresh('count');
            }
        ])
    }
    
    
    
    boolean getShowActions(){
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        if (!isAssignee()) return false;
        return true;
    }

    def isAssignee() {
        if (OsirisContext.env.USERID == entity.assignee.objid || assistant) {
            return true;
        }
        return false;
    }
    
    
    void addMessage(msg){
        caller.addMessage(msg);
    }
    
    void clearMessages(msg){
        caller.clearMessages(msg);
    }
    
    def getCount(){
        return lands.size();
    }

    def getAssistant() {
        def taskstate = entity.taskstate.toString().replace('prov', '');
        return svc.getAssistantInfo([objid: OsirisContext.env.USERID, taskstate: taskstate]);
    }
}
