package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class AFReceiptItemDetailModel  {

    @Service("AFIRService")
    def service;
    
    def params;
    
    //this is the parent;
    def entity;
    def entry;
    def afunit; 
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
            if(o.length() != afunit.serieslength ) {
                throw new ValidatorException("Start series length must be " + afunit.serieslength );
            }      
        }
    ];

    @PropertyChangeListener
    def listener = [
        "entry.qty" : { o->
            if(entry.afunit.formtype == 'serial') {
                if(entry.startseries) entry.endseries =   formatSeries( Integer.parseInt(entry.startseries) + (o * afunit.qty) - 1) ; 
            }
            if( entry.startstub ) entry.endstub = entry.startstub + o - 1;
        },
        "entry.startseries" : { o->
            entry.endseries = formatSeries( Integer.parseInt(o) + (entry.qty * afunit.qty ) - 1 ); 
        },
        "entry.startstub" : { o->
            entry.endstub = o + entry.qty - 1;
        }
    ];
    
    void init() {
        def lineItem = entity.items.find{ it.item.objid == params.afid && it.unit == params.unit };
        entry = [:];
        entry.reftype = entity.txntype;
        entry.refid = entity.objid;
        entry.refno = entity.controlno;
        entry.refdate = entity.dtfiled;
        entry.unit = lineItem.unit;
        entry.afid = lineItem.item.objid;
        entry.afunit = lineItem.afunit;
        entry.respcenter = entity.respcenter;
        entry.cost = lineItem.cost;
        entry.qtyrequested = lineItem.qty;
        entry.qtyreceived = lineItem.qtyserved;
        entry.qty = entry.qtyrequested - entry.qtyreceived;
        afunit = entry.afunit;
    }

    def formatSeries = { o->
        return o.toString().padLeft( afunit.serieslength, '0');
    }

    def doCancel() {
        return "_close";
    }

    def doOk() {
        service.addNewBatch( entry );
        handler( entry );
        return "_close";
    }
    
}    