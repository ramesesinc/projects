package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public class BillingCapturePaymentModel {
    
    @Controller
    def workunit;
        
    @Invoker
    def invoker;
    
    @Service("BillingCapturePaymentService")
    def capturePaymentSvc;
    
     //we specify this so print detail will appear.
    
    def page = "initial";
    def txntype;
    def rulename;
    def txnid;
    def payOption;
    def selectedItem;
    def amount;
    def entity = [:];
    
    def _payOptions;
    
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
     
    public def init() {
        txntype = invoker.properties.txntype;
        if(!txntype) txntype = workunit?.info?.workunit_properties?.txntype;
        if(!txntype) throw new Exception("Please specify txntype in workunit or invoker for Billing Capture Payment");
        
        rulename = invoker.properties.rulename;
        if(!rulename) rulename = workunit?.info?.workunit_properties?.rulename;
        if(!rulename) throw new Exception("Please specify rulename in workunit or invoker for Billing Capture Payment");
        
        page = "initial";
        return page;
    }

    void loadItems( def p ) {
        def b = capturePaymentSvc.getInfo( [id: txnid, txntype: txntype, rulename: rulename ] );
        entity.billitems = b.billitems;
        entity.amount = b.amount;
    }
    
    public def doNext() {
        loadItems( [:] );
        /*
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.receiptdate;
        def info = cashReceiptSvc.getInfo( p );
        entity.putAll(info);
        reloadItems(); 
        afterLoadInfo();
        loadPayOptions();
        */
        page = "entry";
        return page;
    }
    
    
    
    /*
    public void loadPayOptions() {
        def ivf = [
            accept: { vv->
                String vwhen = vv.properties.visibleWhen;
                if(vwhen) {
                    return ExpressionResolver.instance.evalBoolean( vwhen, [entity: entity] );
                }
                return true;
            }
        ] as InvokerFilter;
        def list;
        try {
            list = Inv.lookupOpeners("simple_cashreceipt_payoption_type", [context: this])
        }catch(e){;}
        try {
            def mlist = Inv.lookupOpeners(getContextName()+":cashreceipt_payoption_type", [context: this], ivf);
            if(mlist) list += mlist;
        }catch(e){;}
        _payOptions = list;
    }
    */
    
    public List getPayOptions() {
        return _payOptions;
    }
    
    
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    def showPayOption() {
        if( getPayOptions()==null) return null;
        def m = [:];
        m.payOptions = getPayOptions();
        m.onselect = { o->
            loadInfo( [id: txnid, payment: o, action:'payoption'] );
        }
        return Inv.lookupOpener( "simple_cashreceipt_payoption", m);
    }
    
    def resetPayOption() {
        loadInfo( [id: txnid] );
    }
    
    public void validateBeforePost() {
        if( entity.balancedue !=  0 )
            throw new Exception("Amount must be equal to amount paid");
    }
    
    void reloadItems() {
        if(!entity.items) throw new Exception("There must be at least 1 item to pay");
        entity.amount = NumberUtil.round( entity.items.sum{ it.amount } );  
        updateBalances();
        itemListModel.reload();
    }
     
    def itemListModel = [
        fetchList: { o->
            return entity.billitems;
        },
        
    ] as EditorListModel;
          
    
}