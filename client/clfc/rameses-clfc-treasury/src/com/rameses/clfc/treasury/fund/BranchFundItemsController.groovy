package com.rameses.clfc.treasury.fund;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class BranchFundItemsController 
{
    @Binding 
    def binding;
    
    @Service('BranchFundItemService') 
    def service; 
    
    def query = [:];
    def fundid;
    
    void init() {
    } 
    
    def handler = [
        getRows: { 
            return 10; 
        }, 
        getColumnList:{ 
            return service.getColumns([:]); 
        }, 
        fetchList: {params-> 
            if (query) params.putAll(query); 
            
            params.fundid = fundid;
            return service.getList( params ); 
        } 
    ] as PageListModel;
    
    void moveFirstPage() {
        handler.moveFirstPage(); 
    }
    
    void moveBackPage() {
        handler.moveBackPage(); 
    }
    
    void moveNextPage() {
        handler.moveNextPage();
    }
}
