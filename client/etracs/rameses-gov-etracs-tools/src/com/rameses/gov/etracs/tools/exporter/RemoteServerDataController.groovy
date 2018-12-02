package com.rameses.gov.etracs.tools.exporter;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;
import javax.swing.*;
        
class RemoteServerDataExportController {
    
    @Binding
    def binding;
    
    @Service('RemoteServerDataService')
    def svc;
    
    @Service('OrgClassLookupService')
    def orgClassSvc
    
    @Service('OrgLookupService')
    def orgSvc
    
    def INIT_CREATE = 'init';
    def MODE_CREATE = 'create';
    def MODE_EDIT   = 'edit';
    def MODE_READ   = 'read';
    def MODE_PREVIEW = 'preview';
    
    def asyncHandler;
    def entity;
    def mode;
    
    def formats = ['v254', 'v255'];
    
    String getTitle(){
        return 'Export Remote Server Data (' + ( entity.objid ? entity.objid : 'NEW')  + ')';
    }
    
    void init() {
        entity = [:];
        entity.data = [:];
        entity.data.state = 'DRAFT';
        mode  = INIT_CREATE;
    }
    
    def cancel() {
        if (MsgBox.confirm('You are about to close this window. Continue?')) {
            return '_close'; 
        } 
        return null; 
    }
        
    String back() {
        mode  = INIT_CREATE;
        return 'default';
    }
    
    String next(){
        def result = svc.getOrgData( entity );
        if ( !result ) throw new Exception('No returned data. Please check.')
        
        def orginfo = entity.data.org;         
        entity = new com.rameses.util.Base64Cipher().decode( result ); 
        mode = MODE_PREVIEW;
        [
            userListHandler, usergroupHandler, orgListHandler, afListHandler, 
            fundListHandler, revenueListHandler, collectionTypeListHandler, 
            collectionGroupListHandler, bankListHandler   
        ].each { 
            it.load(); 
        } 
        
        entity.data.orgs.each{ it.root=0 }
        orginfo = entity.data.orgs.find{ it.objid==orginfo.objid }         
        return 'main'; 
    } 
    
    /*void save(){
        asyncHandler = [
            onError: {o-> 
                MsgBox.err( o.message ); 
                mode = MODE_CREATE; 
                binding.refresh(); 
            }, 
            onTimeout: {
                asyncHandler.retry(); 
            },
            onCancel: {
                mode = MODE_CREATE;
                binding.refresh();
            }, 
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                    mode = MODE_CREATE;
                    binding.refresh();
                    
                } else if (o instanceof Throwable) { 
                    println 'remote-server-data failed due to ' + o.message;
                    MsgBox.err(o.message); 
                    asyncHandler.cancel();
                    asyncHandler = null;
                    mode = MODE_CREATE;
                    binding.refresh();
                    
                } else {
                    asyncHandler = null;
                    success();
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler         
        
        
        if (mode == MODE_CREATE) { 
            svc.create(entity, asyncHandler);
        } else { 
            svc.update(entity, asyncHandler); 
        } 
        mode = MODE_PROCESSING;
    }

    void success() {
        println 'remote-server-data successfully saved'
        def runnable = {
            entity.state = 'POSTED'; 
            mode = MODE_READ; 
            binding.fireNavigation('main'); 
        } as Runnable; 
        
        new Thread(runnable).start(); 
    }
    
    void edit(){
        mode = MODE_EDIT;
    }*/
    
    void export(){
        def filename = entity.objid.toString().replaceAll("[\\s]{1,}", "_");
        def chooser = new JFileChooser();
        chooser.setSelectedFile(new File(filename + '.dat'));
        int i = chooser.showSaveDialog(null);
        if ( i==0 ) { 
            FileUtil.writeObject( chooser.selectedFile, entity );
            MsgBox.alert("Data has been successfully exported!");
        } 
    }
    
    public List getOrgclassess(){
        return orgClassSvc.getList([:])
    }
    
    public List getOrgs() {
        if(! entity.data.orgclass ) return []
        
        return orgSvc.getList([orgclass:entity.data.orgclass.name ])
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

