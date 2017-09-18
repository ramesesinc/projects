package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;


class LiquidationModel extends CrudFormModel { 

    @Service("LiquidationService")
    def service;

    @Service("IncomeSummaryService")
    def incomeSvc;
    
    @Caller 
    def caller; 

    String title = "Liquidation";
    String schemaName = "liquidation";
    
    def handler;
    def listModel;
    def selectedItem;
    def selectedRemittance;
    def selectedFund;
    
    @FormTitle
    public String getTitle() { 
        if( entity?.state.toString().equalsIgnoreCase('DRAFT') ) {
            return "New Liquidation"; 
        } else { 
            return "Liquidation " + entity.txnno;
        } 
    }

    @FormId
    public String getFormId() {
        return entity.objid;
    }

    /*
    def getExtActions() {
        return InvokerUtil.lookupActions( "liquidation:formActions", [entity:entity] );
    }
    */

    public String getPrintFormName() {
        return "remittance";
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
     
    def print() {
        return InvokerUtil.lookupOpener( "liquidation:rcd", [entity:entity] );
    }
    
    public void afterCreate() { 
        entity = service.init();  
    }     

    def fundListModel = [
        fetchList: {
            return entity.funds;
        }
    ] as BasicListModel;
    
    def viewBreakdown() {
        def h = [
            getCashBreakdown : {
                return entity.breakdown;   
            },
            getChecks: {
                return  entity.payments.findAll{ it.reftype == 'CHECK' };
            },
            getCreditMemos: {
                return entity.payments.findAll{ it.reftype != 'CHECK' };
            }
        ];
        return Inv.lookupOpener( "cashbreakdown", [entity:entity, editable: false, handler: h ]);
    }
    
    def viewFundBreakdown() {
        return Inv.lookupOpener( "liquidation_fund:open", [entity:selectedFund ]);
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
            
            try {
                if (caller) caller?.reload(); 
            } catch(Throwable t){;}
            
            if ( handler ) handler(); 
        } 
    } 
    
    def approve() {
        def pass = MsgBox.confirm("You are about to submit this transaction for deposit. Proceed?");
        if ( pass ) {
            service.approve([ objid: entity.objid ]);
            try { 
                return "_close"; 
            } finally {
                try {
                    if (caller) caller?.reload(); 
                } catch(Throwable t){;}
            }
        }
        return null;
    }
    
    /*
    def delete() {
        if (MsgBox.confirm("You are about to delete this transaction. Proceed?")) {
            persistenceSvc.removeEntity([ _schemaname:schemaName, objid: entity.objid ]); 
            try { 
                return "_close"; 
            } finally {
                try {
                    if (caller) caller?.reload(); 
                } catch(Throwable t){;}
            }
        }
        return null; 
    }
    */ 
    
} 