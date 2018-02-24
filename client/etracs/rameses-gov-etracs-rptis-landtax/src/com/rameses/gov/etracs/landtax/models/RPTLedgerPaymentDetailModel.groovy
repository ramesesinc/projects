package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerPaymentDetailModel
{
    @Service('RPTLedgerPaymentService')
    def svc; 
    
    String title = 'Payment Details'
    
    def payment;
    
    def items;
    def shares;
    
    void init(){
        def data = svc.getPaymentDetails([objid:payment.objid]);
        items = data.items;
        shares = data.shares;
    }
    
    def paymentListHandler = [
        fetchList :{ items }
    ] as BasicListModel
    
    def shareListHandler = [
        fetchList :{ shares }
    ] as BasicListModel
}