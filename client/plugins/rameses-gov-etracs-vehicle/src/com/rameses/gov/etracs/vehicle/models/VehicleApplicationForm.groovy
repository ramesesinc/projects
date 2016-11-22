package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.models.*;

public abstract class VehicleApplicationForm extends CrudFormModel {
    
    @Service("VehicleApplicationService")
    def appSvc;
    
    @Caller
    def caller;
    
    def ruleExecutor;
    def selectedItem;
    public abstract String getVehicletype();
    
    public String getTitle() {
        return vehicletype.title + " (" + entity.apptype + ")";
    }
    
    void afterInit() {
        ruleExecutor = new RuleExecutor(  { p-> return appSvc.assess(p) } );
    }
    
    def create() {
        super.create();
        entity.apptype = 'NEW';
        entity.vehicletype = getVehicletype();
        entity.owner = [:];
        entity.fees = [];
        return entity;
    }
    
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
    
    void assess() {
        def r = ruleExecutor.execute(entity);
        if( !r) return;
        entity.fees = r.items;
        entity.amount = entity.fees.sum{ it.amount };
        feeListModel.reload();
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;

    void changeOwner() {
        
    }
    
    void changeMotor() {
        
    }

    public def viewBilling() {
        
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