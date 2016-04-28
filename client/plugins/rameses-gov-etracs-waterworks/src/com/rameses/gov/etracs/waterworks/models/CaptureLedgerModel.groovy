package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class CaptureLedgerModel  {

    @Service("WaterworksLedgerService")
    def svc;
    
    def info = [:];
    def entity;
    def handler;
    def payOption = [:];
    
    def txnTypes = ["WFEE", "BOM", "OTHER"];
    
    def doOk() {
        info.payOption = payOption;
        info.parentid = entity.objid;
        svc.post( info );
        handler();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}