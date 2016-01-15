package com.rameses.clfc.customer;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class CustomerSearchContext 
{
    private def caller;
    
    public CustomerSearchContext(def caller) {
        this.caller = caller;
    }
    
    public def getMode() { return caller?.mode; } 
    public def getService() { return caller?.service; } 
    public def getSelectedCustomer() { return caller?.selectedCustomer; } 
    
    
    /* event handlers */
    def selectHandler;
    def closeHandler;
    
} 