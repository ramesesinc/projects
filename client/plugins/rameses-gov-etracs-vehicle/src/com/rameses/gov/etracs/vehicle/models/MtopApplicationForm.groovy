package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class MtopApplicationForm extends VehicleApplicationForm {
    
    public boolean beforePost() {
        boolean pass = false;
        def h = { o->
            entity.info.franchise = o;
            pass = true;
        }
        Modal.show( "mtop_franchise:available:lookup", [onselect:h] );
        if( pass ) {
            pass = false;
            h = { o->
                entity.expirydate = o;
                pass = true;
            }
            Modal.show( "date:prompt", [handler:h, title:'Enter Franchise Expiry date'] );
        }
        return pass;
    }
    
}