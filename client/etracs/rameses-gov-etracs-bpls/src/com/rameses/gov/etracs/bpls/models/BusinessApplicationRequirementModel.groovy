package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BusinessApplicationRequirementModel extends WorkflowTaskModel  { 
    
    def app;
    
    def getQuery() {
        return [appid: app.objid];
    }

    public def getBarcodeFieldname() {
        return "app.appno";
    }
    
    void afterOpen() {
        def m = [_schemaname: 'business_application'];
        m.objid = entity.applicationid;
        app = persistenceService.read( m );
    }
    
    
}