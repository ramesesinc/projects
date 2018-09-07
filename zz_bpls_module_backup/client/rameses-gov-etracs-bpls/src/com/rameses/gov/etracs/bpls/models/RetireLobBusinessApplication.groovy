package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;

public class RetireLobBusinessApplication extends AbstractBusinessApplicationPageFlow  { 

    @Script("BusinessSearchUtil")
    def searchList;

    @Script("BusinessApplicationUtil")
    def application;

    @Script("BusinessLobUtil")
    def lob;

    @Script("BusinessInfoUtil")
    def appinfo;

    @FormTitle
    def title = "Retire Line of Business";

    def entity;
    def text = 'Select line of business to retire. Click Retire button'

    void open() {
        application.init([ businessid:searchList.selectedItem.objid, apptype: 'RETIRELOB']); 
        lob.reset(); 
        appinfo.reset(); 
    } 

    void updateInfo() { 
        def l = entity.lobs.findAll{ it.assessmenttype.matches('RETIRE') };
        if( !l ) {
            throw new Exception("There must be at least one retire line of business");
        }    
        l = entity.lobs.findAll{ it.assessmenttype.matches('ACTIVE') };
        if( !l ) {
            throw new Exception("There must be at least one active line of business");
        } 
        lob.verify(); 
        boolean pass = false;
        appinfo.handler = { 
            pass = true;
        }
        Modal.show(appinfo.update());
        if(!pass) throw new BreakException();
        appinfo.verify();
        lob.removeActiveLobs();
    }
    
}