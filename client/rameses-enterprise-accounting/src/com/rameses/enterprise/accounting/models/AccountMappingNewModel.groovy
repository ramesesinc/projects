package com.rameses.enterprise.accounting.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class AccountMappingNewModel extends CrudFormModel { 

    def entity;
    def handler;
    
    void create() {
        
    }
    
    def itemList = [
        fetchList: { o->
            def m = [:];
            m._schemaname = "itemaccount";
            m._limit = 100;
            def str = ""
            m.where = ["objid NOT IN (" + str +")"];
        }
    ] as BasicListModel;
    
    
} 

