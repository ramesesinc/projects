package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
         
public class MarketChangeActionModel extends CrudFormModel {

    String txntype;
    def oldEntry;
    
    def initEdit() {
        txntype = invoker.properties.txntype;
        oldEntry = entity;
        entity = [objid: oldEntry.objid, _schemaname: schemaName];
        if( txntype == "owner") {
            entity.owner = oldEntry.owner;
            entity.acctname = oldEntry.acctname;
            entity.remarks = oldEntry.remarks;
        };
        else if( txntype == "rentalunit") {
            entity.unit = oldEntry.owner;
            entity.extarea = oldEntry.extarea;
            entity.extrate = oldEntry.extrate;
        }
        else if(txntype=="ledger" ) {
            entity.payfrequency = oldEntry.payfrequency;
            entity.startdate = oldEntry.startdate;
            entity.month = oldEntry.month;
            entity.year = oldEntry.year;
            entity.partialbalance = oldEntry.partialbalance;
        }
        return txntype;
    }

    @PropertyChangeListener
    def listener = [
        'entity.owner': { o->
            if(entity.acctname==null) {
                entity.acctname = o.name;
                binding.refresh("entity.acctname");
            }
        },
        'entity.unit' : { o->
            entity.unitno = o.code;
        }        
    ]; 
    
    void selectBusiness() {
        def r = { o->
            entity.business = o;
            return null;
        }
        Modal.show("market_business_unassigned:lookup", [onselect:r] );
    }
    
    def doOk() {
        persistenceService.update( entity );
        caller.reloadEntity();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
    
}