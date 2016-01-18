package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksAccount extends CrudFormModel {
    
    String title = "Waterworks Account";
    String schemaName = "waterworks_account";
    
    
    def lookupClassification() {
        def h = { o->
            
        }
        return Inv.lookupOpener( "waterworksclassification:lookup", [onselect: h] );
    }
    
}