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
        def value = null;
        if (model.folderName) {
            value = model.folderName;
        } else {
            def folderFieldName = model.invoker.properties.folderFieldName;
            if (!folderFieldName) folderFieldName = 'tdno';
            value = model.callerEntity[folderFieldName];
            if (!value) {
                value = model.callerEntity['prev' + folderFieldName];
            }
        }
        if (!value) throw new Exception('Folder Name is invalid.');
        return value;
    }


    def createFolderInfo(folderName) {
        def folderPath = DBImageUtil.instance.cacheDirectory + File.separatorChar + folderName;
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
        files << createFolderInfo(getFolderName());
    }

    public void loadFolders(files, folderName) {
        files << createFolderInfo(folderName);
    }
        
    public void loadItems( info ) { 
        def folderName = info.objid;
        info.items = []; 
        def headers = getHeaders(info);
        headers.each {
            def f = DBImageUtil.instance.getImage2(folderName, it.objid);
            def item = [
                objid: f.name,
                caption: it.title,
                filepath: f.canonicalPath,
            ];

            if (info._addThumbnail == null || info._addThumbnail == true) {
                item.thumbnail = model.createThumbnail(f);
            }

            info.items << item;
        }
    }

    def getHeaders(info) {
        if (info.prevEntity) {
            return  model.querySvc.getList([_schemaname: 'dbimage_header', findBy:[parentid: info.prevEntity.objid]]);    
        } else {
            return  model.querySvc.getList([_schemaname: 'dbimage_header', findBy:[parentid: model.callerEntity.objid]]);
        }
    }
    
}