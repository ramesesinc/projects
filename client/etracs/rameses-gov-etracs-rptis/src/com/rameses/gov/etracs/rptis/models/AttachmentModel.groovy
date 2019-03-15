package com.rameses.gov.etracs.rptis.models;


                
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class AttachmentModel
{
    @Binding
    def binding;

    @Invoker 
    def invoker;

    @Service('Var')
    def var;

    def listeners = [];    
    def entity;
    def files;
    def folderName;
    def callerEntity;
    def selectedAttachment;
    def isMultiSelect = false;
    def width = 150;
    def height = 150;
    
    void init() {
        callerEntity = entity;
        entity = [:]; 
        files = [];
        def folderpath = getFileServerPath() + getFolderName();
        loadFolders(new java.io.File(folderpath))
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
        if (!folderName) {
            def folderFieldName = invoker.properties.folderFieldName;
            if (!folderFieldName) throw new Exception('Invoker folderFieldName must be defined.');
            folderName = callerEntity[folderFieldName];
        }
        return folderName;
    }
    
    void loadFolders(file){
        if (file.isDirectory()) {
            files << createFolderInfo(file);
            file.listFiles().each{f->
                if (f.isDirectory()) {
                    loadFolders(f);
                }
            }
        }
    }
    
    def createFolderInfo(file) {
        def info = [
            objid    : file.name, 
            title    : file.name, 
            createdby : [name: 'system'],  
            dtcreated : new java.sql.Date( file.lastModified()), 
            filetype : 'jpg',
            filepath : file.canonicalPath,
        ]; 
        return info;
    }
    
    def scaler = new ImageScaler(); 
    
    void loadItems( info ) { 
        info.items = []; 
        
        def dir = new java.io.File( info.filepath );
        dir.listFiles().each{ f-> 
            if ( f.isDirectory()) return; 

            // primary info 
            def m = [
                objid    : f.name, 
                caption  : f.name,  
                filepath : f.canonicalPath 
            ]; 
            
            try {
                m.thumbnail = scaler.createThumbnail( f, width, height  ); 
            } catch(Throwable t){;} 
            
            info.items << m; 
        } 
    }
    
    def attachmentHandler = [
        isAllowAdd: { false },
        isAllowRemove: { false },
        isMultiSelect: { isMultiSelect },
        getCellWidth: { width },
        getCellHeight: { height },
        fetchList: {
            return files; 
        }, 
        getItem: { o->
            loadItems( o ); 
            return o; 
        },
        openItem: { o-> 
            if ( listeners ) {
                listeners.each{ ls-> 
                    ls(o);
                } 
                throw new com.rameses.util.BreakException(); 
            } 
            
            return Inv.lookupOpener('attachment:preview', [entity: o, listeners: listeners]); 
        }
    ] as FileViewModel;
}
       