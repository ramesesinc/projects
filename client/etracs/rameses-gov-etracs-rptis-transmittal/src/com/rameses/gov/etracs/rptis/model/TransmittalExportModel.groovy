package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.io.File;
import com.rameses.gov.etracs.rpt.util.*;

public abstract class TransmittalExportModel
{
    @Binding
    def binding;
    
    def entity;
    def file;
    def info;
    def type;
    def processing = false;
    def task;
    
    String getTitle(){
        return 'Export Transmittal ' + entity.txnno;
    }
    
    void init(){
        initFile();
        type = 'export';
        processing = false;
    }
    
    void initFile(){
        def dir = System.getProperty('user.home');
        if (!dir.endsWith(File.separator))
            dir += File.separator
        def filename = dir + 'TRANSMITTAL-' + entity.txnno + (entity.state=='APPROVED' ? '-' + entity.state : '') + '.dat';
        file = new File(filename);
    }
    
    public abstract def exportItem(transmittalitem);
   
    void export(){
        info = '';
        processing = true;
        task = new TransmittalExportTask();
        task.exportModel = this;
        task.entity      = entity;
        task.file        = file;
        task.oncomplete  = oncomplete;
        task.onerror     = onerror;
        task.showinfo    = showinfo;
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