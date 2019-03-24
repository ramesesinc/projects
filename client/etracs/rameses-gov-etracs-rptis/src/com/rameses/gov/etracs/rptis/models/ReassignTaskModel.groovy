package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class ReassignTaskModel {
    @Binding
    def binding;
            
    @Service("RPTTaskService")
    def svc;

    def entity;
    def taskstates;
    
    def doctype;
    def doctypes = ['faas', 'subdivision', 'consolidation', 'cancelledfaas'];
    
    void init() {
        entity = [:];
    }
    
    void loadTask() {
        entity.task = svc.findCurrentTask(entity);
        loadTaskStates();
        entity.newstate = taskstates.find{it.name == entity.task.state};
        if (!entity.newstate) {
            throw new Exception('Task ' + entity.task.state + ' is defind in the current workflow.\nPlease contact administrator.' );
        }
    }
    
    def getLookup() {
        def lookupname = doctype + ':lookup';
        return Inv.lookupOpener(lookupname, [
                onselect: {
                    it.processname = doctype;
                    entity = it;
                    loadTask();
                },
                onempty: {
                    entity = null;
                    binding.refresh();
                }
        ]);
    }
    
    void loadTaskStates() {
        def param = [processname: entity.processname]
        taskstates = svc.getTaskStates(param);
    }
    
    def getAssignees() {
        entity.newstate.assigneeid = entity.task.assignee.objid;
        svc.getAssignees(entity.newstate);
    }
    

    def reassign() {
        if (MsgBox.confirm('Reassign task?')) {
            svc.reassign(entity);
            return '_close';
        }
    }

            
}