package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

public class VehicleApplicationForm extends CrudFormModel {
    
    @Service("VehicleAssessmentService")
    def assessmentSvc;
    
    @Service("VehicleApplicationService")
    def appSvc;
    
    @Caller
    def caller;
    
    def selectedItem;
    def txntype;
    
    public String getTitle() {
        return txntype.title + " (" + entity.apptype + ")";
    }
    
    def create() {
        super.create();
        entity.apptype = 'NEW';
        if( txntype==null ) {
            txntype = caller.txntype;
        }
        if(!txntype)
            throw new Exception("txntype is required");
        entity.txntype = txntype.objid;
        entity.owner = [:];
        entity.fees = [];
        return entity;
    }
    
    def renew() {
        boolean pass = false;
        def h = { o->
            pass = true;
        }
        Modal.show( "vehicle_account_" + txntype.uihandler+ ":lookup", [onselect: h] );
        if(!pass) throw new BreakException();
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