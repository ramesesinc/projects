package com.rameses.common.treasury.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ProgressPanelModel extends ComponentBean  {

    @Binding 
    def binding;
    
    def handler; 
    
    def evt = [
        onRefresh: { stat-> 
            binding.refresh('label|progressvalue|doStart|doStop'); 
        }, 
        onError: { stat-> 
            binding.refresh('label|progressvalue|doStart|doStop'); 
        }, 
        onFinished: {
            binding.refresh('label|progressvalue|doStart|doStop'); 
        } 
    ] as BatchProcessingHandler; 
    
    void setHandler( h ) {
        if ( h instanceof BatchProcessingModel ) {
            this.handler = h; 
            this.handler.add( evt )
        } else {
            throw new Exception('handler must be an instance of BatchProcessingModel');  
        }
    }
        
    void setInit( x ) {
    }
    
    def getProgressvalue() { 
        if ( handler == null ) return "";
        
        if ( handler.status.mode == 'processing' ) {
            return handler.status.progressValue; 
        } else {
            return ""; 
        }
    }
    def getLabel() {
        return (handler ? handler.status.label : ""); 
    }
    def getMode() {
        return (handler ? handler.status.mode : ""); 
    }
    
    def doClose() {
        return '_close'; 
    }
    void doStart() { 
        if ( handler ) {
            handler.start(); 
        }
    } 
    void doStop() {
        if ( handler ) { 
            handler.cancel(); 
        }
    }
}