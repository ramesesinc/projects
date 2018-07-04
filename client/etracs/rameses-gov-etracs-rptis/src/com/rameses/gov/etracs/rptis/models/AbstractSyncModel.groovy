package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

public abstract class AbstractSyncModel  
{
    @Binding
    def binding;
    
    @Service('RPTSyncService')
    def syncSvc;
    
    @Caller
    def caller;
    
    @Invoker
    def invoker;
    
    def entity;
    
    def processing = false;
    boolean showry = false;
    boolean showsync = true;
    def loghandler = new TextWriter();
    
    def getTitle(){
        return 'Synchronizing ' + entity.title;
    }
    
    public abstract def sync(data);
    
    def onComplete = { data ->
        sync(data);
        processing = false;
        loghandler.writeln('done');
        if (data._errors) {
            loghandler.writeln('\nData synchronization completed with errors.');
            loghandler.writeln('Total records processed: ' + data._count);
            loghandler.writeln('\n------------------------------------------------------------------------');
            loghandler.writeln('Errors: ');
            loghandler.writeln('--------------------------------------------------------------------------');
            data._errors.each {
                loghandler.writeln('  -> ' + it);
            }
        } else {
            loghandler.writeln('\nData synchronization successfully completed.');
        }
        
        caller?.search();
        binding.refresh();
    }
    
    def onError = {e->
        processing = false;
        loghandler.writeln('error');
        loghandler.writeln('\nERROR: ' + e.message);
        loghandler.writeln('');
        binding.refresh();
    }

    
    def process = [
        run : {
            try {
                processing = true;
                loghandler.write('Synchronizing data... ');
                def data = sync(entity);
                onComplete(data)
            } catch(e) {
                onError(e);
            }
        }
    ] as Runnable;
    
    void syncData(){
        if (MsgBox.confirm('Sync data?')){
            new Thread(process).start();
        }
    }
    
    def doClose(){
        return '_close';
    }   
        
}