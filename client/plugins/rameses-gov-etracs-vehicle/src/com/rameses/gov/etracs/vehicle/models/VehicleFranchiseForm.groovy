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

public class VehicleFranchiseForm extends CrudFormModel {
    
    def vehicletype;
    def vehicleTypeHandler;
    
    void afterOpen() {
        vehicletype = workunit.info.workunit_properties.vehicletype;
        if(!vehicletype) vehicletype = entity.vehicletype;
        if(!vehicletype) throw new Exception("Please specify vehicle type");
        vehicleTypeHandler = Inv.lookupOpener("vehicle_type_handler:"+vehicletype, [entity:entity]);
    }
    
    public String getTitle() {
        return entity.controlno ;
    }
    
    public String getWindowTitle() {
        return entity.controlno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    def histListModel = [
        fetchList: { o->
            return [];
            //return entity.fees;
        }
    ] as BasicListModel;
    
    
    def addItem() {
        def h = { o->
            def m = [_schemaname: 'vehicle_franchise_fee'];
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
            def m = [_schemaname: 'vehicle_franchise_fee'];
            m.findBy = [ parentid: entity.objid ]; 
            //m.where = ["amount-amtpaid > 0"];
            return queryService.getList(m); 
        }
    ] as BasicListModel;
    
    def appListModel = [
        fetchList: { o->
            def m = [_schemaname:'vehicle_application'];
            m.findBy = [ controlid: entity.objid ];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    def violationListModel = [
        fetchList: { o->
            return entity.violations;
        }
    ] as BasicListModel;

    
}