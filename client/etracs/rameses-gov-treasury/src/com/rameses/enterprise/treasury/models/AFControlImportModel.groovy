package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.io.*;

class AFControlImportModel {
    
    @Service('AFControlExportImportService')
    def svc;
            
    @Binding
    def binding;
        
    String title = 'Import Accountable Forms';
    
    def data;
    def mode;
    
    def MODE_INIT       = 'init';
    def MODE_FORPOSTING = 'forposting';
    def MODE_POSTED     = 'posted';
    
    void init(){
        data = [];
        mode = MODE_INIT;
    }
        
    def listHandler = [
        fetchList: { return data ; }
    ] as BasicListModel;
    
    void doImport(){ 
        def cwin = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
        def jfc = new javax.swing.JFileChooser(); 
        int opt = jfc.showOpenDialog( cwin );
        if ( opt == javax.swing.JFileChooser.APPROVE_OPTION ) {
            data = FileUtil.readObject( jfc.selectedFile );
            listHandler.reload();
            mode = MODE_FORPOSTING;
        }
    }
    
    void post(){
        if (MsgBox.confirm('Post imported controls?')){
            data.each{ ac ->
                try {
                    svc.postImportData( ac ); 
                    ac.status = 'Successfully posted.'
                }
                catch(Throwable e ){
                    ac.status = e.message
                }
            }

            mode = MODE_POSTED;            
            listHandler.reload();
            MsgBox.alert('AF Controls has been successfully posted.');
        }
    }
} 