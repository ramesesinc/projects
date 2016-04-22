package com.rameses.enterprise.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;

public class AbstractRequirementModel {
    
    @Service("RequirementListService")
    def requirementService;
    
    @Caller 
    def caller;

    def entry;
    String title;
    String schemaname
    
    void init() {
        title = entry.title;
    }
    
    void update() {
        entry._schemaname = schemaname;
        //service.update( entry );
        entry.remove("_schemaname");
        caller.refresh();
    }
    
}