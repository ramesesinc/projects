package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class BatchGRClosedModel 
{
    @Binding
    def binding;
    
    @FormId
    def formId;
    
    @FormTitle
    def formTitle;
    
    @Service('BatchGRService')
    def svc;
    
    def entity;
    def sections;
    def selectedSection;
    
    
    void open(){
        entity = svc.open(entity);
        formId = entity.txnno;
        formTitle = 'Batch GR: ' + formId;
    }   
    
    String getTitle(){
        return 'Batch GR (' +  entity.state + ') '
    }
    
    public def getSections(){
        return InvokerUtil.lookupOpeners('batchgr:info', [entity:entity, svc:svc])
    }
}