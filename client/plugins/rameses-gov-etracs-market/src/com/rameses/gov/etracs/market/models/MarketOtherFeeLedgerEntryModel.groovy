package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.common.*;
import com.rameses.util.*;

public class MarketOtherFeeLedgerEntryModel extends CrudFormModel {
    
    def saveHandler;
    def parent;
    
    @PropertyChangeListener
    def listener = [
        "entity.txntype" : { o->
            entity.txntypeid = o.objid;
        }
    ];

    void afterOpen() {
        entity.txntype = [objid: entity.txntypeid];
    }

    void afterCreate() {
        entity.acctid = parent.objid;       
    }
    
    void afterSave() {
        saveHandler();
    }
    
}