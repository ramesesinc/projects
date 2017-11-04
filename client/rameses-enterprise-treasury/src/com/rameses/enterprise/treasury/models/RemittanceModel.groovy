package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.io.*;
import java.rmi.server.*;
import javax.swing.*;

class RemittanceModel {

    @Binding
    def binding;

    @Service("RemittanceService")
    def service;

    @Service("RemittanceImportExportService")
    def exportSvc;

    String title = "Remittance";

    def entity;
    def cashBreakdown;
    def mode = 'create';

    def total = 0.0;
    def breakdown = 0.0;
    def remaining = 0.0;
    
    def remittancedate; 
    
    private boolean capturemode; 

    def getExtActions() {
        return InvokerUtil.lookupActions( "remittance:formActions", [entity:entity] );
    }

    def popupReports(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
    
    void create() { 
        mode = "initial"; 
        
        def p = [:]; 
        if ( capturemode ) { 
            p.remittancedate = remittancedate; 
            p.capturemode = true; 
        } 
        entity = service.init( p ); 
    }
    
    def capture() { 
        def p = [ pass: false ]; 
        p.title = 'Enter Remittance Date:';
        p.handler = { o-> 
            p.remittancedate = o; 
            p.pass = true; 
        } 
        Modal.show('date:prompt', p); 
        if ( !p.pass ) return '_close'; 
        
        remittancedate = p.remittancedate; 
        capturemode = true; 
        create(); 
        return "default";
    } 

    def back() {
        mode = "initial"
        return "default"
    }    

    def start() {
        calculateCashBreakdown(); 
        if ( cashBreakdown == null ) {
            cashBreakdown = InvokerUtil.lookupOpener("cash:breakdown", [
                entries: entity.cashbreakdown, 
                onupdate: { o-> 
                    breakdown = o; 
                    total = entity.totalnoncash + breakdown;
                    remaining = entity.totalcash - breakdown;
                    binding.refresh("breakdown|total|remaining");
                }
            ]);
        }
        mode = "create";    
        return "main";
    }

    def open() { 
        mode = "read"; 
        entity = service.open( entity ); 
        calculateCashBreakdown(); 
        cashBreakdown = InvokerUtil.lookupOpener("cash:breakdown", [ entries: entity.cashbreakdown ]);    
        total = entity.totalcash + entity.totalnoncash;
        return "main"
    }

    void calculateCashBreakdown() {
        if ( entity.cashbreakdown == null ) {
            entity.cashbreakdown = []; 
        } 
        
        def breakdownamt = 0.0; 
        entity.cashbreakdown.each{ 
            breakdownamt += (it.amount ? it.amount : 0.0); 
        } 
        
        def totalcash = (entity.totalcash ? entity.totalcash : 0.0); 
        remaining = totalcash - breakdownamt; 
        breakdown = breakdownamt; 
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

    void submit() {
        if( breakdown != entity.totalcash )
            throw new Exception("Cash breakdown must equal total cash");

        if( MsgBox.confirm("You are about to submit this remittance. Please ensure the entries are correct")) {
            def o = service.post( entity );
            entity.txnno = o.txnno;
            mode = 'read'
            entity = o;
            MsgBox.alert("Posting successful");
        }
    }


    void doExport(){
        def chooser = new JFileChooser();
        chooser.setSelectedFile(new File(entity.txnno + '.rem'));
        int i = chooser.showSaveDialog(null);
        if(i==0) {
            FileUtil.writeObject( chooser.selectedFile, exportSvc.exportRemittance(entity.objid) );
            MsgBox.alert("Remittance has been successfully exported!");
        }   
    }

}    