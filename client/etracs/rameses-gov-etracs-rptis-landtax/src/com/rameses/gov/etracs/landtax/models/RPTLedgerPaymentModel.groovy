package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerPaymentModel
{
    @Service('RPTLedgerPaymentService')
    def svc; 
    
    def entity;
    def selectedItem;
    def payments;
    
    void init(){
        payments = svc.getPayments([objid:entity.objid]);
    }
    
    def listHandler = [
        fetchList :{ payments }
    ] as BasicListModel
    
    
    void refresh(){
        init();
        listHandler.reload();
    }
    
    def viewDetails(){
        if (!selectedItem) return;
        return Inv.lookupOpener('rptledger:section:paymentdetail', [payment:selectedItem]);
    }
}