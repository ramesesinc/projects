package com.rameses.gov.police.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class PoliceClearanceCashReceiptModel extends AbstractSimpleCashReceiptModel {
    
     @Service("PoliceClearanceCashReceiptService")
     def cashReceiptSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Police Clearance";
    
     public String getContextName() {
         return "policeclearance";
     }
     
     public def getPaymentInfo( def app ) {
         return cashReceiptSvc.getInfo( app );
     }
    
}


