package com.rameses.clfc.treasury.amnesty.history

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AmnestyHistoryListController
{
    @Binding 
    def binding;
    
    @Service('AmnestyHistoryService') 
    def service; 
    
    def query = [:];
    def selectedItem;
    def entity;
    def refno;
    
    void init() {
        refno = entity.refno;
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
            if (refno) params.refno = refno;
            
            return service.getList( params ); 
        },
        onOpenItem: { itm, colName->
            return Inv.lookupOpener('amnestyhistory:open', [entity: itm]);
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
    
    def open() {
        return Inv.lookupOpener('amnestyhistory:open', [entity: selectedItem]);
    }
}

