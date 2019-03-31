package com.rameses.gov.etracs.rptis.models;
                
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.*;
import java.io.*;

class AttachmentDatabaseAdapter implements AttachmentAdapter
{   
    def model;   

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


    def createFolderInfo() {
        def folderPath = DBImageUtil.instance.cacheDirectory + File.separatorChar + getFolderName();
        def file = new java.io.File(folderPath);
        
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

    public void loadFolders(files) {
        files << createFolderInfo();
    }
        
    public void loadItems( info ) { 
        def folderName = getFolderName();
        info.items = []; 
        def headers = getHeaders();
        headers.each {
            def f = DBImageUtil.instance.getImage2(folderName, it.objid);
            info.items << [
                objid: f.name,
                caption: it.title,
                filepath: f.canonicalPath,
                thumbnail:  model.createThumbnail(f),
            ];
        }
    }

    def getHeaders() {
        return model.querySvc.getList([_schemaname: 'dbimage_header', findBy:[parentid: model.callerEntity.objid]]);
    }
    
}