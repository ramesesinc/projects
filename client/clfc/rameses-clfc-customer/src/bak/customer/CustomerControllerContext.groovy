package com.rameses.clfc.customer;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class CustomerControllerContext 
{
    private def caller;
    private def service;
    
    public CustomerControllerContext(def caller, def service) {
        this.caller = caller;
        this.service = service;
    }
    
    public def getService() { return service; } 
    public def getMode() { return caller?.mode; } 
    public def getEntity() { return caller?.entity; } 
} 