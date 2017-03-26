package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class FAASAffectedRpuController
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    @Service('FAASRevisionService')
    def revisionSvc;
    
    def svc;
    
    def entity;
    def selectedItem;
    def affectedrpus;
    def mode;
    
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    void test(){
        affectedrpus = revisionSvc.getAffectedRpus('xx');
        listHandler?.load();
        mode = MODE_READ;
    }
    
    void init(){
        affectedrpus = revisionSvc.getAffectedRpus(entity.objid);
        listHandler?.load();
        mode = MODE_READ;
    }
    
    def getOpener(){
        if (selectedItem && selectedItem.newfaasid) {
            return InvokerUtil.lookupOpener('faasdata:open',[
                    entity      : [objid:selectedItem.newfaasid], 
                    svc         : svc, 
                    taskstate   : entity.taskstate, 
                    assignee    : entity.assignee,
            ]);
        }
        return null;
    }    
    
    
    void edit(){
        caller.addMessage([type:'affectedrpu', msg:'Affected RPUs is still in editing mode.']);
        mode = MODE_EDIT;
    }
    
    void save(){
        caller.clearMessages('affectedrpu');
        mode = MODE_READ;
    }
    
    def listHandler = [
        getRows : { return affectedrpus.size() + 1 },
            
        fetchList : { return affectedrpus },
                
        onColumnUpdate : {arpu, colname ->
            if (colname == 'newpin'){
                if (arpu.newpin)
                    validateNewPin(arpu);
                else
                    arpu.putAll(revisionSvc.deleteAffectedRpuFaas(arpu));
            }
        },
                
        validate : { li -> 
            def arpu = li.item;
            RPTUtil.required('New Suffix', arpu.newsuffix)
            validateNewPin(arpu)
            arpu.putAll(revisionSvc.saveAffectedRpuAndFaas(arpu));
        }
        
    ] as EditorListModel 
            
            
    void validateNewPin(arpu){
        def dup = affectedrpus.find{it.objid != arpu.objid && it.newsuffix == arpu.newsuffix}
        if (dup)
            throw new Exception('Duplicate suffix is not allowed. Suffix is already assigned to PIN ' + dup.prevpin + '.');
    }
    
    
    def getCount(){
        return affectedrpus.size();
    }
    
        
    boolean getShowActions(){
        if (entity.state.matches('CURRENT|CANCELLED')) return false;
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.taskstate && !entity.taskstate.matches('.*taxmapper.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        if (mode != MODE_READ) return false;
        return true;
    }
    
    boolean getAllowEdit(){
        return mode != MODE_READ
    }
    
}
