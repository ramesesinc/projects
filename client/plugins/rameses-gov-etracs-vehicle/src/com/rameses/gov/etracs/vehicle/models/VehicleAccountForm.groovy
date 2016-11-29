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

public abstract class VehicleAccountForm extends CrudFormModel {
    
    String title = "Vehicle Account";
    def selectedItem;
    
    public abstract String getVehicletype();

    def create() {
        super.create();
        entity.vehicletype = vehicletype;
        entity.owner = [:];
        entity.fees = [];
        return entity;
    }
    
    def addItem() {
        def h = { o->
            def m = [_schemaname: 'vehicle_account_fee'];
            m.item = o.revenueitem;
            m.amount = o.amount;
            m.amtpaid = 0;
            m.vehicle = [objid: entity.objid];
            m.txntype = "fee";
            m.sortorder = 100;
            persistenceService.create( m );
            feeListModel.reload();
        }
        Modal.show("revenueitem_entry:create", [handler: h ] );
    }
    
    def feeListModel = [
        fetchList: { o->
            def m = [_schemaname: 'vehicle_account_fee'];
            m.findBy = [ vehicleid: entity.objid ]; 
            //m.where = ["amount-amtpaid > 0"];
            return queryService.getList(m); 
        }
    ] as BasicListModel;
    
    def appHistoryModel = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
    def violationListModel = [
        fetchList: { o->
            return entity.violations;
        }
    ] as BasicListModel;

    public def assignControl() {
        def s = { o->
            entity.control = o;
            binding.refresh();
        }
        return Inv.lookupOpener( "vehicle_control_" + vehicletype + ":available:lookup", [onselect:s] );
    }
    
}