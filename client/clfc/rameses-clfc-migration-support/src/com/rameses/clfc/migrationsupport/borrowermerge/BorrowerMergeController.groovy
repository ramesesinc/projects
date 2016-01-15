package com.rameses.clfc.migrationsupport.borrowermerge

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BorrowerMergeController 
{
    @Service("MigrationBorrowerMergeService")
    def service;
    
    String title = "Merge Request";
    
    def entity;
    void open() {
        entity = service.open(entity);
        listHandler?.reload();
    }
    
    def close() { return '_close'; }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel;
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this request. Continue?")) return;
        
        entity = service.approveDocument(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this request. Continue?")) return;
        
        entity = service.disapprove(entity);
    }
    
}

