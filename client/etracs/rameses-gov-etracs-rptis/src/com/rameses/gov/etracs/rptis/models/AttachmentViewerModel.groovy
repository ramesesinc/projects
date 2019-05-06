package com.rameses.gov.etracs.rptis.models;
                
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.io.StreamUtil;
import java.io.FileInputStream;

class AttachmentViewerModel
{
    @Binding
    def binding;

    @Invoker 
    def invoker;

    @Service('Var')
    def var;

    @Service('QueryService')
    def querySvc;
    
    String title = 'Attachment Viewer'
    
    def entity;
    def callerEntity;
    def prevEntity;
    def folders;
    def files;
    def folderName;
    def selectedFolder;
    def selectedFile;
    def adapter;
    
    def viewername = 'faas';
    def ids = [];
        
    @Close
    public void onClose(){
        ClientContext.currentContext.eventManager.remove(viewername);
    }
    
    
    void doClose(param){
        ids.remove(param.objid);
        if (!ids)
            binding.fireNavigation('_close');
    }
    
    
    void init(){
        callerEntity = entity;
        files = [];
        folders = [];
        loadAdapter();
        loadFolders();
        ClientContext.currentContext.eventManager.register(viewername, [
            onEvent : {o -> 
                entity = o;
                loadFolders();
                selectedFolder = null;
                listHandler.load();
                listHandler.setSelectedItem(0);
                if (!ids.contains(entity.objid))
                    ids << entity.objid;
            }, 
            onMessage : { param ->
                if (param.action == 'close')
                    doClose(param);
            },
            onDestroy : {
                binding.fireNavigation('_close');
            },
        ] as EventListener);
    }

    def getFileServerPath() {
        def varname = 'file_server_path';
        def serverPath = var.get(varname);
        if (!serverPath) {
            throw new Exception('System variable ' + varname + ' is not defined.')
        }
        if (!serverPath.endsWith(File.separator)) {
            serverPath += File.separator;
        }
        return serverPath;
    }

    def getFolderName() {
        return entity.trackingno;
    }

    def getPrevFolderName() {
        def p = [_schemaname: 'faas_list'];
        p.findBy = [tdno: entity.prevtdno];
        p.select = 'objid,trackingno';
        prevEntity = querySvc.findFirst(p)
        if (prevEntity) {
            return prevEntity.trackingno;
        }
        return null;
    }

    def loadFolders() {
        folders = [];
        adapter.loadFolders(folders);

        def prevFolderName = getPrevFolderName();
        if (prevFolderName) {
            def prevfolders = []
            adapter.loadFolders(prevfolders, prevFolderName);
            prevfolders.each{ it.prevEntity = prevEntity};
            folders += prevfolders;
        }
    }
    
    def folderListHandler = [
        fetchList : { return folders },
    ] as BasicListModel
    
    void setSelectedFolder(folder){
        selectedFolder = folder;
        loadFiles();
    }
    
    void loadFiles(){
        files = [];
        if (selectedFolder) {
            selectedFolder._addThumbnail = false; 
            adapter.loadItems(selectedFolder);
            files = selectedFolder.remove('items');
        }
        fileListHandler.load();
    }


    def fileListHandler = [
        fetchList : { return files }
    ] as BasicListModel
    
        
    def getImage(){
        if (selectedFile) {
            def file = new java.io.File(selectedFile.filepath);
            return StreamUtil.toByteArray(new FileInputStream(file));
        }
        return null;
    }

    void loadAdapter() {
        def varname = 'file_server_path';
        def serverPath = var.get(varname);
        if (serverPath) {
            adapter = new AttachmentFileAdapter(model: this);
        } else {
            adapter = new AttachmentDatabaseAdapter(model: this);
        }
    }
    

}
