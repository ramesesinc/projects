package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.io.File;
import com.rameses.gov.etracs.rptis.util.*;

public abstract class TransmittalExportModel
{
    @Binding
    def binding;
    
    def entity;
    def file;
    def info;
    def processing = false;
    def task;
    
    String getTitle(){
        return 'Export Transmittal ' + entity.txnno;
    }
    
    void init(){
        validateItems()
        initFile();
        processing = false;
    }
    
    void validateItems(){
        if (!entity.items)
            throw new Exception('Cannot export transmittal.\nAt least one item is required.');
            
        if ('FORAPPROVAL'.equalsIgnoreCase(entity.type) && entity.tolgu.objid == OsirisContext.env.ORGID){
            def list = entity.items.findAll{ it.status && it.status.matches('APPROVED|DISAPPROVED|RESOLVED')}
            if (!list)
                throw new Exception('Cannot export transmittal.\nAt least one item must either be approved, disapproved or red flagged.');
        }
    }
    
    void initFile(){
        def dir = System.getProperty('user.home');
        if (!dir.endsWith(File.separator))
            dir += File.separator
        def filename = dir + 'TRANSMITTAL-' + entity.txnno + '-' + entity.filetype + (entity.state=='APPROVED' ? '-' + entity.state : '') + '.dat';
        file = new File(filename);
    }
    
    public abstract def exportItem(transmittalitem);
   
    void export(){
        if (!file)
            throw new Exception('File name is required.');
            
        if (MsgBox.confirm('Export transmittal?')){
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
    }
    
    def oncomplete = {
        processing = false;
        task = null;
        showinfo('\n\n' + it)
        binding.refresh('formActions');
    }    
    
    def onerror = {
        processing = false;
        task = null;
        showinfo('\n\nERROR: ' + it)
        binding.refresh('formActions');
    }
    
    def showinfo = { msg ->
        info += msg + '\n';
        binding.refresh('info');
    }    
    
}