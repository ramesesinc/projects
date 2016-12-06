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
    def vehicleTypeHandler;
    def editmode = "read";
    
    void setUp() {
        formTitle = workunit.info.workunit_properties.title;
        if(!formTitle) formTitle = getTitle();
        vehicletype = workunit?.info?.workunit_properties?.vehicletype;
        ruleExecutor = new RuleProcessor(  { p-> return assessmentService.assess(p) } );
        entity = [:];
        entity.apptype = workunit?.info?.workunit_properties?.apptype;
        entity.vehicletype = vehicletype;
    }

    void afterLoad() {
        vehicleTypeHandler = Inv.lookupOpener("vehicle_type_handler:"+vehicletype, [entity:entity]);
    }
        
    def addVehicle() {
        def h = { o->
            entity.vehicle = o;
            entity.vehicleid = o.objid;
            binding.refresh();
        }
        if( entity.vehicle?.objid  ) {
            def op = Inv.lookupOpener("vehicle_" + vehicletype + ":open", [handler: h, entity:entity.vehicle ] );
            op.target = "popup";
            return op;
        }
        else {
            return Inv.lookupOpener("vehicle_" + vehicletype + ":create", [handler: h ] )
        }
    }

    void save() {
        entity = applicationService.create( entity );
        MsgBox.alert( "Application No. " + entity.appno + " was created successfully." );
    }
    
    void assess() {
        if(! entity.vehicle?.objid )
            throw new Exception("Please specify vehicle");
        
        def p = [:];
        p.putAll( entity );
        p.defaultinfos = p.remove("infos");

        def r = ruleExecutor.execute(p);
        if( !r) {
            throw new BreakException();
        }
        entity.franchise.expirydate = r.duedate;
        if( r.items ) {
            entity.fees = r.items;
            entity.amount = entity.fees.sum{ it.amount };
            feeListModel.reload();
        }
        if( r.infos ) {
            entity.infos = r.infos;
            infoListModel.reload();
        }
    }
    
    
    def infoListModel = [
        fetchList: { o->
            return entity.infos;
        }
    ] as BasicListModel;

    def getLookupAvailableFranchise() {
        return Inv.lookupOpener( "vehicle_franchise_" + vehicletype + ":available:lookup" );
    }
    

}