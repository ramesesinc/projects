package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public class BasicBillingCapturePaymentModel {
    
    @Controller
    def workunit;
        
    @Invoker
    def invoker;
    
    @Binding
    def binding; 
    
    @Caller 
    def caller; 
    
    @Service("BasicBillingService")
    def billingSvc;
        
    def parent;
    def status;   
    def selectedItem;
    def txnid;
    def amount;
    def refid;
    
    def entity = [reftype:'cashreceipt'];
    def billAmount = 0;
    def miscAmount = 0;
    
    def miscList = [];
    def billItemList = [];
    
    def amtpaid = 0;
    
    boolean amountSpecified = false;

    public String getRulename() {
        String s = invoker.properties.rulename;
        if( s!=null ) {
            return s;
        }
        s = workunit?.info?.workunit_properties?.rulename;
        if( s != null ) return s;
    }

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
        if ( pfn ) return pfn; 
        
        pfn = workunit?.info?.workunit_properties?.contextName;
        if ( pfn ) return pfn; 
        
        return null; 
    }
    
    def _payOption = null;
    public String getPayOption() {
        if(_payOption == null ) {
            _payOption = invoker.properties.payOption;
            if(!_payOption ) _payOption = workunit?.info?.workunit_properties?.payOption;
        } 
        return _payOption; 
    }
        
    
    public String getDetails() {
        return "Details";
    }

    void buildParams( p ) {;}
    void beforeLoadInfo( p ) {;}
    void afterLoadInfo() {;}
    
    void init() {
        def p = [:];
        buildParams( p ); 
        if ( !p.txnid ) throw new Exception('Please provide txnid parameter'); 
        
        p.action = 'open';
        p.id = p.txnid;
        txnid = p.id;
        loadInfo( p );
    }
        
    void loadInfo(def p) {
        beforeLoadInfo( p );
        
        def pp = [ rulename: getRulename(), params: p ]; 
        def info = billingSvc.getBillingInfo( pp );
        entity.putAll( info );
        billItemList = info.billitems;
        billItemList.each{ 
            it.selected = true; 
            if ( it.amount == null ) it.amount = 0.0; 
            if ( it.discount == null ) it.discount = 0.0; 
            if ( it.surcharge == null ) it.surcharge = 0.0; 
            if ( it.interest == null ) it.interest = 0.0; 
        }
        
        afterLoadInfo(); 
        reloadItems();
    }
    
    void reloadItems() {
        entity.items = [];
        entity.items.addAll( billItemList );
        updateTotals(); 
        listHandler.reload();
        if( binding ) binding.refresh();
    } 
    
    public void updateTotals() {
        def total = entity.items.sum{( it.total ? it.total : 0.0 )}
        entity.totalamount = (total == null ? 0.0 : total); 
        entity.totalamount = NumberUtil.round( entity.totalamount );  
        if ( binding ) binding.notifyDepends('totals');
    } 

    def showPayOptions() {
        if( amountSpecified ) 
            throw new Exception("Please reset amount specified first to Pay All");
        if( !getPayOption() ) return null;
        def m = [:];
        m.onselect = { o->
            loadInfo( [id: txnid, action:'payoption', payoption: o ] );
        }
        return Inv.lookupOpener( getPayOption(), m);
    }
    
    void specifyPayAmount() {
        def o = MsgBox.prompt("Enter Pay Amount");
        if ( !o ) return null;
        
        def p = [amtpaid: o, id:txnid, action:'open' ];
        loadInfo( p );
        amountSpecified = true;
    }
    
    void payAll() {
        def p = [ id: txnid, action: 'open' ];
        loadInfo( p );
        amountSpecified = false;
    }
    
    def resetPayOption() {
        loadInfo( [id: txnid] );
    }
    
    def listHandler = [
        getRows: { 
            return 10; 
        }, 
        fetchList: { o->
            return entity.billitems;
        }, 
        isAllowAdd: {
            return false; 
        },
        isColumnEditable: { item, colname->  
            if ( colname == 'selected' ) return true; 
            return (item.selected ? true : false); 
        },
        afterColumnUpdate: { item, colname-> 
            updateItemTotal( item ); 
            updateTotals(); 
        }
    ] as EditorListModel;
    
    void updateItemTotal( item ) {
        item.total = 0.0; 
        if ( item.selected ) {
            item.total = (item.amount - item.discount) + item.surcharge + item.interest; 
        }
    }
    
    def save() {
        if ( MsgBox.confirm('You are about to post this payment. Continue?')) { 
            
            return '_close'; 
        }
        return null; 
    }
}