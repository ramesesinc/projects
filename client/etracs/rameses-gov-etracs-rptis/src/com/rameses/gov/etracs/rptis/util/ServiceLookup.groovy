package com.rameses.gov.etracs.rptis.util;


import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*; 
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.service.ScriptServiceContext;

public class ServiceLookup
{
    private ServiceLookup(){
    }
    
    public static def create(serviceName){
        return InvokerProxy.instance.create( serviceName );
    }
}