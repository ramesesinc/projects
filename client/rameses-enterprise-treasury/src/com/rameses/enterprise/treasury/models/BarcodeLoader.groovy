package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
        
public class CashReceiptBarcode {
        
    @Service("CashReceiptBarcodeService")
    def barcodeSvc;

    def init() {
        def p = MsgBox.prompt("Enter barcode");
        if(!p) return null;

        def prefix = null; 
        def barcodeid = null; 
        int i = p.indexOf(":");
        if( i <=0 ) {
            barcodeid = p.trim(); 
        } else {
            prefix = p.substring(0,i).trim(); 
            barcodeid = p.substring(i+1).trim();
        } 
        try {
            if(!prefix) {
                def pp = barcodeSvc.findPrefix( [barcodeid: barcodeid] );
                prefix = pp.prefix;
            }
            if(!prefix) 
                throw new Exception("There is no handler found for requested entry");
            
            def e = barcodeSvc.init( [barcodeid: barcodeid, prefix: prefix] );
            def m = [barcodeid: barcodeid];
            m.entity = e;
             
            return InvokerUtil.lookupOpener( "cashreceipt:barcode:"+prefix, m);
        }
        catch(e) {
            //MsgBox.err("No appropriate handler found for this particular barcode.[ cashreceipt:barcode:"+prefix+"]->"+barcodeid);
            MsgBox.err( e );
            return null;
        }
    }
}       