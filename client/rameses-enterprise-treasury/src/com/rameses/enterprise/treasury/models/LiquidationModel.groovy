package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;


class LiquidationModel  { 

    @Binding
    def binding;

    @Service("LiquidationService")
    def service;

    @Service("LiquidationImportExportService")
    def exportSvc;

    @Service("IncomeSummaryService")
    def incomeSvc;

    String title = "Liquidation";
    def entity;
    def mode = 'initial';
    def handler;
    def listModel;
    def selectedItem;
    def selectedCheck;
    def selectedRemittance;
    
    @FormTitle
    public def getTitle() {
        if( mode == "create" ) {
            return "New Liquidation";
        }
        else {
            return entity.txnno;
        }
    }

    @FormId
    public def getFormId() {
        return entity.objid;
    }

    def getExtActions() {
        return InvokerUtil.lookupActions( "liquidation:formActions", [entity:entity] );
    }

    def popupReports(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }

     //whats bad about this is that the report is located in etracs treasuty gov.
    def preview() {
        return InvokerUtil.lookupOpener( "liquidation:rcd", [entity:entity] );
    }
    
    def create() {
        entity = service.init();
        entity.cashbreakdown = [];
        mode = "create";
        return "create";
    }
    
    def open(){
        mode = "read";
        entity = service.open(entity);
        return "view";
    }

    def remittancesModel = [
        fetchList: { o->return entity.remittances; },
        onOpenItem: { o, col->
            return viewRemittance();
        }
    ] as BasicListModel;

    def checkModel = [
        fetchList: {
            return entity.checks;
        }
    ] as BasicListModel;
    
    def fundSummaryModel = [
        fetchList: {
            return entity.fundsummary;
        }
    ] as BasicListModel;
    
    def viewRemittance() {
        if( !selectedRemittance ) throw new Exception('Please select an item');
        //def rem = remittanceSvc.open( [objid:selectedRemittance.objid ] );
        def op = Inv.lookupOpener( "remittance:open", [entity:selectedRemittance] );
        op.target = 'popup';
        return op;
    }
    
    void post() {
        boolean pass = false;
        try {
            def h = { sig->
               pass = true;
               entity.signature = sig;
            }
            if ( com.rameses.rcp.sigid.SigIdDeviceManager.getProvider()?.test()) {
                def msg = "You are about to submit this liquidation. Please ensure the entries are correct";
                Modal.show("verify_submit_with_signature", [handler: h, message: msg ]);
            }
        } catch(Throwable t) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?");
        } 
        
        if( pass ) {
            entity = service.post( entity ); 
            MsgBox.alert("Posting successful"); 
            mode = "read"; 
            if ( handler ) handler(); 
        } 
    } 
    
    def approve() {
        boolean pass = false;
        def signature = null;
        try {
            def h = { sig->
               pass = true;
               signature = sig;
               return "_close";
            }
            if ( com.rameses.rcp.sigid.SigIdDeviceManager.getProvider()?.test()) {
                def msg = "Please check all entries are correct before approving";
                Modal.show("verify_submit_with_signature", [handler:h, message: msg] );
            }
        } catch( Throwable t) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?");
        }
        
        if ( pass ) {
            service.approve([ objid: entity.objid, signature: signature ]);
            return "_close";
        }
        return null; 
    }
    
    def delete() {
        if (MsgBox.confirm("You are about to delete this transaction. Proceed?")) {
            service.delete([ objid: entity.objid ]); 
            return '_close'; 
        }
        return null; 
    }
} 