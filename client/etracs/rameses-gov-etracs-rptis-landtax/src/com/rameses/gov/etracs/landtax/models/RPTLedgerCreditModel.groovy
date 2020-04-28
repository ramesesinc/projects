package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class RPTLedgerCreditModel
{
    @Service('RPTTaxCreditService')
    def svc; 
    
    def entity;
    def credits;
    def selectedItem;
    
    void init() {
        credits = svc.getCredits([objid: entity.objid]);
    }

    def viewCredit() {
        if (selectedItem) {
            def inv = Inv.lookupOpener('rpttaxcredit:open', [entity: selectedItem]);
            inv.target = 'popup';
            return inv;
        }
    }

    def listHandler = [
        getRows: { credits.size() },
        fetchList: { credits }
    ] as BasicListModel;
}