package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BankAccountLookupModel extends CrudLookupModel {
            
    def fundids;
    def fundid;

    def getCustomFilter() {
        if( fundid ) {
            return [ " fund.objid = :fundid", [fundid: fundid] ]; 
        }
        else if(fundids) {
            return [ " fund.objid IN '(" + fundids.join("','") + "')" ]; 
        }
        return null;
    }

}