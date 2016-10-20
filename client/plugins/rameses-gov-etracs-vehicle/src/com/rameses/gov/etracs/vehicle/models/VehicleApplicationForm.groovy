package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleApplicationForm extends CrudFormModel {
    
    @Service("VehicleAssessmentService")
    def assessmentSvc;
    
    @Service("VehicleApplicationService")
    def appSvc;
    
    String title = "Vehicle Registration";
    def selectedItem;
    def txntype;
    
    public String getSchemaName() {
        return "vehicle_application";
    }
    
    public boolean beforePost() { return true;}
    
    def create() {
        def z = super.create();
        entity.apptype = 'NEW';
        entity.txntype = txntype;
        entity.operator = [:];
        entity.info = [:];
        entity.fees = [];
        title += " " + txntype.objid;
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

    void post() {
        if(MsgBox.confirm("You are about to post this entry. Continue?")) {
            if( beforePost() ) {
                def z = appSvc.post(entity);
                MsgBox.alert("Acct No created " + z.acctno);
            }
        }
    }
    
}