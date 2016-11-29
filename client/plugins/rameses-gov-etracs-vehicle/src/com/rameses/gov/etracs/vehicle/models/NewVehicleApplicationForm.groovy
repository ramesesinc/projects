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

public class NewVehicleApplicationForm extends PageFlowController {
    
    @Service("PersistenceService")
    def persistenceService;
    
    @Service("VehicleAssessmentService")
    def appSvc;

    def ruleExecutor;
    def selectedItem;
    def entity;
    def vehicletype;
    def schemaName;
    def vehicleTypeHandler;
    
    void create() {
        vehicletype = workunit?.info?.workunit_properties?.vehicletype;
        schemaName = workunit?.info?.workunit_properties?.schemaName;
        ruleExecutor = new RuleProcessor(  { p-> return appSvc.assess(p) } );
        entity = [:];
        entity.apptype = workunit?.info?.workunit_properties?.apptype;
        entity.vehicletype = vehicletype;
        entity.owner = [:];
        entity.fees = [];
        entity.requirements = [];
        entity.infos = [];
        vehicleTypeHandler = Inv.lookupOpener("vehicle_type_handler:"+vehicletype, [entity:entity]);
    }
    
    def addVehicle() {
        def h = { o->
            entity.vehicle = o;
            binding.refresh();
        }
        return Inv.lookupOpener("vehicle_account_" + vehicletype + ":lookupnew", [handler: h ] )
    }

    void save() {
        entity._schemaname = schemaName;    
        entity = persistenceService.create( entity );
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

    def renew() {
        boolean pass = false;
        def h = { o->
            pass = true;
            entity = appSvc.loadForApplication( [objid: o.objid, txntype: txntype] );
            entity.apptype= 'RENEW';
            super.init();
        }
        Modal.show( "vehicle_account_" + getVehicletype() + "_for_renew:lookup", [onselect: h] );
        if(!pass) throw new BreakException();
    }
    
    public boolean beforePost() {
        boolean pass = false;
        def h = { o->
            if(o.currentacctid)
                throw new Exception("This control is not available");
            entity.control = o;
            pass = true;
        }
        Modal.show( "vehicle_control:available:lookup", [onselect:h, txntypeid:entity.txntype] );
        if( pass ) {
            pass = false;
            h = { o->
                entity.expirydate = o;
                pass = true;
            }
            Modal.show( "date:prompt", [handler:h, title:'Enter Franchise Expiry date'] );
        }
        return pass;
    }
    

}