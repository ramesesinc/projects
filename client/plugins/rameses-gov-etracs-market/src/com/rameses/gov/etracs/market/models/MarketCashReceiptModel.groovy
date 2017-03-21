package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class MarketCashReceiptModel extends AbstractSimpleCashReceiptModel {
    
     @Service("MarketCashReceiptService")
     def cashReceiptSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Market Rental";
    
     public String getContextName() {
         return "market";
     }
     
     public def getPaymentInfo( def app ) {
         return cashReceiptSvc.getInfo( app );
     }
    
}

