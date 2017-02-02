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

public abstract class AbstractVehicleEntryForm extends PageFlowController {
    
    @Service("VehicleAssessmentService")
    def assessmentService;
    
    @Service("VehicleApplicationService")
    def applicationService;


    @FormTitle
    def formTitle;
    
    def ruleExecutor;
    def entity;
    def vehicletype;
    def editmode = "read";
    
    def apptypes = ["NEW","RENEW"];
    
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

    void save() {
        entity = applicationService.create( entity );
    }
    
    void assess() {
        def p = [:];
        p.putAll( entity );
        p.defaultinfos = p.remove("infos");

        def r = ruleExecutor.execute(p);
        if( !r) {
            throw new BreakException();
        }
        entity.billexpirydate = r.duedate;
        
        if ( entity.fees == null ) {
            entity.fees = []; 
        } else { 
            entity.fees.clear(); 
        }
        if ( entity.infos == null ) {
            entity.infos = []; 
        } else { 
            entity.infos.clear(); 
        }

        if( r.infos ) entity.infos.addAll( r.infos );  
        
        if( r.items ) {
            entity.fees.addAll( r.items );
            entity.amount = entity.fees.sum{ it.amount };
        }

        feeListModel.reload();
        infoListModel.reload();
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

    void viewTrackingno() {
        Modal.show( "show_trackingno", [trackingno: "51010:" + entity.appno ]);
    }

    public Object printApplication() {
        return null; 
    }
    
    public Object printAssessment() {
        return null; 
    }
    
    boolean getFranchiseControlEditable() {
        if( entity.txnmode == 'CAPTURE' ) return true;
        return entity.apptype.matches( 'NEW|CHANGE_OWNER_UNIT' );
    }
    
    boolean getOwnerEditable() {
        if( entity.txnmode == 'CAPTURE' ) return true;
        return entity.apptype.matches( 'NEW|CHANGE_OWNER_UNIT' );
    }
    
    
}