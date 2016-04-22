package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutSectionAccountModel {
    
    @Service('WaterworksStuboutService')
    def svc; 
    
    @Caller 
    def caller;

    def schemaName = 'waterworks_stubout';
    def title = 'Accounts'; 
    def accounts = [];
    def selectedItem;
    
    def getEntity() {
        return caller?.entity; 
    } 
    
    void init() {         
    }

    def listHandler = [
        fetchList: { o-> 
            if ( !accounts ) { 
                accounts = svc.getAccounts([ stuboutid: entity.objid ]);
            } 
            return accounts; 
        }        
    ] as BasicListModel;
    
    def addAccount() {
        def h = { o-> 
            def m = [ stuboutid: entity.objid, accountid: o.objid ];
            svc.addAccount( m ); 
            accounts.clear(); 
            listHandler.reload(); 
        }
        return Inv.lookupOpener("waterworks_account_notin_stubout:lookup", [ onselect: h])
    }
    
    void removeAccount() { 
        if ( !selectedItem ) return;
        
        svc.removeAccount([ stuboutid: entity.objid, accountid: selectedItem.objid ]); 
        accounts.clear();
        listHandler.reload(); 
    }
    
    
    void moveUp() { 
        if ( !selectedItem ) return; 
        
        svc.moveUp([ stuboutid: entity.objid, accountid: selectedItem.objid ]);
        accounts.clear(); 
        listHandler.reload(); 
        updateSelection( listHandler.selectedItem.index-1 ); 
    }
    void moveDown() { 
        if ( !selectedItem ) return; 
        
        svc.moveDown([ stuboutid: entity.objid, accountid: selectedItem.objid ]);
        accounts.clear(); 
        listHandler.reload(); 
        updateSelection( listHandler.selectedItem.index+1 ); 
    }
    
    void swap() {
        if ( !selectedItem ) return; 
        
        def val = MsgBox.prompt('Enter the sort order number:');
        if ( !val ) return; 
        
        def sortorder = val.toInteger(); 
        if ( selectedItem.stuboutindex != sortorder ) { 
            if ( !accounts.find{ it.stuboutindex==sortorder }) {
                throw new Exception('Invalid sort order number'); 
            }
            
            svc.swap([ stuboutid: entity.objid, accountid: selectedItem.objid, stuboutindex:sortorder ]);
            accounts.clear(); 
            listHandler.reload(); 
            updateSelection( sortorder-1 ); 
        }
    }
    void updateSelection( int index ) {
        if ( index >= 0 && index < accounts.size()) {
            listHandler.setSelectedItem( index );  
        } 
    }
}