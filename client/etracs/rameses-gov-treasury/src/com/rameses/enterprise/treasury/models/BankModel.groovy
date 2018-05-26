package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class BankModel extends CrudFormModel {

    def depositHandlerList;
    
    private def loadItems(def openerName) {
        def arr = [];
        Inv.lookupOpeners( openerName ).each {
            arr << it.properties.name; 
        }
        return arr;
    }
    
    void afterInit() {
        try {
            if(!depositHandlerList) { 
                depositHandlerList = loadItems( "depositslip:printout" ); 
            }
        } catch(Throwable e) {
            e.printStackTrace(); 
        }
    }
    
    @PropertyChangeListener
    def listener = [
        'entity.depository' : {
            if (entity.depository == 1){
                entity.deposittype = 'ON-US'
            }
            else {
                entity.deposittype = null
                entity.depositsliphandler = null; 
            }
        }
    ]
    
}