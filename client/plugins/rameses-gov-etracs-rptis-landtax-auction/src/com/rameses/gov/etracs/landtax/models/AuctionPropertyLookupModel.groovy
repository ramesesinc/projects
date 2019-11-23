package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionPropertyLookupModel extends CrudLookupModel
{   
    def state = 'REDEMPTIONFORPAYMENT';
    
    public def getCustomFilter(){
        if (state) {
            return ["state = :state ", [state:state]]
        }
        return null;
    }
}