package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnMainModel extends CrudPageFlowModel {

    @Service("DateService")
    def dateSvc;
    
    def txnTypes;
    boolean withrequest = false;
    def afrequest;
    def reqtype = null;
    def handler;

    def startCreate() {
        entity = [:];
        entity.state = "DRAFT";
        entity.items = [];
        afrequest = null; 
        entity.dtfiled = dateSvc.getBasicServerDate();

        try {
            txnTypes = Inv.lookupOpeners("aftxn:handler").collect { [caption:it.caption, name:it.properties.name] };
        }
        catch(e){;}
        return super.start("default");
    }
    
    void selectHandler() {
        String hname = "aftxn:handler:"+entity.txntype.toLowerCase();
        try {
            def h = [
                back : {
                    def op = super.signal("back");
                    binding.fireNavigation( op );
                },
                forward: {
                    def op = super.signal("forward");
                    binding.fireNavigation( op );
                }
            ];
            handler = Inv.lookupOpener(hname, [handler: h, entity: entity]);
        }
        catch(e) {
            throw new Exception("Txn Type " + hname + " not found!" );
        }
    }
    
     //the only lookup here is for the request because the other request os for purchase (af receipt)
    public def getLookupRequest() {
        if( entity.txntype == "ISSUE" ) {
            return Inv.lookupOpener( "afrequest_collection:lookup", [:] );
        }
        else {
            return Inv.lookupOpener( "afrequest_purchase:lookup", [:] );
        }
    }

    
    
}    