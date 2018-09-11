package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BusinessApplicationModel extends WorkflowTaskModel  { 
    
    @Service("BusinessApplicationRequirementService")
    def service;
    
    @Script("BusinessInfoUtil")
    def businessInfo;
    
    def reqHandler;
    
    def getQuery() {
        return [appid: entity.objid];
    }
    
    void updateInfo() {
        def handler = { o->
            o.each {
                println it;
            };
        }
        businessInfo.update(entity, handler );
    }
    
    public def getBarcodeFieldname() {
        return "appno";
    }
    
    def selectedDocRequirement;
    
    void editDoc() {
        if( !selectedDocRequirement ) throw new Exception("Please select an item");
        MsgBox.alert("doc " + selectedDocRequirement);
    }
    
    void execReqRules() {
        service.updateRequirements( entity );
        reqHandler.reload();
    }
    
}