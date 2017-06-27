package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.framework.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;

public class FAASClosedModel
{

    @Binding
    def binding;
    
    @FormId
    def formId;
    
    @FormTitle
    def formTitle;
    
    @Service('FAASService')
    def svc;
    
    @Service('RPTSmsService')
    def smsSvc
    
    def entity;
    def sections;
    def selectedSection;
    def messages = [];
    
    
    void open(){
        entity = svc.openFaas(entity);
        formId = (entity.tdno ? entity.tdno : entity.utdno);
        formTitle = 'FAAS: ' + formId
    }   
    
    String getTitle(){
        return 'FAAS (' +  entity.state + ') '
    }
    
    def getSections(){
        return InvokerUtil.lookupOpeners( "faas:info", [entity:entity, svc:svc ] ).findAll {
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean( vw, [entity:entity] ));
        }
    }
     
    
    void notifyClient(){
        if (smsSvc)
            smsSvc.notifyClient(entity.trackingno);
        else
            throw new Exception('SMS Service is not available at this time. Try again later.');
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
