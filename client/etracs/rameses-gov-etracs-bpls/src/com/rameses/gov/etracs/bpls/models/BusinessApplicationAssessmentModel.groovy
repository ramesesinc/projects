package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.common.*;
import java.rmi.server.*
import com.rameses.util.*;

class  BusinessApplicationAssessmentModel {
        
    @Binding
    def binding;

    @Caller
    def caller;

    def assessmentInfo;
    def appinfo;

    def appSvc;
    def paymentSvc;

    String title = "Assessment";
    def task;
    def entity;
    String entityName = "business_application:assessment";

    public void load() {
        assessmentInfo = caller.assessmentInfo;
        appinfo = caller.appinfo;

        appSvc = caller.appService;
        paymentSvc = caller.paymentSvc;

        if (!caller.has_loaded_assessment) {
            assessmentInfo.load(); 
            caller.has_loaded_assessment = true;
        } 
        assessmentInfo.analyzer = false;
        assessmentInfo.refresh(); 
    } 

    void refresh() { 
        assessmentInfo.load(); 
        appinfo.refresh(); 
    } 

    def showEditMenu() {
        def h = { t->
            if (t) refresh();

            binding.refresh();
        }
        def list = Inv.lookupOpeners( "business_assessment:change", [entity:entity, handler:h] );
        def pop = new PopupMenuOpener();
        list.each { pop.add( it ) }
        return pop;
    }

    def showPayMenu() {
        def h = {
            assessmentInfo.load();
        }
        def list = Inv.lookupOpeners( "business_assessment:payment", [entity:entity, handler:h] );
        def pop = new PopupMenuOpener();
        list.each {
            pop.add( it );
        }
        return pop;
    }

    def paymentModel = [
        fetchList: { o->
            return paymentSvc.getApplicationPayments([applicationid:entity.objid]);
        }, 
        onOpenItem: { o,col->
            if( o.reftype == 'cashreceipt' ) {
                def op = Inv.lookupOpener( "cashreceiptinfo:open", [entity:[objid: o.refid]] );
                op.target = 'popup';
                return op;
            }  
        }
    ] as BasicListModel;

    void doAdvanceBill() {
        def h = { bd->
            assessmentInfo.runBill( bd );
            assessmentInfo.taxfeeModel.reload();
            paymentModel.reload();   
            binding.refresh();
        }
        Modal.show( "billdate:edit", [handler:h] )
    }

    def printAssessment() { 
        def h = { t-> 
            if (t) refresh(); 

            binding.refresh(); 
        }
        def list = Inv.lookupOpeners("business_application:assessment:print", [entity:entity, handler:h]);
        def pop = new PopupMenuOpener(); 
        pop.executeOnSingleResult = true; 
        list.each { pop.add( it ) }
        return pop;  
    } 

    def preview() {
        return Inv.lookupOpener("business:preassessment:printout", [entity: entity] ); 
    }

    def calculate() {
        assessmentInfo.handler = {
            assessmentInfo.verify(); 

            def m = [:];
            m.putAll(entity); 
            m.businessid = entity.business.objid; 
            appSvc.saveAssessment(m);
            assessmentInfo.load(); 
            assessmentInfo.analyzer = false; 
            assessmentInfo.refresh(); 
        } 
        assessmentInfo.calculate() ;
    }

    def infoModel = [
        fetchList: { o->
            def tmps = []; 
            if ( entity.assessmentinfos ) tmps.addAll( entity.assessmentinfos ); 
            if ( entity.appinfos ) tmps.addAll( entity.appinfos );

            def results = [];                     
            def lobs = tmps.findAll{(it.lob?.objid != null)}.collect{ it.lob.objid }; 
            lobs.unique();                    
            lobs.each{ x-> 
                results.addAll(tmps.findAll{(it.lob?.objid == x)}); 
            } 
            results.addAll(tmps.findAll{(it.lob?.objid == null)}); 
            return results;
        }
    ] as BasicListModel;


    def viewGrossInformation() { 
        return Inv.lookupOpener( "business_application_gross:open", [entity: entity.business] );
    }

    def viewInfoHistory() { 
        return Inv.lookupOpener( "business_application:info_history", [entity: entity.business] );
    }
            
}