package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class SubdivisionAffectedRpuModel
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    @Service('FAASService')
    def faasSvc;
    
    def svc;
    
    def entity;
    def selectedItem;
    def lands;
    def affectedrpus;
    def mode;
    
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    void init(){
        lands = svc.getSubdividedLands(entity.objid);
        affectedrpus = svc.getAffectedRpus(entity.objid);
        listHandler?.load();
        mode = MODE_READ;
    }
    
    def getOpener(){
        if (selectedItem && selectedItem.newfaasid) {
            return InvokerUtil.lookupOpener('faasdata:open',[
                    entity      : [objid:selectedItem.newfaasid], 
                    svc         : faasSvc, 
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
                    arpu.putAll(svc.deleteAffectedRpuFaas(arpu));
            }
        },
                
        validate : { li -> 
            def arpu = li.item;
            RPTUtil.required('New PIN', arpu.newpin);
            RPTUtil.required('New Suffix', arpu.newsuffix)
            validateNewPin(arpu)
            arpu.putAll(svc.saveAffectedRpuAndFaas(arpu));
        }
        
    ] as EditorListModel 
            
            
    void validateNewPin(arpu){
        def sland = lands.find{ it.newpin == arpu.newpin }
        if (!sland) throw new Exception('PIN entered is invalid. The PIN does not exist on the list of Subdivided Lands.')
        arpu.subdividedlandid = sland.objid;
        arpu.newrpid = sland.newrpid;
    }
    
    
    def getCount(){
        return affectedrpus.size();
    }
    
        
    boolean getShowActions(){
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.taskstate && !entity.taskstate.matches('.*taxmapper.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        if (mode != MODE_READ) return false;
        return true;
    }
    
    boolean getAllowEdit(){
        return mode != MODE_READ
    }
    
    
    /**************************************************
     * ADD NEW IMPROVEMENT
    **************************************************/
    def improvement;
    
    def addImprovement(){
        improvement = [:];
        return new PopupOpener(outcome:'newrpu');
    }
    
    def doAddImprovement(){
        if (! MsgBox.confirm('Add new improvement?')) 
            return;
            
       improvement.objid = 'SAF' + new java.rmi.server.UID();
       improvement.subdivisionid = entity.objid;
       improvement.ry = entity.ry;
       improvement.lguid = entity.lguid;
       improvement.subdividedlandid = improvement.subdividedland.objid;
       improvement.putAll(svc.addNewImprovement(improvement))
       affectedrpus << improvement;
       listHandler.reload();
       return '_close';
    }
    
    def getPintypes(){ 
        return ['new', 'old']
    }
    
    def getRputypes(){
        return ['bldg', 'mach', 'planttree', 'misc']
    }
    
    
    /**************************************************
    * CANCEL IMPROVEMENT SUPPORT 
    **************************************************/
    void cancelImprovement(){
        if (selectedItem && MsgBox.confirm('Cancel selected improvement?')){
            def improvement = [:]
            improvement.objid = selectedItem.objid;
            improvement.parentid = entity.objid;
            improvement.faasid = selectedItem.prevfaasid;
            improvement.arpu = selectedItem;
            svc.createCancelledImprovement(improvement);
            affectedrpus.remove(selectedItem);
            listHandler.reload();
        }
    }
}
       