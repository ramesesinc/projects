package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

/******************************************************************************
* This is used for cash receipt where it checks for the info then items 
* are added just like the misc cash receipt
******************************************************************************/
public class LedgerCashReceiptModel extends BasicCashReceipt {
    
    @Controller
    def workunit;
        
    @Invoker
    def invoker;
    
    @Service("BillingCashReceiptService")
    def cashReceiptSvc;
    
    String entityName = "misc_cashreceipt"
    def prefix;
    def barcodeid;

    def txnid; 
    
    public String getTitle() {
        if( invoker.properties.formTitle ) {
            return ExpressionResolver.getInstance().evalString(invoker.properties.formTitle,this);
        }
        if( invoker.caption ) {
            return invoker.caption;
        }
        return getContextName();
     }
    
     public String getContextName() {
        def pfn = invoker.properties.contextName;
        if(pfn) return pfn;
        pfn = workunit?.info?.workunit_properties?.contextName;
        if ( pfn ) return pfn; 
        return super.getSchemaName(); 
    }
    
    public String getRulename() {
        String s = invoker.properties.rulename;
        if( s!=null ) {
            return s;
        }
        s = workunit?.info?.workunit_properties?.rulename;
        if( s != null ) return s;
    }

    public String getDetails() {
        return "Details";
    }

    void init() {
        def opener = null;
        try {
            def h = { o->
                txnid = o; 
            }
            opener = Inv.lookupOpener(getContextName() + ":cashreceipt_lookup", [onselect: h ]);
            Modal.show( opener );
        }catch(ign){;}
        
        if( !txnid ) {
            txnid = MsgBox.prompt("Enter Transaction No");
        }
        if(!txnid) throw new BreakException();
        loadInfo([id:txnid, action:'open']);
    }
    
    void loadInfo(def p) {
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.receiptdate;
        p.rulename = getRulename();
        def info = cashReceiptSvc.getInfo( p );
        entity.putAll(info);
        entity.items = [];
        entity.paymentitems = [];
    }
    
    
}