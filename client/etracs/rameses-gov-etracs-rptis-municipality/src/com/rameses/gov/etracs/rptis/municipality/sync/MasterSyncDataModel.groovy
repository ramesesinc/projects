package com.rameses.gov.etracs.rptis.municipality.sync;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

public class MasterSyncDataController  
{
    @Binding
    def binding;
    
    @Service('MunicipalityMasterDataSyncService')
    def syncSvc;
    
    @Service('PersistenceService')
    def persistence;

    @Caller
    def caller;
    
    @Invoker 
    def inv;
    
    
    def processing;
    boolean showry = false;
    boolean showsync = false;
    
    def params = [:];
    
    def getTitle(){
        return 'Synchronizing ' + inv.properties.schemaCaption;
    }
    
    def loghandler = new TextWriter();
    
    def handler = [
        onMessage : { data ->
            if (data != AsyncHandler.EOF){
                data.each{ 
                    it._schemaname = inv.properties.schemaName;
                    persistence.save(it);
                    loghandler.writeln('Updating data : ' + (it.name ? it.name : it.code) + '.');
                }
            }
            else{
                caller?.search();
                loghandler.writeln('\nData has been successfully synchronized.');
                processing = false;
            }
            binding.refresh('doClose')
        },
        
        onError : { e ->
            println '='*50 ;
            println e.printStackTrace();
            loghandler.writeln('\n');
            loghandler.writeln('ERROR: ' + e.message);
            processing = false;
            binding.refresh('doClose|syncData')
        }
    ] as AsyncHandler

    
    def process = [
        run : {
            processing = true;
            loghandler.writeln('Connecting to remote server...');
            syncSvc.syncData(params, handler);
        }
    ] as Runnable;
    
    void syncData(){
        params.schemaname = inv.properties.schemaName;
        
        if (!params.schemaname) 
            throw new Exception('Schema Name is not specified in the invoker.')
            
        if (MsgBox.confirm('Sync data from province?')){
            new Thread(process).start();
        }
    }
    
    def doClose(){
        return '_close';
    }   
        
}