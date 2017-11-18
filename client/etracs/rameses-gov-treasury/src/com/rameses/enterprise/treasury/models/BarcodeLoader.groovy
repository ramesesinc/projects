package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
        
public class BarcodeLoader {
        
    @Service("CashReceiptBarcodeService")
    def barcodeSvc;

    @Service('QueryService')
    def qrySvc 

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
        
        def po = null;
        if ('PMO'.equalsIgnoreCase(prefix)){
            def q = [:]
            q._schemaname = 'paymentorder'
            q.findBy = [objid:p];
            q.debug = true;
            po = qrySvc.findFirst(q);
            if (!po){
                q.findBy = [objid:barcodeid]
                po = qrySvc.findFirst(q)
            }
            if (!po) throw new Exception('Payment Order does not exist.')
            def colltype = po.collectiontype;
            
            prefix = colltype.barcodekey 
            barcodeid = po.refno 
        }

        try {
            if(!prefix) {
                def pp = barcodeSvc.findPrefix( [barcodeid: barcodeid] );
                prefix = pp.prefix;
            }
            if(!prefix) 
                throw new Exception("There is no handler found for requested entry");
            
            def e = barcodeSvc.init( [barcodeid: barcodeid, prefix: prefix] );
            def m = [barcodeid: barcodeid, prefix: prefix, _paymentorderid:po?.objid];
            m.entity = e;
            m.info = po?.info;
             
            return InvokerUtil.lookupOpener( "cashreceipt:barcode:"+prefix, m);
        }
        catch(e) {
            //MsgBox.err("No appropriate handler found for this particular barcode.[ cashreceipt:barcode:"+prefix+"]->"+barcodeid);
            MsgBox.err( e );
            return null;
        }
    }
}       