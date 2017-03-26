package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class SubdivisionCancelledImprovementModel
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    def svc;
    
    def entity;
    def selectedItem;
    def cancelledrpus;
    def mode;
    
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    String title = 'Cancelled Improvements';
    
    void init(){
        cancelledrpus = svc.getCancelledImprovements(entity.objid);
        listHandler?.load();
        mode = MODE_READ;
    }
    
    void edit(){
        caller.addMessage([type:'cancelledimprovement', msg:'Cancelled Affected Improvements still in editing mode.']);
        mode = MODE_EDIT;
    }
    
    void save(){
        caller.clearMessages('cancelledimprovement');
        mode = MODE_READ;
    }
    
    void undoCancel(){
        if (selectedItem && MsgBox.confirm('Undo cancellation?')){
            svc.undoCancelledImprovement(selectedItem);
            cancelledrpus.remove(selectedItem);
            listHandler.reload();
        }
    }
    
    def listHandler = [
        getRows : { return cancelledrpus.size() + 1 },
        fetchList : { return cancelledrpus },
    ] as BasicListModel 
            
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
    
}
       