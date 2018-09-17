package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EFTPaymentLookupModel extends CrudLookupModel {
            
    def fundids;

    
    
    def getCustomFilter() {
        if(fundids) {
            return [ "state='DRAFT' AND fund.objid IN ('" + fundids.join("','") + "')" ]; 
        }
        return null;
    }

}