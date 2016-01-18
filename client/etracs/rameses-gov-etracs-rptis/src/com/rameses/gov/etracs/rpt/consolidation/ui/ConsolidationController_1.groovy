package com.rameses.gov.etracs.rpt.consolidation.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.consolidation.task.*;
import com.rameses.util.MapBeanUtils;

public class ConsolidationController extends com.rameses.gov.etracs.rpt.workflow.RPTWorkflowController
{
    
    @Service('ConsolidationWorkflowService')
    def service;
        
    @Service('ConsolidationService')
    def svc;
    
    String formTitlePrefix = 'CS';
    
    def getService() {return service; }
    String getFileType(){ return 'consolidation'; }
    
    boolean autoposting = false;
    
    public def getSections(){
        return InvokerUtil.lookupOpeners( getFileType() + ':info', [entity:entity, svc:svc])
    }
    
    void afterOpen(o){
        super.afterOpen(o);
        formId = entity.txnno;
        formTitle = 'Consolidation: ' + formId;
    }
    
    
    public void beforeSignal(Object tsk){
        super.beforeSignal(tsk);
        if (processing == true){
            throw new Exception('Unable to process request. Posting is ongoing.')
        }
    }
    
            
    public void afterSignal(Object result){
        super.afterSignal(result);
        if (task && task.state.matches('approver|forprovsubmission')){
            autoposting = true;
        }
    }
    
    
    public Object findPage(Map o){
         if (task && task.state.matches('approver|forprovsubmission')){
             info = null;
             if (autoposting)
                doApprove();
             return 'wait';
         }
         return 'default';
    }
    
    String getTitle(){
        return 'Consolidation (' +  task.title + ') '
    }
    
    
    def approveTask = null;
    def info;
    def processing = false;
    def haserror = false;
    
    
    def oncomplete = {
        haserror = false;
        processing = false;
        approveTask = null;
        def res = signal('completed');
        if (res == null){
            entity.state = 'APPROVED';
            task.title = 'APPROVED';
            res = 'default';
        }
        binding.fireNavigation(res);
        binding.refresh();
        caller?.search();
    }
    
    def onerror = {
        haserror = true;
        processing = false;
        approveTask = null;
        showinfo('ERROR: ' + it)
    }
    
    def showinfo = { msg ->
        info += msg;
        binding.refresh('.*');
    }
    
    
    void doApprove() {
        checkMessages();
        info = '';
        processing = true;
        if (task.state == 'approver'){
            approveTask = new ManualApproveConsolidationTask(
                        svc             : svc, 
                        entity          : entity,
                        oncomplete      : oncomplete,
                        showinfo        : showinfo,
                        onerror         : onerror,
            );
        }
        else {
            approveTask = new SubmitToProvinceConsolidationTask(
                        svc             : svc, 
                        entity          : entity,
                        oncomplete      : oncomplete,
                        showinfo        : showinfo,
                        onerror         : onerror,
            );
        }
        Thread t = new Thread(approveTask);
        t.start();
        
    }
    
    void checkFinish(){
        if (haserror)
            throw new Exception('Approval cannot be completed due to errors. Fix errors before finishing the transaction.')
        if (processing)
            throw new Exception('Consolidation Approval is ongoing.');
    }


    
    
}
