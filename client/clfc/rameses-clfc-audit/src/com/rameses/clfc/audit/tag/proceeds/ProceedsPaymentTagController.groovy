package com.rameses.clfc.tag.proceeds

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.audit.tag.*;

class ProceedsPaymentTagController extends AbstractPaymentTagController
{
    String title = "Payment Proceeds Tag";
    
    String serviceName = "CaptureLoanLedgerProceedsTagService";
    
    def addTag() {
        if (!selectedPayment) throw new Exception("Please select payment.");
        
        def handler = { remarks->
            selectedPayment.description = remarks;
            service.addTag(selectedPayment);
            paymentsHandler?.reload();
        }
        return Inv.lookupOpener('remarks:create', [title: 'Proceed Description', handler: handler]);
    }
}

