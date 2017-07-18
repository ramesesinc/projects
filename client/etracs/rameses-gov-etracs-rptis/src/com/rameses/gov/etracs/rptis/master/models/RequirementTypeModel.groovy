package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class RequirementTypeModel extends MasterModel
{
    def handler;
    def handlers;
            
    public void afterOpen(){
        handler = getHandlers().find{it.properties.name == entity.handler }
    }
    
    def getHandlers(){
        if (!handlers)
            handlers = InvokerUtil.lookupOpeners('rptrequirementtype:handler', [:]);
        return handlers;
    }
            
    void setHandler(handler){
        this.handler = handler;
        entity.handler = handler.properties.name;
    }
}