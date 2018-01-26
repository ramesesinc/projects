package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.seti2.models.*; 

public class SingleBillingModel extends FormReportModel {

    @Controller
    def workunit;
        
    @Invoker
    def invoker;
    
    @Service("SingleBillingService")    
    def billingSvc;
    
    @Caller
    def caller;
    
    public String getRulename() {
        def pfn = invoker.properties.rulename;
        if(pfn) return pfn;
        pfn = workunit?.info?.workunit_properties?.rulename;
        if ( pfn ) return pfn; 
        throw new Exception("rulename is required in SingleBillingModel")
    }
    
    public def init() {
        return "init";
    }
    
    public void runBill() {
        if(!query) query = caller.entity;
        data = [data: billingSvc.execute( [rulename: rulename, params: query ] )];
    }
    
}