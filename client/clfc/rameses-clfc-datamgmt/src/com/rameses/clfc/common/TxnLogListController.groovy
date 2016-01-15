package com.rameses.clfc.common;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class TxnLogListController 
{
    @Binding 
    def binding;
    
    @Service('TxnLogService') 
    def service; 
    
    def query = [:];
    
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
