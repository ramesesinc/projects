package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleApplicationForm extends WorkflowTaskModel {
    
    @Service("VehicleAssessmentService")
    def assessmentService;
    
    @Service("VehicleFranchiseService")
    def franSvc;
    
    def vehicletype;
    def ruleExecutor;
    
    public def open() {
        def retval = super.open();
        vehicletype = workunit.info.workunit_properties.vehicletype;
        ruleExecutor = new RuleProcessor(  { p-> return assessmentService.assess(p) } );
        return retval;
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

    def paymentListModel = [
        fetchList : {
            return entity.payments;
        },
        onOpenItem: { o->
            return Inv.lookupOpener( "cashreceiptinfo:open", [entity: [objid:o.refid] ] );
        }
    ] as BasicListModel;
    
    void assess() {
        def p = [:];
        p.putAll( entity );
        p.vehicletype = vehicletype;
        p.defaultinfos = p.remove("infos");
        
        def r = ruleExecutor.execute(p);
        if( !r) {
            throw new BreakException();
        }
        entity.billexpirydate = r.duedate;
        if( r.items ) {
            entity.fees = r.items;
            entity.amount = entity.fees.sum{ it.amount };
        }
        if( r.infos ) {
            entity.infos = r.infos;
        }
        else {
            entity.infos = [];
        }
        feeListModel.reload();
        infoListModel.reload();
    }
    
    void viewTrackingno() {
        Modal.show( "show_vehicle_trackingno", [appno: entity.appno] );
    }
 
    boolean isCanIssuePermit() {
        try { 
            def sname = ['vehicle_application', vehicletype].findAll{( it )}.join('_'); 
            Inv.lookupOpener(sname+':permit:create', [entity: entity]); 
            return true; 
        } catch(Throwable t) {
            return false; 
        }
    }
    def createPermit() { 
        def sname = ['vehicle_application', vehicletype].findAll{( it )}.join('_'); 
        return Inv.lookupOpener(sname+':permit:create', [entity: entity]); 
    }
    def openPermit() {
        def sname = ['vehicle_application', vehicletype].findAll{( it )}.join('_'); 
        return Inv.lookupOpener(sname+':permit:open', [entity: entity]); 
    }    
}