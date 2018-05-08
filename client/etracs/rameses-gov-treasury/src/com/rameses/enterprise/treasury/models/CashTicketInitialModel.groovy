package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class CashTicketInitialModel {

    @Invoker 
    def invoker; 
    
    def tag;
    def entity;
    
    String title = 'Cash Ticket Entries';
    
    void initCollector() {
        this.tag = 'collector'; 
        initEntity();
    }
    
    void initSubCollector() {
        this.tag = 'subcollector'; 
        initEntity();
    }
    
    void initEntity() {
        entity = [:]; 
        entity.items = []; 
    }
    
    def selectedItem;
    def listHandler = [
        fetchList : {
            return entity.items; 
        }
    ] as BasicListModel;
    
    def doAdd() {
        return null; 
    }

    def doRemove() {
        return null; 
    }

    def doOpen() {
        return null; 
    }

}