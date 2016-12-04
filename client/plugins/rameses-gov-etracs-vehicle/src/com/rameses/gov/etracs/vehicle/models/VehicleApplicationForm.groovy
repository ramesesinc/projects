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

public class VehicleApplicationForm extends WorkflowTaskModel {
    
    @Service("VehicleFranchiseService")
    def franSvc;
    
    def vehicletype;
    def vehicleTypeHandler;
    
    void afterOpen() {
        vehicletype = workunit.info.workunit_properties.vehicletype;
        vehicleTypeHandler = Inv.lookupOpener("vehicle_type_handler:"+vehicletype, [entity:entity]); 
        
        //load the fees
        def m = [_schemaname: 'vehicle_application_fee'];
        m.findBy = [appid: entity.objid];
        entity.fees = queryService.getList(m);
        if(entity.fees) {
            entity.amount = entity.fees.sum{ it.amount - it.amtpaid };
        }
    }
    
    String getFormName() {
        return getSchemaName() + ":form";
    }
    
    String getBarcodeFieldname() {
        return "appno";
    }
    
    public String getTitle() {
        return entity.appno + " - " + task?.title;
    }
    
    public String getWindowTitle() {
        return entity.appno;
    }
    
    public String getFormId() {
        return entity.objid;
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
 
    
    def assignFranchise() {
        boolean pass = false;
        def h = { o->
            def z = [:];
            z.appid = entity.objid;
            z.controlid = o.objid;
            entity.franchise = franSvc.assign(z);
            binding.refresh();
        }
        def n = "vehicle_franchise_" + vehicletype + ":available:lookup";
        return Inv.lookupOpener( n, [onselect:h] );
    }
    
}