package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class AFReceiptItemDetailModel  {

    @Binding
    def binding;

    @Service("QueryService")
    def querySvc;

    def entity;
    def entry = [:];
    int unitqty;
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
            if(o.length() != entity.item.serieslength ) {
                throw new ValidatorException("Start series length must be " + entity.item.serieslength );
            }      
        }
    ];


    def formatSeries = { o->
        return o.toString().padLeft( entity.item.serieslength, '0');
    }

    @PropertyChangeListener
    def listener = [
        "entry.qty" : { o->
            if(entry.startseries) entry.endseries =   formatSeries( Integer.parseInt(entry.startseries) + (o * unitqty) - 1) ; 
            if( entry.startstub ) entry.endstub = entry.startstub + o - 1;
        },
        "entry.startseries" : { o->
            entry.endseries = formatSeries( Integer.parseInt(o) + (entry.qty * unitqty ) - 1 ); 
        },
        "entry.startstub" : { o->
            entry.endstub = o + entry.qty - 1;
        }
    ];


    void init() {
        entry = [:];
        entry.qty = entity.qtyrequested - entity.qtyreceived;
        def m = [_schemaname:'afunit'];
        m.findBy = [ itemid: entity.item.objid , unit: entity.unit  ];
        unitqty = querySvc.findFirst(m).qty;
    }

    def doCancel() {
        return "_close";
    }

    def doOk() {
        entry.ref = entity;
        entry.ref.unitqty = unitqty;
        handler( entry );
        return "_close";
    }
    
}    