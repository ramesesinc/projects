package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BillDispatcherReceipt {
    
    @Service('WaterworksBillDispatcherReceiptService') 
    def reportSvc; 
    
    def buildReport( param ) { 
        reportSvc.process( param ); 
        return null; 
    } 
}