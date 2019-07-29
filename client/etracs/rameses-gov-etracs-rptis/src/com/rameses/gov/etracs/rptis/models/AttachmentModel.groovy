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

    @Service('QueryService')
    def querySvc;

    def listeners = [];    
    def entity;
    def files;
    def folderName;
    def callerEntity;
    def selectedAttachment;
    def isMultiSelect = false;
    def width = 150;
    def height = 150;
    def adapter;
    def scaler = new ImageScaler(); 
    
    void init() {
        callerEntity = entity;
        entity = [:]; 
        files = [];
        loadAdapter();
        adapter.loadFolders(files);
    }

    def createThumbnail(file) {
        try {
            return scaler.createThumbnail( file, width, height  ); 
        } catch(Throwable t){
            return null;
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
            adapter.loadItems( o ); 
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

