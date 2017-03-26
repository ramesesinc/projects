package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*; 
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.rptis.util.ServiceLookup;

class ExportModel
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
    
    def entity;
    def asyncHandler;
    String title;
    
    
    /* overridable */
    void getData(asyncHandler){
        getService().exportData(entity, asyncHandler)
    }
    
    def getService(){
        String serviceName = inv.properties.serviceName;
        return ServiceLookup.create(serviceName);
    }
    
    def init(){
        println 'entity -> ' + entity;
        title = inv.properties.title;
        msg = 'Processing request please wait...';
        mode = 'init';
        return 'default';
    }
    
    def cancel() {
        asyncHandler?.cancel(); 
        return init();
    } 

    def export() {
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
                    binding.refresh(); 
                } 
                else if (o instanceof Throwable) { 
                    MsgBox.err(o.message); 
                    asyncHandler.cancel();
                    init();
                    binding.refresh();   
                } 
                else {
                    data = o;     
                    exportFile();
                    mode = 'processed';
                    msg = 'Data has been successfully exported.'
                    binding.refresh();
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler;
        
        getData(asyncHandler); 
        mode = 'processing'; 
        return null; 
    } 
    
    void exportFile(){
        if (!file)
            throw new Exception('File Name is required.');
        FileUtil.writeObject(file, data);
    }
}
