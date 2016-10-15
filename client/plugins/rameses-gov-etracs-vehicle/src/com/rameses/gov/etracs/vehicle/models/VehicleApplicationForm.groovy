package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleApplicationForm extends CrudFormModel {
    
    @Service("VehicleAssessmentService")
    def assessmentSvc;
    
    String title = "Vehicle Registration";
    def selectedItem;
    
    def create() {
        def z = super.create();
        entity.apptype = 'NEW';
        entity.txntype = 'MTOP';
        entity.apptype = 'NEW';
        entity.operator = [:];
        entity.fees = [];
        return z;
    }
    
    void assess() {
        def r = assessmentSvc.assess(entity);
        entity.fees = r.items;
        entity.amount = entity.fees.sum{ it.amount };
        feeListModel.reload();
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
}