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
    
    @Service("BasicCapturePaymentService")
    def paySvc;
        
    def parent;
    def txnid;
    def selectedItem;    
    def entity = [reftype: 'cashreceiptcapture'];
        
    boolean amountSpecified = false;

    public String getRulename() {
        String s = invoker.properties.rulename;
        if( s!=null ) {
            return s;
        }
        s = workunit?.info?.workunit_properties?.rulename;
        if( s != null ) return s;
    }

    public String getTxntype() {
        String s = invoker.properties.txntype;
        if( s!=null ) {
            return s;
        }
        s = workunit?.info?.workunit_properties?.txntype;
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
    void beforeSave() {} 
    void afterSave() {} 
    
    void init() {
        def p = [:];
        buildParams( p ); 

        if(!txnid) {
            def opener = null;
            try {
                def h = { o->
                    txnid = o?.objid; 
                    return null;
                }
                opener = Inv.lookupOpener(getContextName() + ":cashreceipt_lookup", [onselect: h ]);
                Modal.show( opener );
            }catch(ign){;}
            if( opener==null && txnid==null ) {
                txnid = MsgBox.prompt("Enter Transaction No");
            }
            if(!txnid) throw new BreakException();
        }
        
        p.action = 'open';
        p.id = txnid;
        p.txntype = txntype;
        loadInfo( p );
    }
        
    void loadInfo(def p) {
        beforeLoadInfo( p );
        
        def pp = [ rulename: getRulename(), params: p ]; 
        def info = paySvc.getBillingInfo( pp );
        entity.putAll( info );
        entity.billitems.each { 
            it.selected = true; 
            if ( it.amount == null ) it.amount = 0.0; 
            if ( it.discount == null ) it.discount = 0.0; 
            if ( it.surcharge == null ) it.surcharge = 0.0; 
            if ( it.interest == null ) it.interest = 0.0; 
            println it;
        }
        
        afterLoadInfo(); 
        reloadItems();
    }
    
    void reloadItems() {
        updateTotals(); 
        listHandler.reload();
        if( binding ) binding.refresh();
    } 
    
    public void updateTotals() {
        def total = entity.billitems.sum{( it.total ? it.total : 0.0 )}
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
        beforeSave(); 
        updateTotals(); 
        entity.txntype = getTxntype(); 

        def items = entity.billitems.findAll{ it.selected == true } 
        if ( !items ) throw new Exception('Please select at least one item'); 
        
        if ( entity.totalamount==null || entity.amount <= 0 ) 
            throw new Exception('Total Amount must be greater than zero'); 
        if ( entity.amount != entity.totalamount ) 
            throw new Exception('Amount must be equal to the Total Amount'); 
            
        if ( MsgBox.confirm('You are about to post this payment. Continue?')) { 
            def pmt = [:]; 
            pmt.putAll( entity ); 
            pmt.billitems = items; 
            paySvc.post( pmt ); 
            afterSave(); 
            
            try {
                if ( caller ) caller.reload(); 
            } catch(Throwable t){;} 

            return '_close'; 
        }
        return null; 
    }
}