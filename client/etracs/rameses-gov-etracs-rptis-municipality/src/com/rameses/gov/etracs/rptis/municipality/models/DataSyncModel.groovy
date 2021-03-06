package com.rameses.gov.etracs.rptis.municipality.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.ServiceLookup;

public class DataSyncModel
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    @Invoker 
    def inv;
    
    @Service('MunicipalitySyncService')
    def syncService;
    
    def service;
    def processing = false;
    def ry;
    boolean showry = true;
    boolean showsync = true;
    
    def getTitle(){
        return 'Synchronizing data from Province'
    }
    
    def loghandler = new TextWriter();
    
    void init(){
        if ( inv.properties.showRy && inv.properties.showRy.matches('false|no|0') )
            showry = false;
            
        initServices();
    }
    
    
    def handler = [
        onMessage : { msg ->
            if (msg != AsyncHandler.EOF && msg.msgtype == '_ERROR_' ){
                loghandler.writeln('');
                loghandler.writeln(msg.msg);
            }
            else if (msg != AsyncHandler.EOF){
                service.syncData(msg);
                def m = 'Synchronizing ' + msg.msgtype + '.';
                if (msg.msg) m += ' ' + msg.msg;
                loghandler.writeln(m);
            }
            else{
                try{ caller?.search(); } catch(e){}
                loghandler.writeln('\nSynchronization has been successfully completed.');
                processing = false;
            }
            binding.refresh('doClose|syncData|formActions')
        },
        
        onError : { e ->
            println '='*50 ;
            println e.printStackTrace();
            loghandler.writeln('\n\n');
            loghandler.writeln('ERROR: ' + e.message);
            processing = false;
            binding.refresh('doClose|syncData|formActions')
        }
    ] as AsyncHandler

    
    def process = [
        run : {
            println 'running loghandler thread...'
            processing = true;
            loghandler.writeln('Connecting to remote server...');
            def params = [
                remoteservicename   : inv.properties.remoteServiceName,
                ry : ry,
            ]
            params.putAll(inv.properties);
            syncService.syncData(params, handler);
        }
    ] as Runnable;
    
    void syncData(){
        new Thread(process).start();
        binding.refresh('formActions');
    }
    
    def doClose(){
        return '_close';
    }
        
    void initServices(){
        def servicename = inv.properties.serviceName;
        if (! servicename)
            throw new Exception('Service Name is not specified in the invoker..');
        service = ServiceLookup.create( servicename );
    }
}