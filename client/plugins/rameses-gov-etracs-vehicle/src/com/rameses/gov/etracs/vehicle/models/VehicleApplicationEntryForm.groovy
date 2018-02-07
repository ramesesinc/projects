package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleApplicationEntryForm extends PageFlowController {
    
    @Service("VehicleAssessmentService")
    def assessmentService;
    
    @Service("VehicleApplicationService")
    def applicationService;

    @FormTitle
    def formTitle;
    
    @Binding
    def binding;
    
    def franchiseno;
    def ruleExecutor;
    def entity;
    def vehicletype;
    def editmode = "read";
    
    def apptypes = ["NEW","RENEW"];
    def appType;
    int reqYear;
    
    def df = new java.text.SimpleDateFormat( "yyyy" );
    
    void setUp() {
        formTitle = workunit.info.workunit_properties.title;
        if(!formTitle) formTitle = getTitle();
        vehicletype = workunit?.info?.workunit_properties?.vehicletype;
        ruleExecutor = new RuleProcessor(  { p-> return assessmentService.assess(p) } );
        entity = [:];
        entity.apptype = workunit?.info?.workunit_properties?.apptype;
        entity.txnmode = "ONLINE";
        entity.vehicletype = vehicletype;
    }

    void create() {
        setUp();
        entity = applicationService.init(entity);
        editmode = 'create';
    }
    
    @PropertyChangeListener
    def listeners = [
        "appType" : { o->
            if( entity.txnmode == "CAPTURE"  ) {
                entity.apptype = o;
            }
        },
        "entity.appdate": { o->
            entity.appyear = df.format( df.parse(o) ).toInteger();
        }
    ];
    
    //called by all except new
    void open() {
        if(!entity) throw new Exception("Call the setUp method in ApplicationForm first. Check start action");
        entity.franchiseno = franchiseno;
        entity = applicationService.init( entity );
        editmode = 'read';
        if(entity.appyear) reqYear = entity.appyear;
    }
    
    void save() {
        entity = applicationService.create( entity );
    }
    
    def assess() {
        def m = [rulename : 'vehiclebilling'];
        m.handler = { o->
            if(!o) return;  
            entity.fees = o.billitems;
            entity.infos = o.infos;
            entity.amount = o.amount;
            entity.expirydate = o.expirydate;
            if(o.franchise?.expirydate) {
                entity.franchise.expirydate = o.franchise.expirydate;
            }
            entity.billexpirydate = entity.billexpirydate;
            feeListModel.reloadAll();
            infoListModel.reloadAll();
            binding.refresh();
        }
        m.params = [application: entity, franchise: entity.franchise];
        m.defaultInfos = entity.infos;
        return Inv.lookupOpener( "assessment", m );
    }

    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    def infoListModel = [
        fetchList: { o->
            return entity.infos;
        }
    ] as BasicListModel;
    
    //this is just dummy for the new application so the page will not error.
    def paymentListModel = [
        
    ] as BasicListModel;
    
    def getLookupAvailableFranchise() {
        return Inv.lookupOpener( "vehicle_franchise_" + vehicletype + ":available:lookup" );
    }


    boolean getFranchiseControlEditable() {
        if( entity.txnmode == 'CAPTURE' ) return true;
        if( entity.apptype == 'NEW') return true;    
        return false;
    }
    
    boolean getOwnerEditable() {
        if( entity.txnmode == 'CAPTURE' ) return true;
        if( entity.apptype.matches( 'NEW|CHANGE_OWNER_UNIT' ) ) return true;
        return false;
    }
    
    //Print application and assessment
    def printApplication() {
        return Inv.lookupOpener('vehicle_application_' +  vehicletype +  ':print', [entity: entity]); 
    }
    
    def printTrackingno() {
        Modal.show( "show_vehicle_trackingno", [appno: entity.appno] );
    }
    
    def printAssessment() {
        return Inv.lookupOpener('vehicle_assessment_' + vehicletype + ':print', [entity: entity]); 
    }
    
}