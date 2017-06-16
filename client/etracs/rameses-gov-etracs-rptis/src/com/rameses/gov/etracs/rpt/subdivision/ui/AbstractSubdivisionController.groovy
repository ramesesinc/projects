package com.rameses.gov.etracs.rpt.subdivision.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;
import com.rameses.gov.etracs.rpt.workflow.RPTWorkflowController;
import com.rameses.gov.etracs.rpt.subdivision.task.*;
import java.util.List;

public abstract class AbstractSubdivisionController extends RPTWorkflowController
{
    @Caller 
    def caller;
    
    @Service('SubdivisionWorkflowService')
    def service;
    
    @Service('SubdivisionService')
    def svc;
    
    String formTitlePrefix = 'SD';
    
    def getService() {return service; }
    String getFileType(){ return 'subdivision'; }
    

    boolean autoposting = false;
    
    public def getSections(){
        return InvokerUtil.lookupOpeners('subdivision:info', [entity:entity, svc:svc])
    }
            
    void afterOpen(o){
        super.afterOpen(o);
        formId = entity.txnno;
        formTitle = 'Subdivision: ' + formId;
        buildQueryInfo();
    }
    
    public void beforeSignal(Object tsk){
        super.beforeSignal(tsk)
        if (processing == true){
            throw new Exception('Unable to process request. Posting is ongoing.')
        }
    }
    
        
    public void afterSignal(Object result){
        super.afterSignal(result);
        entity.putAll(svc.openSubdivision(entity.objid));
        if (task && task.state.matches('cityapprover|provapprover|forprovsubmission')){
            autoposting = true;
        }
    }
    
    public Object findPage(Map o){
         if (task && task.state.matches('cityapprover|provapprover|forprovsubmission')){
             info = null;
             if (autoposting)
                doApprove();
             return 'wait';
         }
         return 'default';
    }
    
    String getTitle(){
        return 'Subdivision (' +  task.title + ') '
    }
    
    
    def approverTask = null;
    def info;
    def processing = false;
    def haserror = false;
    
    
    def oncomplete = {
        haserror = false;
        processing = false;
        approverTask = null;
        def res = signal('completed');
        if (res == null){
            entity.state = 'APPROVED';
            task.title = 'APPROVED';
            res = 'default';
        }
        binding?.fireNavigation(res);
        binding?.refresh();
        caller?.search();
    }
    
    def onerror = {
        haserror = true;
        processing = false;
        approverTask = null;
        showinfo('ERROR: ' + it)
    }
    
    def showinfo = { msg ->
        info += msg;
        binding?.refresh('.*');
    }
    
    abstract def getApproverTask(task);
    
    final void doApprove() {
        checkMessages();
        info = '';
        processing = true;
        approverTask = getApproverTask(task);
        if( approverTask){
            approverTask.svc         = svc; 
            approverTask.entity      = entity;
            approverTask.oncomplete  = oncomplete;
            approverTask.showinfo    = showinfo;
            approverTask.onerror     = onerror;
            Thread t = new Thread(approverTask);
            t.start();
        }
    }
    
    void cancelApproval(){
        autoposting = true;
        binding.fireNavigation(signal('forprovapproval'));
    }

        
    void checkFinish(){
        if (haserror)
            throw new Exception('Approval cannot be completed due to errors. Fix errors before finishing the transaction.')
        if (processing)
            throw new Exception('Subdivision Approval is ongoing.');
    }
    
    
    def queryinfo;
    void buildQueryInfo(){
        queryinfo = null;
        def redflagCount = svc.getOpenRedflagCount([objid:entity.objid]);
        if (redflagCount > 0){
            queryinfo = redflagCount + ' Red Flag' + (redflagCount == 1 ? ' needs' : 's need') + ' to be resolved.';
        }
    }
    
    List getMessagelist(){
        if (queryinfo)
            return [queryinfo];
        return null;
    }
        
}


