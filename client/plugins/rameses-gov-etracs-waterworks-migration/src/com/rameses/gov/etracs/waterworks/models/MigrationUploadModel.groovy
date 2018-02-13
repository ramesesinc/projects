package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class MigrationUploadModel {

    @Binding 
    def binding;
    
    def options;
    def totalrows;
    def currentrow;
    def initHandler;
    def stopHandler;
    def startHandler;
    
    def mode;
    boolean cancelled;
    
    void init() {
        mode = 'init';
        options = [:];
        options.refresh = { 
            binding.refresh('progressvalue');  
        }
        options.finish = {
            mode = 'finish'; 
            binding.refresh();  
            binding.fireNavigation('_close'); 
        } 
        if ( initHandler ) {
            initHandler( options ); 
        }
    }
    
    def getProgressvalue() { 
        if ( cancelled ) {
            return ""; 
        } else if ( mode == 'finish' ) {
            return ""; 
        } else {
            return options?.progressvalue; 
        }
    }
    def getLabel() {
        if ( cancelled ) {
            return 'Operation cancelled.';
        } else if( mode == 'init' ) {
            return "Press Start to begin";
        } else if ( mode == 'upload' ) {
            return "Uploading Data..."; 
        } else if ( mode == 'error' ) {
            return "Processed with errors..."; 
        } else if ( mode == 'finish' ) {
            return "Successfully uploaded"; 
        } else {
            return "";
        }
    }
    
    def doClose() {
        return '_close'; 
    }
    void doStart() { 
        cancelled = false; 
        def proc = { 
            if( startHandler ) { 
                try { 
                    startHandler(); 
                } catch(e) {
                    mode = 'error';
                    MsgBox.err( e ); 
                }
            }
        } as Runnable;
        
        def thread = new Thread( proc );
        mode = 'upload'; 
        thread.start();
    } 
    void doStop() {
        mode = 'init';
        cancelled = true; 
        if ( stopHandler ) {
            stopHandler(); 
        }
    }
}