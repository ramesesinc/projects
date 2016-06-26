package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.enterprise.treasury.models.*;
                
public class WaterworksCapturePayment extends CapturePaymentModel {
    
    public void beforeInit() {
        query.acctid = caller.getMasterEntity().objid;
        handler = {
            try { caller.reload(); } catch(e) {;}
        }
    }
}