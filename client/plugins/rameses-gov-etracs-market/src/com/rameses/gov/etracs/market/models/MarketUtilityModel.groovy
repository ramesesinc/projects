package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
         
public class MarketUtilityModel extends CrudFormModel {

    @Service("MarketUtilityService")
    def svc;

    void afterCreate() {
        if( !entity.account ) {
            entity.type = invoker.properties.tag?.toUpperCase();
        }
    }
    
    void calcNextReadingDate() {
        def p = [ year:entity.year, month:entity.month, txntype:entity.type ];
        def z = svc.calculate( p ); 
        entity.nextreadingdate = z.nextreadingdate; 
        binding.refresh('entity.nextreadingdate'); 
    }
}    