package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutSectionAccountModel {
    
    @Service('QueryService')
    def querySvc; 

    @Service('PersistenceService')
    def persistenceSvc; 
    
    @Caller 
    def caller;

    def title = 'Accounts'; 
    def accounts = [];
    def schemaName = 'waterworks_stubout_account';
    def selectedItem;
    int sortorder;
    
    
    def getEntity() {
        return caller?.entity; 
    } 
    
    void init() { 
        
    }

    def listHandler = [
        fetchList: { o-> 
            if ( !accounts ) { 
                def m = [:];
                m._schemaname = schemaName;
                m.findBy = [parentid:entity.objid];
                m.orderBy = 'sortorder';
                m.putAll( o );
                accounts = querySvc.getList( m );
                sortorder = accounts.size();
            } 
            return accounts; 
        }        
    ] as BasicListModel;
    
    def addAccount() {
        def h = { o-> 
            def m = [ parentid: entity.objid, account: o, _schemaname: schemaName, sortorder:sortorder+1 ]; 
            persistenceSvc.create( m ); 
            accounts.clear();
            listHandler.reload(); 
        }
        return Inv.lookupOpener("waterworks_account_notin_stubout:lookup", [ onselect: h])
    }
    
    void removeAccount() { 
        if ( !selectedItem ) return;
        
        persistenceSvc.removeEntity([ _schemaname: schemaName, objid: selectedItem.objid ]); 
        accounts.clear();
        listHandler.reload(); 
    }
    
    void swap() {
        if ( !selectedItem ) return; 
        
        def val = MsgBox.prompt('Enter the sort order number:');
        if ( !val ) return; 
        
        def sortorder = val.toInteger(); 
        if ( selectedItem.sortorder != sortorder ) { 
            if ( !accounts.find{ it.sortorder==sortorder }) {
                throw new Exception('Invalid sort order number'); 
            }

            persistenceSvc.update([ 
                _schemaname: schemaName, _tag:'swap', 
                objid: selectedItem.objid, 
                sortorder: sortorder  
            ]); 
            accounts.clear(); 
            listHandler.reload(); 
        }
    }
    void moveUp() { 
        if ( !selectedItem ) return; 
        
        int sortorder = selectedItem.sortorder-1;
        if (sortorder < 1) sortorder = 1; 
        
        persistenceSvc.update([ 
            _schemaname: schemaName, _tag:'moveup', 
            objid: selectedItem.objid, sortorder: sortorder  
        ]); 
        accounts.clear(); 
        listHandler.reload(); 
    }
    void moveDown() { 
        if ( !selectedItem ) return; 
        
        int sortorder = selectedItem.sortorder+1;
        if (sortorder > accounts.size()) sortorder = accounts.size(); 
        
        persistenceSvc.update([ 
            _schemaname: schemaName, _tag:'movedown', 
            objid: selectedItem.objid, sortorder: sortorder  
        ]); 
        accounts.clear(); 
        listHandler.reload(); 
    }
}