package wtemplates;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ProgressModel {

    @Binding 
    def binding;
    
    def options;
    def initHandler;
    def stopHandler;
    def startHandler;
    
    def mode;
    boolean cancelled;
        
    void init() {
        mode = 'init';
        options = [:];
        options.refresh = { 
            Number value = (options.value ? options.value : 0); 
            Number maxvalue = options.maxvalue; 
            Number num = (value.doubleValue() / maxvalue.doubleValue()) * 100.0; 
            options.progressvalue = (''+ num.intValue() +'%'); 
            binding.refresh('progressvalue|label'); 
        }
        options.finish = { 
            mode = 'finish'; 
            binding.refresh();  
            binding.fireNavigation('_close'); 
        } 
        options.isSuccess = { 
            return (mode == 'finish'); 
        }
        options.getMode = {
            return mode; 
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
            return "Processing Data...  ("+ options.value +" of "+ options.maxvalue +")"; 
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