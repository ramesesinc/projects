package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudListModel;

class RemittanceListModel extends CrudListModel {

    public void initColumn( c ) { 
        if ( c.name == 'controlno') {
            c.width = 120; 
            c.maxWidth = 140; 
        }
        else if ( c.name == 'controldate') {
            c.width = 120; 
            c.maxWidth = 120; 
        }
        else if ( c.name == 'collector.name') {
            c.width = 200; 
            c.maxWidth = 200;
        }
        else if ( c.name == 'liquidatingofficer.name') {
            c.width = 200; 
            c.maxWidth = 200;
        }
        else if ( c.name.toString().matches('amount|totalcash|totalcheck|totalcr')) {
            c.width = 100; 
            c.maxWidth = 120;
        }        
        else if ( c.name == 'state') {
            c.width = 120;
        }
    }     
    
    def getCustomFilter() {
        if ( tag == 'COLLECTOR' ) {
            return [
                " collector.objid = :collectorid ", 
                [ collectorid: OsirisContext.env.USERID ] 
            ]; 
        }
        return null; 
    }
    
    void beforeRemoveItem() {
        if ( selectedItem?.state.toString() != 'DRAFT' ) 
            throw new Exception("You are not allowed to delete this transaction"); 

        if ( selectedItem?.collector?.objid != OsirisContext.env.USERID ) 
            throw new Exception("You are not allowed to delete this transaction"); 
    }
}    