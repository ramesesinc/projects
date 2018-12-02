package com.rameses.gov.etracs.tools.exporter;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;
import javax.swing.*;

class RemoteServerDataImportController {
    
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
        entity = [:];
        entity.data = [:];
        entity.data.state = 'POSTED';
        entity.data.collectiontypes = [];
        entity.data.itemaccounts = [];
        entity.data.users = [];
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
        if ( i==0 ) { 
            entity = FileUtil.readObject( chooser.selectedFile );
            mode = MODE_READ; 
            
            [
                userListHandler, usergroupHandler, orgListHandler, afListHandler, 
                fundListHandler, revenueListHandler, collectionTypeListHandler, 
                collectionGroupListHandler, bankListHandler   
            ].each { 
                it.reload(); 
            } 
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
            
    def userListHandler = [
           fetchList : { return entity.data.users; }
    ] as BasicListModel;

    def usergroupHandler = [
           fetchList : { return entity.data.usergroups; }
    ] as BasicListModel;
 
    def orgListHandler = [
           fetchList : { return entity.data.orgs; }
    ] as BasicListModel; 

    def afListHandler = [
            fetchList : { return entity.data.afs; } 
    ] as BasicListModel;
    
    def fundListHandler = [
            fetchList : { return entity.data.funds; } 
    ] as BasicListModel;

    def revenueListHandler = [
            fetchList : { return entity.data.itemaccounts; } 
    ] as BasicListModel;
    
    def collectionTypeListHandler = [
            fetchList : { return entity.data.collectiontypes; }
    ] as BasicListModel;
    
    def collectionGroupListHandler = [
           fetchList : { return entity.data.collectiongroups; }
    ] as BasicListModel;   

    def bankListHandler = [
           fetchList : { return entity.data.banks; }
    ] as BasicListModel; 
}

