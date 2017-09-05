package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BankAccountLookupModel extends CrudLookupModel {
            
    def fundid;

    def getCustomFilter() {
        if( fundid ) {
            return [ " fund.objid = :fundid", [fundid: fundid] ]; 
        }
        return null;
    }

}