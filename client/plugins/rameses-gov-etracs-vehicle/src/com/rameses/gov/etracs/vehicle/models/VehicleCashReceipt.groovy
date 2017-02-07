package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class VehicleCashReceipt extends AbstractSimpleCashReceiptModel {
    
    @Service("VehicleCashReceiptService")
    def cashReceiptSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Vehicle Registration";
    
     public String getContextName() {
         return "vehicle";
     }
    
     public def getPaymentInfo( def app ) {
         return cashReceiptSvc.getInfo( app );
     }
          
    
}