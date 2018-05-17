package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
        
public class BarcodeLoader {
        
    @Service("CashReceiptService")
    def cashReceiptSvc;
    
    @Service("CashReceiptBarcodeService")
    def barcodeSvc;

    @Service('QueryService')
    def qrySvc 
    
    def handler;

    void init() {
        def p = MsgBox.prompt("Enter barcode");
        if(!p) return null;
        def prefix = null; 
        def barcodeid = null; 
        int i = p.indexOf(":");
        if( i <=0 ) {
            barcodeid = p.trim(); 
            def m = [_schemaname: 'paymentorder'];
            m.findBy = [objid: barcodeid];
            def po = qrySvc.findFirst( m );
            if ( !po ) throw new Exception('Order of payment '+ barcodeid + ' not found'); 
            def v = [
                prefix: po.collectiontype.barcodekey,
                barcodeid:po.refno,
                collectiontype  : po.collectiontype,
                info: po, 
                _paymentorderid : barcodeid 
            ];
            handler( v );            
        } 
        else {
            prefix = p.substring(0,i).trim(); 
            barcodeid = p.substring(i+1).trim();
            def m = [_schemaname: 'collectiontype'];
            m.findBy = [barcodekey: prefix];
            def ct = qrySvc.findFirst( m );
            def v = [
                prefix: prefix,
                barcodeid:barcodeid,
                collectiontype  : ct
            ];
            handler( v );
        } 
    }
}       