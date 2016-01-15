package com.rameses.tag.noncash

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.audit.tag.*;

class NoncashPaymentTagController extends AbstractPaymentTagController
{
    String title = "Payment Non-cash Tag";
    
    String serviceName = "CaptureLoanLedgerNoncashTagService";
}

