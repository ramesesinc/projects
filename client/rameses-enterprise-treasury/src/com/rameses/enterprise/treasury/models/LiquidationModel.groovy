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
    def listModel;
    def selectedItem;
    
    def selectedRemittance;
    def selectedFund;
    
    @FormTitle
    public def getTitle() {
        return entity.txnno;
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
    
    def open(){
        mode = "read";
        entity = service.open(entity);
        return null;
    }

    def remittancesModel = [
        fetchList: { o->return entity.remittances; },
        onOpenItem: { o, col->
            return viewRemittance();
        }
    ] as BasicListModel;

    def fundListModel = [
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
    
    def viewBreakdown() {
        def h = [
            getCashBreakdown : {
                return entity.breakdown;   
            },
            getChecks: {
                return  entity.checks;
            },
            getCreditMemos: {
                return entity.creditmemos;
            }
        ]   
        return Inv.lookupOpener( "cashbreakdown", [entity:entity, editable: false, handler: h ]);
    }
    
    
    def viewFundBreakdown() {
        if(!selectedFund) throw new Exception("please select a fund entry");
        
    }
    
    void post() {
        boolean pass = false;
        try {
            def h = { sig->
               pass = true;
               entity.signature = sig;
            }
            def msg = "You are about to submit this liquidation. Please ensure the entries are correct";
            Modal.show("verify_submit_with_signature", [handler:h, message: msg] );
        }
        catch(e) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?");
        }
        
        if( pass == true) {
            entity = service.post( entity ); 
            MsgBox.alert("Posting successful"); 
            mode = "read"; 
            if ( handler ) handler(); 
        }
    }
    
    def approve() {
        boolean pass = false;
        try {
            def h = { sig->
               pass = true;
               service.approve( [signature:sig, objid:entity.objid ] );
               return "_close";
            }
            def msg = "Please check all entries are correct before approving";
            Modal.show("verify_submit_with_signature", [handler:h, message: msg] );
        }
        catch(e) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?");
            if(pass) {
                service.approve( [objid:entity.objid ] );          
            }
        }
        if(pass) {
            return "_close";
        }
    }
    
    def delete() {
        if (MsgBox.confirm("You are about to delete this transaction. Proceed?")) {
            service.delete([ objid: entity.objid ]); 
            return '_close'; 
        }
        return null; 
    }
    
} 