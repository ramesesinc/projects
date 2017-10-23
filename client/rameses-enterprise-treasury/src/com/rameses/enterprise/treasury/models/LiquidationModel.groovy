package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import javax.swing.*;

class LiquidationModel {

    @Binding
    def binding;

    @Service("LiquidationService")
    def service;

    @Service("LiquidationImportExportService")
    def exportSvc;

    @Service("IncomeSummaryService")
    def incomeSvc;

    String title = "Liquidation";
    def mode = 'initial';    
    def entity;
    def cashBreakdown;
    def selectedItem;
    def selectedCheck;
    def listModel;

    def total = 0.0;
    def breakdown = 0.0;
    def remaining = 0.0;

    def handler;
    def selectedRemittance;

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

    def init() {
        mode = "initial";
        def params = [ txndate: entity.txndate ]; 
        params.capturemode = (entity.capturemode==true); 
        entity = service.init( params ); 
        return mode; 
    }

    def liquidate() {
        init();
        start();
        return "default";
    }
    
    def back() {
        mode = "initial";
        return "initial";
    }

    def start() {
        if( !entity.remittances ) throw new Exception('No items to remit');
        
        mode = "create";
        entity.cashbreakdown = [];
        remaining = entity.totalcash; 
        cashBreakdown = InvokerUtil.lookupOpener("cash:breakdown", [
            entries: entity.cashbreakdown, onupdate: { o->
                breakdown = o; 
                total = entity.totalnoncash + breakdown;
                remaining = entity.totalcash - breakdown; 
                binding.refresh("remaining|breakdown|total");
            } 
        ]); 
        return "default";
    }

    def open(){
        mode = "read";
        entity = service.open(entity);
        cashBreakdown = InvokerUtil.lookupOpener("cash:breakdown", [entries: entity.cashbreakdown, allowEdit:false] );    
        total = entity.totalcash + entity.totalnoncash;
        remaining = 0.0
        breakdown = entity.cashbreakdown.sum{it.amount};
    }

    void submit() {
        if( breakdown != entity.totalcash )
            throw new Exception("Cash breakdown must equal total cash"); 

        if( MsgBox.confirm("You are about to submit this liquidation. Please ensure the entries are correct")) {
            entity = service.post( entity ); 
            MsgBox.alert("Posting successful"); 
            mode = "read"; 
            if ( handler ) handler(); 
        }
    }

    void doExport() {
        def chooser = new JFileChooser();
        chooser.setSelectedFile(new File(entity.txnno + '.liq'));
        int i = chooser.showSaveDialog(null);
        if(i==0) {
            FileUtil.writeObject( chooser.selectedFile, exportSvc.exportLiquidation(entity.objid) );
            MsgBox.alert("Liquidation has been successfully exported!");
        }   
    }


    def remittancesModel = [
        fetchList: { o->return entity.remittances; }
    ] as BasicListModel;

    def fundSummaryModel = [
        fetchList: { o->return entity.fundsummary; }
    ] as EditorListModel;

    def checkModel = [
        fetchList: {
            return entity.checks;
        }
    ] as BasicListModel;

    void postSummary() {
        if( MsgBox.confirm("You are about to post this to income summary. Proceed?")) {
            incomeSvc.post([liquidationid: entity.objid]); 
            entity.posted = 1;   
            MsgBox.alert("Posting successful");
        }
    }

    void doHold() {
        if( !selectedRemittance ) throw new Exception('Please select an item');
        def o = MsgBox.prompt( 'Please enter reason for hold' );
        service.holdRemittance( [objid: selectedRemittance.objid] );
        try { 
            entity = service.init();  
        } 
        catch(ign){
            entity = [remittances:[], fundsummary: [], totalcash:0, totalnoncash:0, amount:0, checks:[]];
        }
        binding.refresh();
        remittancesModel.reload();
        fundSummaryModel.reload();
    }

    def viewRemittance() {
        if( !selectedRemittance ) throw new Exception('Please select an item');
        def op = Inv.lookupOpener( "remittance:reportbyfund", [entity:selectedRemittance] );
        op.target = 'popup';
        op.properties.width = 800;
        op.properties.height = 650;
        return op;
    }

}  
