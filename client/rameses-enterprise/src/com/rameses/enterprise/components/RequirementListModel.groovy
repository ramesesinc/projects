package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

class RequirmentListModel extends ComponentBean {
        
    @Service("RequirementRuleService")
    def service;
    
    def selectedItem;

    String ruleset;
    
    public def getEntity() {
        return getValue();
    }
    
    void reload() {
        listModel.reload();
    }

    def listModel = [
        fetchList: { o->
            return entity.requirements;
        }
    ] as BasicListModel;
    
    void runRules() {
        def m = [:];
        m.putAll( entity );
        m.ruleset = ruleset;
        def result = service.execute( m );
        entity.requirements = result.requirements;
        listModel.reload();
    }
    
    /*    
    def edit() {
        if(!selectedItem) return null;
        return InvokerUtil.lookupOpener( "business_application:requirement", [
            info:selectedItem,
            handler: { o->
                selectedItem.putAll( o );
                listModel.reload();
            }
        ]);
    }

    void verify() {
        def info = entity.requirements?.find{( it.completed && !it.refno )} 
        if ( info ) throw new Exception('Please provide a refno for '+ info.title + ' requirement'); 
    } 

    void reset(){
        listModel.reload();
    }
    */
    
    
}