package com.rameses.gov.etracs.rptis.models;


                
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class AttachmentFileAdapter implements AttachmentAdapter
{   
    def model;
    def folderName;

    def getFileServerPath() {
        def varname = 'file_server_path';
        def serverPath = model.var.get(varname);
        if (!serverPath) {
            throw new Exception('System variable ' + varname + ' is not defined.')
        }
        if (!serverPath.endsWith(File.separator)) {
            serverPath += File.separator;
        }
        return serverPath;
    }

    def getFolderName() {
        def folderFieldName = model.invoker.properties.folderFieldName;
        if (!folderFieldName) throw new Exception('Invoker folderFieldName must be defined.');
        def value = model.callerEntity[folderFieldName];
        if (!value) {
            value = model.callerEntity['prev' + folderFieldName];
        }
        if (!value) throw new Exception('Folder Name is invalid.');
        return value;
    }

    public void loadFolders(files) {
        def folderpath = getFileServerPath() + getFolderName();
        doLoadFolders(files, new java.io.File(folderpath))
    }
    
    void doLoadFolders(files, file){
        if (file.isDirectory()) {
            files << createFolderInfo(file);
            file.listFiles().each{f->
                if (f.isDirectory()) {
                    doLoadFolders(files,f);
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
    
    public void loadItems( info ) { 
        info.items = []; 
        
        def dir = new java.io.File( info.filepath );
        dir.listFiles().each{ f-> 
            if ( f.isDirectory()) return; 

            // primary info 
            info.items << [
                objid    : f.name, 
                caption  : f.name,  
                filepath : f.canonicalPath,
                thumbnail : model.createThumbnail(f),
            ]; 
        } 
    }
    
}
       