package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.io.File;
import com.rameses.gov.etracs.rptis.util.*;

public class TransmittalImportModel
{
    @Binding
    def binding;
    
    @Service('RPTTransmittalImportService')
    def importSvc
    
    @Invoker
    def invoker 
    
    def file;
    def mode;
    def task;
    def reader;
    def entity;
    def data;
    
    @PropertyChangeListener
    def listener = [
        'file' : {
            entity = [:]
            if (file){
                try{
                    reader = new ObjectReader(file);
                    data = reader.readObject();
                    entity = data.transmittal;
                }
                finally{
                    reader.close();
                }
                
            }
        }
    ]
    
    String getTitle(){
        return 'Import Transmittal ';
    }
    
    def init(){
        mode = 'init';
        return 'init';
    }
    
    public def getFileType(){
        def filetype = invoker.properties.filetype;
        if (!filetype) throw new Exception('Attribute filetype is required.');
        return filetype;
    }
    
    public def importData(transmittal, data){
        return importSvc.importData(transmittal, data);
    }
    
    
    def listHandler = [
        fetchList : { return entity.items }
    ] as BasicListModel
    
    
    def doNext(){
        if (!file)
            throw new Exception('Transmittal File is required.');
            
        if (data == null || data.filetype != 'transmittal'){
            println 'data.filetype -> ' + data.filetype
			throw new Exception('Invalid file format.')
        }
        
        def filetype = getFileType();
        if (entity.filetype != filetype)
            throw new Exception('Transmittal is invalid. Only file of Type "' + filetype + '" is allowed.');

        importSvc.validateImport(entity);
        
        mode = 'read';
        return 'default';
    }
    
    void doImport(){
        if (MsgBox.confirm('Import transmittal?')){
            task = new TransmittalImportTask();
            task.importModel = this;
            task.importSvc   = importSvc; 
            task.file        = file;
            task.updateItem  = updateItem;
            task.oncomplete  = oncomplete;
            task.onerror     = onerror;
            Thread t = new Thread(task);
            t.start();
            mode = 'processing'
        }
    }
    
    def updateItem = {updateditem ->
        def item = entity.items.find{it.objid == updateditem.objid}
        item.putAll(updateditem);
        listHandler.reload();
    }
    
    def oncomplete = {
        mode = 'completed'
        task = null;
        binding.refresh();
        MsgBox.alert('Transmittal has been successfully imported.');
    }    
    
    def onerror = {errmsg ->
        mode = 'read'
        task = null;
        binding.refresh();
        MsgBox.alert(errmsg);
    }   
}