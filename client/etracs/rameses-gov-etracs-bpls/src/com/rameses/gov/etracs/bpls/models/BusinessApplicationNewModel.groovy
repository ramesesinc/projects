package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

/************************************************************************
* NEW BUSINESS APPLICATION
*************************************************************************/
public class BusinessApplicationNewModel extends CrudPageFlowModel {

    @Service("DateService")
    def dateSvc;
    
    def permitType;
    
    def officeTypes = ["MAIN", "BRANCH"];
    def permitTypes = [
        [id:'standard', title:"BUSINESS_PERMIT"],
        [id:'market', title:"MARKET/GOV PROPERTY RENTAL"],
        [id:'lessor', title:"LESSOR"]
    ];    
    def orgTypes = [ [key:"SING", value:"SINGLE PROPRIETORSHIP"] ] + LOV.JURIDICAL_ORG_TYPES + [ [key:"MULTIPLE", value:"MULTIPLE"] ];
    
    def selectedRequirement;
    
    @PropertyChangeListener
    def listener = [
        "entity.business.orgtype" : { o->
            entity.business.owner = [:];
        }
    ]
    
    void afterCreate() {
        entity.apptype = "NEW";
        entity.appyear = dateSvc.getServerYear();
        entity.business = [owner:[:], orgtype: "SING"];
    }
    
    
    public String getEntityType() {
        if( entity.business.orgtype == "SING" ) {  
            return "individual";
        }   
        else if( entity.business.orgtype == "MULTIPLE" ) {
            return "multiple";
        }
        else {
            return "juridical:"+entity.business.orgtype.toLowerCase();
        }
    }
    
    def selectedItem;
    def lobListModel = [
        fetchList: { o->
            return entity.lobs;
        }
    ] as EditorListModel;
    
    def getQuery() {
        return [appid: entity.objid]
    }
    
    /*
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
    */
}