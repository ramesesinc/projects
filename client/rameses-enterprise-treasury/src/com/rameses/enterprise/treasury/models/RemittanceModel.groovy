package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import javax.swing.*;
import com.rameses.io.*;
import com.rameses.seti2.models.*;

class RemittanceModel  extends CrudFormModel { 

    @Service("RemittanceService")
    def service;

    @Service("RemittanceImportExportService")
    def exportSvc;
    
    def todate;
    boolean captureMode = false;
    def selectedFund;

    final def numFormatter = new java.text.DecimalFormat("#,##0.00"); 
    
    String schemaName = "remittance";
    
    @FormTitle
    public String getTitle() {
        if( captureMode ) {
            return "Capture Remittance";
        }
        else if( mode == "create" ) {
            return "New Remittance";
        }
        else {
            return "Remittance " + entity.txnno;
        }
    }

    @FormId
    public String getFormId() {
        return entity.objid;
    }

    /*
    def getExtActions() {
        return InvokerUtil.lookupActions( "remittance:formActions", [entity:entity] );
    }
    */

    //whats bad about this is that the report is located in etracs treasuty gov.
    def print() {
        return InvokerUtil.lookupOpener( "remittance:rcd", [entity:entity] );
    }
    
    def popupReports(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }

    def create() {
        mode = "initial";
        entity = service.init( [todate: todate ] );  
        mode = "create";    
        return "create";
    }

    def capture() {
        captureMode = true;
        mode = "capture";    
        return "capture"; 
    }    

    def remittanceFundModel = [
        fetchList: { o->
            return entity.funds;
            //return service.getRemittanceFunds([remittanceid: entity.objid]);
        },
        onOpenItem: { item, colName->
            return openFundBreakdown(item);
        }
    ] as BasicListModel;
    
    //MAIN BREAKDOWN
    def viewBreakdown() {
        def h = [
            getChecks: {
                return  service.getRemittanceChecks([remittanceid: entity.objid]);
            },
            getCreditMemos: {
                return service.getRemittanceCreditMemos([remittanceid: entity.objid]);   
            }
        ]   
        return Inv.lookupOpener( "cashbreakdown", [entity:entity, editable: false, handler: h ]);
    }
    
    //called from the menu
    def openFundBreakdown() {
        if(!selectedFund) throw new Exception("Please choose a fund");
        return Inv.lookupOpener("remittance_fund:open", [entity: selectedFund] )
    }
   
    def submit() {
        boolean pass = false;
        
        try {
            def h = { sig->
               pass = true;
               if( !entity.collector ) entity.collector = [:];
               entity.collector.signature = sig;
            }
            if ( com.rameses.rcp.sigid.SigIdDeviceManager.getProvider()?.test()) {
                def msg = "You are about to submit this remittance.Please ensure the entries are correct";
                Modal.show("verify_submit_with_signature", [handler: h, message: msg ]);
            }
        } catch( Throwable t ) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?"); 
        } 
        
        if( pass ) { 
            def o = service.post([ 
                remittanceid: entity.objid, 
                collector: entity.collector, 
                cashbreakdown: entity.cashbreakdown                 
            ]);
            entity.txnno = o.txnno;
            MsgBox.alert("Posting successful. Control No " + entity.txnno);
            return "_close";
        } 
        return null; 
    }

    def approve() {
        boolean pass = MsgBox.confirm("You are about to accept this remittance. Proceed?");
        if ( pass ) { 
            service.approve([ objid:entity.objid ]);
            return "_close"; 
        } 
        return null;
    }
    
} 