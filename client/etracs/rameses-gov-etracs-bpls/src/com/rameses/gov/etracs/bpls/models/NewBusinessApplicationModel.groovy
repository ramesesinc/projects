package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.bpls.controller.*;
import com.rameses.util.*;

class NewBusinessApplicationModel extends PageFlowController {

    @Service("BusinessApplicationService")
    def service;            

    @Script("BusinessVerificationUtil")
    def verifyList;

    @Script("BusinessAddressUtil")
    def address;

    @Script("BusinessRequirementUtil")
    def requirement;

    @Script("BusinessInfoUtil")
    def appinfo;

    def entity;

    void init() {
        entity = service.initNew( [apptype:'NEW'] );
        entity.yearstarted = entity.appyear;
        requirement.reset();
        appinfo.reset();
    }

    void updateInfo() {
        if( !entity.lobs ) throw new Exception("Please specify at least one line of business");
        boolean test = false;
        appinfo.handler = {
            test = true;
        }
        Modal.show(appinfo.update());
        if(!test) throw new BreakException();
        appinfo.verify();
    }

    void initAddressType() {
        owner.reload(); //so we can reload address
        if( application.copyOwnerAddress ) {
            def map = [:];
            map.putAll( entity.business.owner.address ); 
            map.objid = null; 

            entity.business.address = map; 
            address.addressType = entity.business.address.type;
        }
        else {
            address.addressType = application.addressType;
        }
    }

    void verifyContacts() {
        boolean test = false;
        if( entity.business.mobileno ) test = true;
        if( entity.business.phoneno ) test = true;
        if( entity.business.email ) test = true;            
        if(!test) throw new Exception("Please provide at least one contact info");
    }
    
}