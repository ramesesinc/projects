package com.rameses.gov.epayment.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EORPaymentOrderListModel extends CrudListModel  {

    @Service("OnlinePaymentResolverService")
    def onlineSvc;

    public void resolveOnlinePayment() {
        onlineSvc.resolve( [:] );
    }
}    