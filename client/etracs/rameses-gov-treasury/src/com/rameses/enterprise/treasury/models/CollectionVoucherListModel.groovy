package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudListModel;

class CollectionVoucherListModel extends CrudListModel {

    public boolean isAutoResize() {
        return true; 
    }
    
    public void initColumn( c ) { 
        if ( c.name == 'controlno') {
            c.width = 120; 
            c.maxWidth = 140; 
        }
        else if ( c.name == 'controldate') {
            c.width = 120; 
            c.maxWidth = 120; 
        }
        else if ( c.name == 'liquidatingofficer.name') {
            c.width = 200; 
        }
        else if ( c.name.toString().matches('amount|totalcash|totalcheck|totalcr')) {
            c.width = 100; 
            c.maxWidth = 120;
        } 
        else if ( c.name == 'state') {
            c.width = 120;
            c.maxWidth = 120;
        }
    } 
}    