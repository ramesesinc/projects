package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.treasury.common.models.LedgerListModel;

public class WaterConsumptionListModel extends LedgerListModel {
    
    public void initColumn( def c ) {
        if ( !c ) return; 
        if ( c.name == 'hold' ) {
            c.maxWidth = 50;
            c.minWidth = 30;
        }
        else if ( c.name.toString().matches('amount|amtpaid|discount|balance')) {
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
        else if ( c.name == 'schedule.readingdate') {
            c.maxWidth = 100;
        }
        else if ( c.name == 'state') {
            c.maxWidth = 100;
            c.minWidth = 30;
            c.width = 60;
        }
    }        
} 