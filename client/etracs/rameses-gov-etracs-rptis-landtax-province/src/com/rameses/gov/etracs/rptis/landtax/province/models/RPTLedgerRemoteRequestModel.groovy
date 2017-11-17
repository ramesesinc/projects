package com.rameses.gov.etracs.rptis.landtax.province.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

class RPTLedgerRemoteRequestModel 
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    @Service('LGUService')
    def lguSvc;
    
    @Service('ProvinceRPTLedgerService')
    def service
    
    def request = [:];
    def processing = false;
    
    String title = 'Request Municipal Real Property Ledger';
    
    def loghandler = new TextWriter();
    
    void init(){
        
    }
    
    
    def handler = [
        onMessage : { msg ->
            if (msg != AsyncHandler.EOF && msg.msgtype == '_ERROR_' ){
                loghandler.writeln('');
                loghandler.writeln(msg.msg);
            }
            else if (msg != AsyncHandler.EOF){
                //TODO: service.syncData(msg);
                loghandler.writeln('Updating data ' + msg.msgtype + ': ' + msg.data.objid + '.');
            }
            else{
                try{ caller?.search(); } catch(e){}
                
                try{ 
                    caller?.open();
                    caller?.binding?.refresh(); 
                } catch(e){}
                
                loghandler.writeln('\nRecord has been successfully synchronized.');
                processing = false;
            }
            binding.refresh('doClose|syncData')
        },
        
        onError : { e ->
            println '='*50 ;
            println e.printStackTrace();
            loghandler.writeln('\n\n');
            loghandler.writeln('ERROR: ' + e.message);
            processing = false;
            binding.refresh('doClose|syncData')
        }
    ] as AsyncHandler
    
    
    def process = [
        run : {
            processing = true;
            loghandler.writeln('Sending request to remote server...');
            //def params = [remoteservicename:'MunicipalityRPTLedgerSyncHandler', methodname:methodname]
            def params = [:]
            params.tdno = request.tdno 
            params.lguid = request.lgu?.objid 
            params.pin = request.lgu?.pin 
            service.requestRemoteLedger(params, handler)
            // syncSvc.syncData(params, handler);
        }
    ] as Runnable;
    
    def sendRequest(){
        new Thread(process).start();
        return 'sync';
    }
    
    def getMunicipalities(){
        return lguSvc.lookupMunicipalities([:])
    }
    
}