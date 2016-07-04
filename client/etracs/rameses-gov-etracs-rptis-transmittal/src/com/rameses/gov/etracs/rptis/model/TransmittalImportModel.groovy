package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.io.File;

public abstract class TransmittalImportModel
{
    @Binding
    def binding;
    
    @Service('RPTTransmittalImportService')
    def importSvc
    
    def file;
    def info;
    def processing = false;
    def task;
    def type;
    
    String getTitle(){
        return 'Import Transmittal ';
    }
    
    void init(){
        type = 'import';
        processing = false;
    }
    
    public abstract def getFileType();
    public abstract void importData(data);
    
    
    void doImport(){
        info = '';
        processing = true;
        task = new TransmittalImportTask();
        task.importModel = this;
        task.importSvc   = importSvc; 
        task.file        = file;
        task.oncomplete  = oncomplete;
        task.showinfo    = showinfo;
        task.onerror     = onerror;
        Thread t = new Thread(task);
        t.start();
    }
    
    def oncomplete = {
        processing = false;
        task = null;
        showinfo('\n\n\n' + it)
    }    
    
    def onerror = {
        processing = false;
        task = null;
        showinfo('\n\n\nERROR: ' + it)
    }
    
    def showinfo = { msg ->
        info += msg;
        binding.refresh('.*');
    }    
    
}