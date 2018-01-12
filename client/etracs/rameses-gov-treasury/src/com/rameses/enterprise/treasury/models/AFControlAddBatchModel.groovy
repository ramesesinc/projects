package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class AFControlAddBatchModel  {

    @Service("AFTxnService")
    def service;
    
    @Service("QueryService")
    def queryService;
    
    def refitem;
    
    //this is the parent;
    def entry;
    def handler;
    
    @FieldValidator
    def validators = [
        "entry.startseries" : { o->
            try {
                Integer.parseInt(o);
            }
            catch(e) {
                throw new ValidatorException("Start series must be a number" );
            }
            if( !o.endsWith("1") )
                throw new ValidatorException("Start series must end with 1" );
            if(o.length() != refitem.afunit.serieslength ) {
                throw new ValidatorException("Start series length must be " + refitem.afunit.serieslength );
            }      
        }
    ];

    @PropertyChangeListener
    def listener = [
        "entry.qty" : { o->
            if(refitem.afunit.formtype == 'serial') {
                if(entry.startseries) entry.endseries =   formatSeries( Integer.parseInt(entry.startseries) + (o * refitem.afunit.qty) - 1) ; 
            }
            if( entry.startstub ) entry.endstub = entry.startstub + o - 1;
        },
        "entry.startseries" : { o->
            entry.endseries = formatSeries( Integer.parseInt(o) + (entry.qty * refitem.afunit.qty ) - 1 ); 
        },
        "entry.startstub" : { o->
            entry.endstub = o + entry.qty - 1;
        }
    ];
    
    void init() {
        entry = [:];
        entry.refitemid = refitem.objid;
        entry.qty = refitem.qty - refitem.qtyserved;
    }

    def formatSeries = { o->
        return o.toString().padLeft( refitem.afunit.serieslength, '0');
    }

    def doCancel() {
        return "_close";
    }

    def doOk() {
        if(entry.prefix==null) entry.prefix = '';
        if(entry.suffix==null) entry.suffix = '';
        service.addBatch( entry );
        handler( entry );
        return "_close";
    }
    
}    