package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutSectionAccountModel {
    
    @Service('WaterworksStuboutService')
    def svc; 
    
    @Service("QueryService")
    def queryService;
    
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
        accounts = null;
        listHandler.reload();
    }

    def listHandler = [
        fetchList: { o-> 
            if ( !accounts ) { 
                def m = [_schemaname:'waterworks_account'];
                m.findBy = [stuboutid: entity.objid];
                m.orderBy = "stuboutindex";
                accounts = queryService.getList(m);
            } 
            return accounts; 
        }        
    ] as BasicListModel;
    
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