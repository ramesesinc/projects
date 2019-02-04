package com.rameses.gov.etracs.waterworks.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudListModel;

class WaterAccountBillingHistoryListModel extends CrudListModel {
    
    public void initColumn( def c ) {
        if ( !c ) return; 
        if ( c.name.toString().matches('amount|arrears|surcharge|interest|credits')) {
            c.maxWidth = 80;
            c.minWidth = 30;
        }
        else if ( c.name.toString().matches('year|volume')) {
            c.maxWidth = 60;
            c.minWidth = 30;
            c.width = 60;
        }
        else if ( c.name.toString().matches("reading|prevreading|monthname")) {
            c.maxWidth = 100;
            c.minWidth = 30;
        }
        else if ( c.name == 'readingdate') {
            c.maxWidth = 100;
        }
    }     
    
    public int getRows() {
        return 24; 
    } 
    
    public def getFindBy() { 
        return [acctid: caller?.entity?.objid]; 
    }
}