package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class RPTLedgerRedFlagResolveModel {
    def onresolve = {};
    def remarks;
    
    def resolve() {
        onresolve(remarks);
        return '_close';
    }
}