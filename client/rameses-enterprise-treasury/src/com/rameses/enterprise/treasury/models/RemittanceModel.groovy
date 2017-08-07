package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import javax.swing.*;
import com.rameses.io.*;

class RemittanceModel  { 

    @Binding
    def binding;

    @Service("RemittanceService")
    def service;

    @Service("RemittanceImportExportService")
    def exportSvc;

    String title = "Remittance";

    def entity;
    def mode = 'create';
    def todate;
    boolean captureMode = false;
    
    @FormTitle
    public def getTitle() {
        if( captureMode ) {
            return "Capture Remittance";
        }
        else if( mode == "create" ) {
            return "New Remittance";
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
        return InvokerUtil.lookupActions( "remittance:formActions", [entity:entity] );
    }

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
        entity.cashbreakdown = [];
        mode = "create";    
        return "create"        
    }

    def capture() {
        captureMode = true;
        mode = "capture";    
        return "capture"        
    }    

    def open(){
        mode = "read";
        entity = service.open(entity);
        return "view"
    }

    def listModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;

    def checkModel = [
        fetchList: {
            return entity.checks;
        }
    ] as BasicListModel;

    def submit() {
        def breakdown = 0;
        if( entity.cashbreakdown ) {
            breakdown = entity.cashbreakdown.sum{ it.amount };
        }
        if( breakdown != entity.totalcash )
            throw new Exception("Cash breakdown must equal total cash");

        boolean pass = false;
        try {
            def h = { sig->
               pass = true;
               if( !entity.collector ) entity.collector = [:];
               entity.collector.signature = sig;
            }
            def msg = "You are about to submit this remittance.Please ensure the entries are correct";
            Modal.show("verify_submit_with_signature", [handler:h, message: msg] );
        }
        catch(e) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?");
        }
        if( pass == true ) {
            def o = service.post( entity );
            entity.txnno = o.txnno;
            mode = 'read'
            MsgBox.alert("Posting successful. Control No " + entity.txnno);
            return "_close";
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
    
    
} 