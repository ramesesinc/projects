package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;

public class RPTLedgerLookupModel extends ServiceLookupController  
{   
    def query;
    
    protected void onbeforeFetchList(Map params) {
        if (query){
            params.putAll(query);
        }
        super.onbeforeFetchList(params);
    }
}