package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*; 
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.rptis.util.ServiceLookup;

class ImportModel 
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    @Invoker
    def inv;
    
    def mode;
    def file;
    def data;
    String msg;
    String title;
    def asyncHandler;
    
    public void importData(data, asyncHandler){
        getService().importData(data, asyncHandler);
    } 
    
    def getService(){
        String serviceName = inv.properties.serviceName;
        return ServiceLookup.create(serviceName);
    }
    
    def init(){
        title = inv.properties.title;
        msg = 'Processing request please wait...';
        mode = 'init';
        return 'default';
    }
    
    def cancel() {
        asyncHandler?.cancel(); 
        return init();
    } 

    def startImport() {
        if (!file)
            throw new Exception('File Name is required.');
            
        asyncHandler = [
            onError: {o-> 
                MsgBox.err(o.message); 
                init();
                binding.refresh(); 
            }, 
            onTimeout: {
                asyncHandler.retry(); 
            },
            onCancel: {
                binding.fireNavigation( init() );
            }, 
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                    caller?.search();
                    binding.refresh(); 
                } 
                else if (o instanceof Throwable) { 
                    MsgBox.err(o.message); 
                    msg = null;
                    asyncHandler.cancel();
                    init();
                    binding.refresh();   
                } 
                else {
                    mode = 'processed';
                    msg = 'Data has been successfully imported.'
                    binding.refresh();
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler;
        
        data = FileUtil.readObject( file );
        importData(data, asyncHandler)
        mode = 'processing'; 
        return null; 
    } 
}
