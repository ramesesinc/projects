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
    
    def selectedFee;
    
    public String getTitle() {
        return entity.controlno ;
    }
    
    public String getWindowTitle() {
        return entity.controlno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    def addItem() {
        def h = { o->
            def m = [_schemaname: 'vehicle_franchise_fee'];
            m.item = o.revenueitem;
            m.amount = o.amount;
            m.amtpaid = 0;
            m.vehicle = [objid: entity.objid];
            m.txntype = "fee";
            m.sortorder = 100;
            m.parentid = entity.objid;
            m.remarks = o.remarks;
            persistenceService.create( m );
            feeListModel.reload();
        }
        Modal.show("revenueitem_entry:create", [handler: h ] );
    }
    
    def removeItem() {
        if(!selectedFee) throw new Exception("select an item first");
        if(selectedFee.amtpaid) 
            throw new Exception("Cannot remove an item where there is already amount paid");
        if(selectedFee.ledgertype == 'application') 
            throw new Exception("Cannot remove application fee");
        def m = [_schemaname: 'vehicle_franchise_fee'];
        m.objid = selectedFee.objid;
        persistenceService.removeEntity( m );
        feeListModel.reload();
    }
    
    def feeListModel = [
        fetchList: { o->
            def m = [_schemaname: 'vehicle_fee'];
            m.findBy = [ controlid: entity.objid ]; 
            m.where = [" amount - amtpaid > 0"];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    def appListModel = [
        fetchList: { o->
            def m = [_schemaname:'vehicle_application_'+entity.vehicletype];
            m.findBy = [ controlid: entity.objid ];
            m.orderBy = "dtfiled DESC";
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    def violationListModel = [
        fetchList: { o->
            return entity.violations;
        }
    ] as BasicListModel;

    
}