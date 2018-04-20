package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class BillingCashReceiptModel extends AbstractCashReceipt {
    
    @Controller
    def workunit;
        
    @Invoker
    def invoker;
    
    @Service("BillingCashReceiptService")
    def cashReceiptSvc;
    
    String entityName = "misc_cashreceipt"
    def prefix;
    def barcodeid;

    def status;   
    def selectedItem;
    def txnid;
    
    def billAmount = 0;
    
    def billItemList = [];
    
    
    boolean amountSpecified = false;
    
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
    
    public boolean getAllowDeposit() {
        return true;
    }
    
    def _payOption = null;
    public String getPayOption() {
        if(_payOption == null ) {
            _payOption = invoker.properties.payOption;
            if(!_payOption ) _payOption = workunit?.info?.workunit_properties?.payOption;
        } 
        return _payOption; 
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

    void afterLoadInfo() {;}
    boolean onNoItemsFound() { return false;}
    
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
        MsgBox.alert( "enter aaa");
        if(!txnid) throw new BreakException();
        loadInfo([id:txnid, action:'open']);
    }
    
    void loadInfo(def p) {
        MsgBox.alert( "enter bbb");
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.receiptdate;
        p.rulename = getRulename();
        MsgBox.alert( "enter 0");
        def info = cashReceiptSvc.getInfo( p );
        MsgBox.alert( "enter 01");
        if( !info.billitems ) {
            MsgBox.alert( "enter 1");
            if( getAllowDeposit() ) {
                def amt = MsgBox.prompt( "Enter amount for advance payment. ");
                if(!amt) throw new BreakException();
                amt = new BigDecimal(""+amt);
                MsgBox.alert( "amount is " + amt );
                p.amtpaid = amt;
                info = cashReceiptSvc.getInfo( p );
            }
            else {
                throw new Exception("No bill items found");
            }
        }
        entity.putAll(info);
        billItemList = info.items;
        reloadItems(); 
        //afterLoadInfo();
        //loadPayOptions();
    }
    
    void reloadItems() {
        entity.items = [];
        entity.items.addAll( billItemList );
        itemListModel.reload();
        if(binding) binding.refresh();
        updateBalances();
    }
    
    public void updateBalances() {
        billAmount = NumberUtil.round( billItemList.sum{ it.amount } );
        super.updateBalances();
    }
    
    void loadBarcode() {
        txnid = barcodeid;
        loadInfo( [id: txnid, action:'barcode'] );
    }   
    
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
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
        if(!o) return null;
        def p = [amtpaid: o, id:txnid, action:'open' ];
        loadInfo( p );
        amountSpecified = true;
    }
    
    void payAll() {
        def p = [id:txnid, action:'open' ];
        loadInfo( p );
        amountSpecified = false;
    }
    
    def resetPayOption() {
        loadInfo( [id: txnid] );
    }
    
    public void validateBeforePost() {
        if( entity.balancedue !=  0 )
            throw new Exception("Amount must be equal to amount paid");
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.billitems;
        }
    ] as BasicListModel;
          
    
}