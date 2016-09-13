package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.enterprise.treasury.cashreceipt.*
        
public class  PaymentOrderCashReceiptModel extends BasicCashReceipt {

     @Service("CashReceiptBarcodeService")
     def barcodeService;

     String title = "Payment Order";
     String entityName = "misc_cashreceipt"
     def barcodeid;

     void init() {
         if(!barcodeid) {
             boolean pass = false;
             def s = { o->
                 entity = barcodeService.init( [prefix:'PMO', barcodeid: o.controlno ] );
                 pass = true;
             }
             Modal.show( "paymentorder:lookup", [onselect:s] );
             if( !pass ) throw new BreakException();
         }
         super.init();
     }

 }