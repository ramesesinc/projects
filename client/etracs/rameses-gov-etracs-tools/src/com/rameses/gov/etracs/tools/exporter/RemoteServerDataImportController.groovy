package com.rameses.gov.etracs.tools.exporter;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import javax.swing.*
import com.rameses.io.*
        
class RemoteServerDataImportController 
{
    @Binding
    def binding;
    
    @Service('RemoteServerDataDeployerService')
    def svc;
    
    def MODE_INIT = 'init';
    def MODE_READ = 'read';
    
    def entity;
    def mode;
    
    String getTitle(){
        return 'Import Remote Server Data ';
    }
    
    void init(){
        entity = [:]
        entity.data = [:]
        entity.data.state = 'POSTED'
        entity.data.collectiontypes = []
        entity.data.itemaccounts = []
        entity.data.users = []
        mode   = MODE_INIT;
    }
    
    void deploy(){
        if (MsgBox.confirm('Deploy updates?')){
            svc.deployUpdates(entity);
            MsgBox.alert('Updates has been successfully deployed.');
        }
    }
    
    void doImport(){
        def chooser = new JFileChooser();
        int i = chooser.showOpenDialog(null);
        if(i==0) {
            entity = FileUtil.readObject( chooser.selectedFile );
            collectionTypeListHandler.reload();
            revenueListHandler.reload();
            userListHandler.reload();
            mode = MODE_READ;
        }      
    }
          
    
    /*=================================================================
     *
     * HANDLERS 
     *
     *================================================================= */
    
    def selectedCollectionType;
    def selectedRevenueItem;
    def selectedUser;
            
 
    def collectionTypeListHandler = [
            fetchList : { return entity.data.collectiontypes; }
    ] as BasicListModel;
    
    
    def revenueListHandler = [
            fetchList    : { return entity.data.itemaccounts; } 
    ] as BasicListModel;
    
    
    def userListHandler = [
           fetchList    : { return entity.data.users; }
    ] as EditorListModel;
}

