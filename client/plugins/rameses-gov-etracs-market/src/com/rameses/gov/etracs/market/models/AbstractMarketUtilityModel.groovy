package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
         
public abstract class AbstractMarketUtilityModel extends CrudFormModel {

    @Service("MarketUtilityService")
    def svc;

    @Caller
    def caller;

    def oldEntity;
    int year;
    int month;
    def readingdate;
    
    public abstract String getTxntype();
    
    @PropertyChangeListener
    def listener = [
        "entity.reading" : { o->
            calcAmount();
            //entity.usage = 0;
            //entity.amount = 0;
            //entity.amtpaid = 0;
        },
        "entity.account" : { o->
            entity.prevreding = o.electricityreading;
            entity.acctid = o.objid;
        },
        "entity.year" : { o->
            year = o;
        },
        "entity.month" : { o->
            month = o;
        },
        "entity.readingdate": { o->
            readingdate = o;
        }
    ];
    
    void afterCreate() {
        try {
            oldEntity = caller?.masterEntity;
        }catch(e){;}
        if( oldEntity != null ) {
            entity.account = oldEntity;
            entity.prevreading = oldEntity.electricityreading;
        }
        if(year>0) entity.year = year;
        if(month>0) entity.month = month;
        if(readingdate) entity.readingdate = readingdate;
    }

    void afterOpen() {
        entity._reading = entity.reading; 
        oldEntity = entity.account;
    }

    void calcAmount() {
        if(  entity.prevreading > entity.reading ) {
            throw new Exception("Current reading must be greater than previous");
        }
        entity.usage = entity.reading - entity.prevreading;
        def p = [txntype:getTxntype()];
        p.usage = entity.usage;
        def z = svc.calculate( p ); 
        entity.amount = z.amount;
        entity.amtpaid = 0;
    }

}