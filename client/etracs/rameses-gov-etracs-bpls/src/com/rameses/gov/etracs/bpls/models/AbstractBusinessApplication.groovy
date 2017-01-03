package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;

/************************************************************************
* This abstract class is extended by 
*************************************************************************/
public abstract class AbstractBusinessApplication extends PageFlowController {

    @Service("BusinessApplicationService")
    def service;

    @Script("BusinessLobUtil")
    def lob;

    @Script("BusinessOwnerUtil")
    def businessOwner;

    @Script("BusinessRequirementUtil")
    def businessReq;

    @Script("BusinessInfoUtil")
    def businessInfo;

    @Script("BusinessAddressUtil")
    def businessAddress;

    def appTypes = ['NEW','RENEW'];
    
    def businessPurposes = LOV.BUSINESS_PURPOSE;
    def entity;
    
    @PropertyChangeListener
    def listener = [
        "business.orgtype" : { o->
            business.owner = null;
            if(handler) handler();
        }
    ]

    def copyBusinessName() {
        entity.business.tradename = entity.business.businessname;
        binding.refresh("entity.business.tradename");
        binding.focus("entity.business.tradename");
    }


}