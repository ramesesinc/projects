package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import com.rameses.util.*;

class AssignMeter {

    @Service("WaterworksApplicationService")
    def appSvc;

    @Caller
    def caller;
    def handler;

    def info = [:];

    void init() {
        def e = caller.entity;
        info.objid = e.objid;
        info.meter = e.meter;
        info.installer = e.installer;
        info.dtinstalled = e.dtinstalled;
        info.initialreading = e.initialreading;
    }

    def doCancel() {
        return "_close";
    }          

    def doOk() { 
        if(handler) { 
            handler(info); 
        } 
        else { 
            appSvc.assignMeter( info ); 
            caller.entity.putAll( info ); 
            caller.refresh(); 
        }
        return "_close";
    }                              
} 
    