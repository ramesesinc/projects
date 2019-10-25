package com.rameses.enterprise.financial.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class CashLedgerListModel extends com.rameses.seti2.models.CrudLookupModel {
    
    def confs = [
        'jev.jevdate' : [ caption: 'Ref Date', maxWidth: 120 ],
        'jev.refno '  : [ caption: 'Ref No' ],
        'dr' : [ caption: 'DR', maxWidth: 150 ],
        'cr' : [ caption: 'CR', maxWidth: 150 ],
    ]; 
    
    public void initColumn( def c ) { 
        def conf = confs.get( c.name ); 
        if ( !conf ) return; 
        
        if ( conf.type != null ) c.type = conf.type;
        if ( conf.width != null ) c.width = conf.width; 
        if ( conf.minWidth != null ) c.minWidth = conf.minWidth; 
        if ( conf.maxWidth != null ) c.maxWidth = conf.maxWidth; 
        if ( conf.visible != null ) c.visible = conf.visible; 
        if ( conf.caption != null ) c.caption = conf.caption; 
    }     
    
}