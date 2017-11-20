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

    def findCollectionTypeParams( def finder ) {
        //find collection type based on the prefix
        def p = [_schemaname: 'collectiontype'];
        p.findBy = finder;
        def res = qrySvc.findFirst( p );
        def params = [:];
        params.txnmode = "ONLINE";
        params.formno = res.af.objid;
        params.formtype = res.af.formtype;
        params.collectiontype = [objid:res.objid, title: res.title, handler: res.handler, name: res.name ];
        return params;
    } 
    
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
            prefix = po.collectiontype.barcodekey 
            barcodeid = po.refno;
            
            //this is usually for general collections
            if(barcodeid==null) {
                def parms = findCollectionTypeParams( [ objid: po.collectiontype.objid ] );
                return findOpener( po,  parms, po.objid ); 
            }
        }

        
        try {
            if(!prefix) {
                def pp = barcodeSvc.findPrefix( [barcodeid: barcodeid] );
                prefix = pp.prefix;
            }
            if(!prefix) 
                throw new Exception("There is no handler found for requested entry");
            
            def binfo = barcodeSvc.init( [barcodeid: barcodeid, prefix: prefix] );
            def m = [barcodeid: barcodeid, prefix: prefix, _paymentorderid:po?.objid];
            m.entity = binfo;
            m.info = po?.info;
             
            return InvokerUtil.lookupOpener( "cashreceipt:barcode:"+prefix, m);
        }
        catch(BreakException be) { 
            return null;
        }    
        catch(Warning w) { 
            def parms = findCollectionTypeParams( [ barcodekey: prefix  ] );
            String m = "cashreceipt:" + w.message;
            return InvokerUtil.lookupOpener(m, [entity: parms]);
        }
        catch(e) {
            //MsgBox.err("No appropriate handler found for this particular barcode.[ cashreceipt:barcode:"+prefix+"]->"+barcodeid);
            MsgBox.err( e );
            return null;
        }
    }
        
    //This is generally for miscellaneous general collection
    def findOpener( def entity, def params, paymentOrderId ) {
        try {
            def info = cashReceiptSvc.init( params );
            def e = [:];
            e.putAll( info );
            e.payer = entity.payer;
            e.paidby = entity.paidby;
            e.paidbyaddress = entity.paidbyaddress;
            e.amount = entity.amount;
            e.info = entity.info;
            e.items = entity.items.collect{ [item:it.item, amount:it.amount, remarks:it.remarks ] };
            e.collectiontype = params.collectiontype;
            def openerParams = [entity: e, _paymentorderid: paymentOrderId]; 
            return Inv.lookupOpener("cashreceipt:"+ params.collectiontype.handler, openerParams);  
        } 
        catch(BreakException be) { 
            return null;
        } catch(Warning w) { 
            String m = "cashreceipt:" + w.message;
            return InvokerUtil.lookupOpener(m, [entity: params]);
        }
    } 
        
        
}       