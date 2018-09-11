package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;

/************************************************************************
* NEW BUSINESS APPLICATION
*************************************************************************/
public class NewBusinessApplication extends AbstractBusinessApplicationPageFlow {

    @Script("BusinessVerificationUtil")
    def verifyList;

    @Script("BusinessLobUtil")
    def lob;

    @Script("BusinessApplicationUtil")
    def application;

    @Script("BusinessOwnerUtil")
    def owner;

    @Script("BusinessAddressUtil")
    def address;

    @Script("BusinessRequirementUtil")
    def requirement;

    @Script("BusinessInfoUtil")
    def appinfo;

    @Script("BusinessContactUtil")
    def contact;

    def entity;
    def lobAssessmentTypes = ['NEW'];

    void init() {
        application.init([apptype:'NEW']);
        lob.reset();
        address.reset();
        requirement.reset();
        appinfo.reset();
    }

    void updateInfo() {
        lob.verify();
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

}