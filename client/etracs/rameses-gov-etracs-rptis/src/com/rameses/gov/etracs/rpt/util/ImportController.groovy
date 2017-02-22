package com.rameses.gov.etracs.rpt.util;

import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*; 
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;
import com.rameses.util.*;

class ImportController 
{
    @Binding
    def binding;
    
    @Service('RPTISMasterImportService')
    def importSvc;
    
    def entity;
    def mode;
    def file;
    def data;
    String msg;
    String title;
    def asyncHandler;
    
    void importData(data, asyncHandler){
        if (!entity.schemaname.equalsIgnoreCase(data.schemaname))
            throw new Exception('File to import is invalid. Type must be ' + entity.schemane + '.');
        importSvc.importData(data, asyncHandler)
    }
    
    def init(){
        title = entity.windowTitle;
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