package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BatchBillingEditModel extends ChangeInfoModel {

    def item; // set by the caller 

    public def getEntity() {
        return item; 
    }
}