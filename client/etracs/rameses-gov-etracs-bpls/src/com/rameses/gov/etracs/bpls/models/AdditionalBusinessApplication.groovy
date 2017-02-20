package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;

public class AdditionalBusinessApplication extends AbstractBusinessApplicationPageFlow  { 

    @Script("BusinessSearchUtil")
    def searchList;

    @Script("BusinessApplicationUtil")
    def application;

    @Script("BusinessLobUtil")
    def lob;

    @Script("BusinessInfoUtil")
    def appinfo;

    @FormTitle
    def title = "Additional Business";

    def entity;
    def text = 'Add line of business'

    void open() {
        application.init([ businessid:searchList.selectedItem.objid, apptype: 'ADDITIONAL']); 
        lob.reset(); 
        appinfo.reset(); 
    } 

    void updateInfo() { 
        def l = entity.lobs.findAll{ it.assessmenttype.matches('NEW') };
        if( !l ) {
            throw new Exception("There must be at least one additional line of business");
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