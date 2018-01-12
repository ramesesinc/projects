package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class AFControlLookupBatchModel  extends CrudLookupModel {
    
    def refitem;
    
    def getCustomFilter() {
        def m = [:];
        m.afid = refitem.item.objid;
        m.unit = refitem.unit;
        
        if( refitem.txntype.matches(".*SALE") ) {
            m.state = 'SOLD';
        }
        else if( refitem.txntype.matches(".*COLLECTION")) {
            m.state = 'ISSUED'
        }
        return [ "afid=:afid AND unit =:unit AND state =:state AND active=0 AND lockid IS NULL", m ];
    }
}    