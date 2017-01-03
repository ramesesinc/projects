package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class NewOboApplicationModel extends PageFlowController{
    
    @Service("OboAssessmentService")
    def assessmentService;
    
    @Service("OboApplicationService")
    def applicationService;

    @FormTitle
    def formTitle;
    
    def ruleExecutor;
    def entity;
    def editmode = "read";
    
    def apptypes = ["NEW","RENEW"];
    
    void setUp() {
        formTitle = workunit.info.workunit_properties.title;
        if(!formTitle) formTitle = getTitle();
        vehicletype = workunit?.info?.workunit_properties?.vehicletype;
        ruleExecutor = new RuleProcessor(  { p-> return assessmentService.assess(p) } );
        entity = [:];
        entity.apptype = workunit?.info?.workunit_properties?.apptype;
        entity.txnmode = "ONLINE";
        entity.vehicletype = vehicletype;
    }

    void afterLoad() {
        
    }
        
    void save() {
        entity = applicationService.create( entity );
    }
    
    void assess() {
        def p = [:];
        p.putAll( entity );
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

    def getLookupAvailableFranchise() {
        return Inv.lookupOpener( "vehicle_franchise_" + vehicletype + ":available:lookup" );
    }

    void viewTrackingno() {
        Modal.show( "show_trackingno", [trackingno: "51010:" + entity.appno ]);
    }
    
    
    
}