package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.io.*;

class AFControlExportModel {
    
    @Binding
    def binding;
    
    @Service('AFControlExportImportService')
    def svc;
            
    String title = 'Export Accountable Forms'
    
    def entity = [:];
    def selectedItem;
    def selectedExportItem;
    
    def aftypes = [];
    def afcontrols;
    def exportedcontrols;
    
    @PropertyChangeListener
    def listener = [
        "entity.formno": { 
            loadAfControls() 
        }
    ];    

    void init(){
        afcontrols = [];
        exportedcontrols = [];
        aftypes = svc.getAFTypes(); 
    }
    
    def listHandler = [
        fetchList: { return afcontrols; }
    ] as BasicListModel;
    
    def exportListHandler = [
        fetchList  : { return exportedcontrols; }
    ] as BasicListModel;
    
    void loadAfControls(){
        afcontrols = []; 
        if ( entity.formno?.objid ) { 
            def list = svc.getOpenList([ formno: entity.formno.objid ]); 
            list.each{ o-> 
                if ( o.active == 1 || o.txnmode == 'REMOTE' ) return; 
                if ( exportedcontrols.find{( it.objid == o.objid  )} == null ) {
                    afcontrols << o; 
                }
            }
        } 
        listHandler.reload();
    }
    
    void addItem(){
        if ( selectedItem ) { 
            def o = exportedcontrols.find{( it.objid == selectedItem.objid )}
            if ( o ) exportedcontrols.remove( o ); 
            
            o = afcontrols.find{( it.objid == selectedItem.objid )} 
            if ( o ) afcontrols.remove( o ); 
            
            selectedItem.formno = entity.formno.objid;
            exportedcontrols << selectedItem;
            
            listHandler.reload();
            exportListHandler.reload();
        }
    }
    
    void removeExportItem(){
        if ( selectedExportItem ) { 
            def o = exportedcontrols.find{( it.objid == selectedExportItem.objid )}
            if ( o ) exportedcontrols.remove( o ); 
            
            if ( o.formno == entity.formno?.objid ) {
                afcontrols << o;    
            }             
            listHandler.reload();
            exportListHandler.reload();
        }
    }

    void doExport(){
        def cwin = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
        def jfc = new javax.swing.JFileChooser(); 
        int opt = jfc.showSaveDialog( cwin );
        if ( opt == javax.swing.JFileChooser.APPROVE_OPTION ) {
            FileUtil.writeObject( jfc.selectedFile, buildExportedControls());
            MsgBox.alert("Data has been successfully exported!");
        }
    }
    
    def buildExportedControls(){
        def list = [];
        exportedcontrols.each{
            list  << svc.buildExportData([ objid: it.objid ]);
        } 
        return list;
    }
} 