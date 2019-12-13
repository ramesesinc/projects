package com.rameses.gov.etracs.rpt.workflow;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.rcp.framework.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;

abstract class RPTWorkflowController extends WorkflowController
{
    @Caller 
    def caller;
        
    @Binding
    def binding;
    
    
    @Invoker
    def invoker 
    
    @FormId
    def formId;
    
    @FormTitle
    def formTitle;
    
    String formTitlePrefix = '';
    def sections;
    def selectedSection;
    def messages = [];
    
    abstract def getService();
    abstract String getFileType();
    abstract def getSections();

    
    void afterOpen( task ){
        loadSections();
    }
    
    public void onEnd() {
        //workaround to close actions when no task is available
        entity.taskstate = 'xxx';
        loadSections();
    }
    
    public void afterSignal(Object result){
        if (result.data){
            entity.putAll(result.data)
        }
        if (task) {
            entity.taskstate = task.state
            entity.assignee = task.assignee
        }
        if (prevtask.action != 'delete'){
            loadSections();
        }
    }
    

    final void loadSections(){
        sections = getSections().findAll{
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean( vw, [entity:entity] ));
        }
        if (sections){
            sections.sort{a,b -> a.properties.index <=> b.properties.index }
            selectedSection = sections[0];
        }
    }
    
    def prevtask = [:]
    public void beforeSignal(tsk){
        prevtask.putAll(tsk);
        entity.filetype = getFileType();
        checkMessages();
    }
        
    
    final void addMessage(msg){
        messages << msg;
    }
    
    
    final void clearMessages(type){
        messages.removeAll( messages.findAll{it.type == type} )
    }
    

    final void checkMessages(){
        if (messages)
            throw new Exception(messages[0].msg);
    }   
    
    def close(){
        binding.fireNavigation('_close');
    }
 
    @Activate
    public void onActivate(){
        ClientContext.currentContext.eventManager.postEvent('faas', entity);
    }
    
    @Close
    public void onClose(){
        ClientContext.currentContext.eventManager.sendMessage('faas', [action:'close', objid:entity.objid]);
    }
    
}
