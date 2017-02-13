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
public class RenewBusinessApplication extends AbstractBusinessApplicationPageFlow  {

    @Service("EntityService")
    def entitySvc;

    @Script("BusinessSearchUtil")
    def searchList;

    @Script("BusinessAddressUtil")
    def address;

    @Script("BusinessApplicationUtil")
    def application;

    @Script("BusinessRequirementUtil")
    def requirement;

    @Script("BusinessLobUtil")
    def lob;

    @Script("BusinessInfoUtil")
    def appinfo;

    @Script("BusinessContactUtil")
    def contact;

    @Service("BusinessMasterService")
    def businessMasterSvc;

    def entity;

    def message = "Please review the current business information. Apply the necessary updates.";

    void loadBusiness( def businessid ) {
        try {
            application.init( [businessid:businessid, apptype: 'RENEW'] );
            lob.reset(); 
            requirement.reset(); 
        } catch(Warning w) {
            Modal.show( 'business_redflag:warning', [list: w.info.list] );
            throw new BreakException();
        } catch(e) {
            throw e;
        }
    }

    void open() {
        loadBusiness( searchList.selectedItem.objid );
    }

    void verifyOwnerAddress() {
        if( !entity.business.owner.address.objid )
            throw new Exception("Owner address is not specified. Please update address");
    }

    void reloadAddress() {
        if(!entity.business.owner.objid) 
            throw new Exception("Please select first an owner");
        def owner = entitySvc.open([objid:entity.business.owner.objid]);
        entity.business.owner = owner;
    }

    void updateInfo() {
        def test = false;
        appinfo.handler = {
            test = true;
        }

        def renewapp = entity.lobs.find{ it.assessmenttype.toString().toUpperCase()=='RENEW' }
        def retireapp = entity.lobs.find{ it.assessmenttype.toString().toUpperCase()=='RETIRE' }
        def newapp = entity.lobs.find{ it.assessmenttype.toString().toUpperCase()=='NEW' }
        if ( retireapp?.objid && !renewapp?.objid && !newapp?.objid ) { 
            throw new Exception('Please use the Retire Application facility if all lines of business were marked as RETIRE'); 
        } 

        requirement.fetch(); 

        Modal.show( appinfo.update());
        if(!test) throw new BreakException();
        appinfo.verify();
    }

    def getLobAssessmentTypes() {
        return lob.assessmentTypes; 
    } 

    void searchBarcode() {
        def code = MsgBox.prompt("Enter barcode");
        if(!code) throw new BreakException();
        def bin = code.substring( code.indexOf(":")+1 ); 
        def b = businessMasterSvc.findByBIN([bin: bin]);
        if(!b) throw new Exception("Business does not exist");
        loadBusiness( b.objid );
    }

    void save() { 
        requirement.verify();
        application.save(); 
    } 
}