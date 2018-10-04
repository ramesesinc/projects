package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AFModel extends CrudFormModel {

    @PropertyChangeListener
    def listener = [
        "serial" : { o->
            if(o==false) entity.serieslength = 0;
        }
    ];
    
    def serial = false;
    def useTypes = ["collection", "disbursement"];
    def formTypes = [ "serial", "cashticket", "eor" ];

    void afterCreate() {
        entity.system = 0;
        entity.serieslength = 0;
        entity.denomination = 0;
    }
    
    void afterOpen() {
        if( entity.formtype == "cashticket" && entity.serieslength > 0 ) {
            serial = true;
        }
    }
    
    //
    void beforeSave(def m ) {
        if( entity.formtype == "serial" && entity.serieslength == 0 ) {
            throw new Exception("Please specify a series length");
        }
        else if( entity.formtype == "cashticket" && serial==true  && entity.serieslength == 0 ) {
            throw new Exception("Please specify a series length for cashticket serial");
        }
    } 

}        