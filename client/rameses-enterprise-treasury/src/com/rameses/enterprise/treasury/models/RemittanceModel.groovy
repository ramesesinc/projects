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
        loadCashBreakdownOpener();
        mode = "create";    
        return "main";
    }
    
    void loadCashBreakdownOpener() {
        if ( cashBreakdown == null ) {
            def params = [:]; 
            params.entries = entity.cashbreakdown;
            params.allowEdit = ( mode == 'read' ? false : true );             
            if ( params.allowEdit ) {
                params.onupdate = { o-> 
                    breakdown = o; 
                    total = entity.totalnoncash + breakdown;
                    remaining = entity.totalcash - breakdown;
                    binding.refresh("breakdown|total|remaining");
                }
            }
            cashBreakdown = Inv.lookupOpener("cash:breakdown", params);
        }        
    }

    def open() { 
        mode = "read"; 
        entity = service.open( entity ); 
        calculateCashBreakdown();         
        loadCashBreakdownOpener(); 
        total = entity.totalcash + entity.totalnoncash;
        return "main";
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
            mode = 'read';
            entity = o;
            entity.txnno = o.txnno;
            MsgBox.alert("Posting successful");            
            
            cashBreakdown = null; 
            loadCashBreakdownOpener();            
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

    def popupReports(def inv) { 
        return showMenus( inv, null ); 
    } 
    
    def showModifyMenus( def inv ) { 
        def p = [:]; 
        p.handler = { o-> 
            binding.refresh(); 
        }
        return showMenus( inv, p ); 
    }
    
    def showMenus( def inv, def param ) { 
        if ( param == null ) param = [:]; 
        
        param.entity = entity; 
        
        def popupMenu = new PopupMenuOpener();
        try { 
            def list = Inv.lookupOpeners( inv.properties.category, param );
            list.each { 
                popupMenu.add( it ); 
            } 
        } catch(Throwable ign) {
            // just ignore the errors 
        } 
        return popupMenu;
    }
}    