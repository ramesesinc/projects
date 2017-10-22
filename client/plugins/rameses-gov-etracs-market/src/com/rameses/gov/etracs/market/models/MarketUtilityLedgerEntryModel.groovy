package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
         
public class MarketUtilityLedgerEntryModel extends CrudFormModel {

    @Service("MarketUtilityService")
    def svc;
    
    void afterCreate() {
        entity.txntypeid = caller.masterEntity.type;
        entity.parentid = caller.masterEntity.objid;
        int ym = (caller.masterEntity.year*12)+caller.masterEntity.month;
        entity.year = (int)((ym+1)/12);
        entity.month = (ym+1) % 12;
        entity.prevreading = caller.masterEntity.currentreading;
    }
    
    void calcAmount() {
        if(  entity.prevreading > entity.reading ) {
            throw new Exception("Current reading must be greater than previous");
        }
        entity.usage = entity.reading - entity.prevreading;
        def p = [txntype:entity.txntypeid,usage: entity.usage, year:entity.year, month:entity.month];
        def z = svc.calculate( p ); 
        entity.amount = z.amount;
        entity.amtpaid = 0;
    }        
            
}    