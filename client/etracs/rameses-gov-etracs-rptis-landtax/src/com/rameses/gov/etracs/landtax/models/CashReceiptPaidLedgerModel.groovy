package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
class CashReceiptPaidLedgerModel  {
    @Service('RPTReceiptService')
    def svc;

    String title = 'List of Paid Ledgers'
    
    def entity;
    def ledgers;

    void init() {
        ledgers = svc.getPaidLedgers([objid: entity.objid]);
    }

    def listHandler = [
        fetchList: { ledgers }
    ] as BasicListModel
}