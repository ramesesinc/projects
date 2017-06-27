package com.rameses.gov.etracs.rpt.cancelledfaas.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.common.*;

public class CancelledFAASController 
{
    @Binding
    def binding;
    
    @Invoker
    def invoker;
    
    @Service('CancelledFAASService')
    def svc;
    
    @Service('Var')
    def var;
    
    def MODE_EDIT   = 'edit';
    def MODE_READ   = 'read';
    
    def entity;
    def mode = MODE_READ;
    def callbacks;
    def taskstate;
    def assignee; 
    def queryinfo;
    def reasons;
    
        
    String entityName = 'cancelledfaas';
    
    public String getTitle(){
        return 'FAAS No.: ' + entity.faas.tdno;
    }
    
    void open(){
        entity.putAll(svc.openCancelledFaas(entity));
        if (taskstate) entity.taskstate = taskstate;
        if (assignee) entity.assignee = assignee;
        entity.reason = getCancelReasons().find{it.objid == entity.reason.objid}
        mode = MODE_READ;
    }
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    void cancelEdit(){
        entity.putAll(svc.openCancelledFaas(entity));
        if (taskstate) entity.taskstate = taskstate;
        if (assignee) entity.assignee = assignee;
        mode = MODE_READ;
    }
    
    void save(){
        entity.putAll(svc.updateCancelledFaas(entity));
        mode = MODE_READ;
    }
    
    
    def delete(){
        if (MsgBox.confirm('Delete?')){
            svc.deleteCancelledFaas(entity);
            return '_close';
        }
        return null;
    }
    
    boolean getShowActions(){
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        if (OsirisContext.env.USERID != entity.assignee.objid) return false;
        return true;
    }  
    
    List getCancelReasons(){
        if (!reasons)
            reasons = svc.getCancelReasons();
        return reasons;
    }    
}