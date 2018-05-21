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
        m.unit = refitem.unit;
        m.afid = refitem.item.objid;
        if( refitem.txntype.matches(".*SALE") ) {
            m.state = 'SOLD';
        }
        else if( refitem.txntype.matches(".*COLLECTION")) {
            m.state = 'ISSUED';
        }
        
        def qarr = []; 
        qarr << "afid = :afid AND unit = :unit AND state = :state AND active=0";         
        if ( refitem.txntype.matches('TRANSFER_.*')) {
            qarr << " owner.objid = :ownerid AND assignee.objid = :ownerid "; 
            m.ownerid = refitem.parent.issuefrom.objid; 
        } else if ( refitem.txntype.matches('RETURN_.*')) { 
            qarr << " owner.objid = :ownerid AND assignee.objid = :ownerid "; 
            m.ownerid = refitem.parent.issueto.objid; 
        } 

        return [qarr.join(' AND '), m];
    }
}    