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
    def miscAmount = 0;
    
    def miscList = [];
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
    
    void loadInfo(def p) {
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.receiptdate;
        p.rulename = getRulename();
        def info = cashReceiptSvc.getInfo( p );
        billItemList = info.items;
        entity.putAll(info);
        reloadItems(); 
        //afterLoadInfo();
        //loadPayOptions();
    }
    
    void reloadItems() {
        entity.items = [];
        entity.items.addAll( billItemList );
        if( miscList ) {
            entity.items.addAll( miscList );
        }
        itemListModel.reload();
        miscListModel.reload();
        if(binding) binding.refresh();
        updateBalances();
    }
    
    public void updateBalances() {
        billAmount = NumberUtil.round( billItemList.sum{ it.amount } );
        miscAmount = 0;
        if( miscList ) {
            miscAmount = NumberUtil.round( miscList.sum{ it.amount } );
        }
        //entity.amount = NumberUtil.round( entity.items.sum{ it.amount } );  
        super.updateBalances();
    }
    
    void init() {
        def o = MsgBox.prompt("Enter Transaction No");
        if(!o) throw new BreakException();
        txnid = o;
        loadInfo([id:txnid, action:'open']);
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
          
    
    def selectedMiscItem;
    def getLookupItems() {
        def n = contextName + "_miscitem:lookup";
        try {
            return InvokerUtil.lookupOpener( n, [ 
                onselect:{ o-> 
                    selectedMiscItem.item = o; 
                    selectedMiscItem.amount = o.defaultvalue; 
                } 
            ]); 
        }
        catch(e) {
            MsgBox.err( "No lookup handler found for " + n );
        }
    } 
    
    def miscListModel = [
        fetchList: { o-> 
            return miscList;
        }, 
        isColumnEditable: { o,colName ->
            return (amountSpecified == false);
        },
        onAddItem: {o-> 
            o.objid = 'RCTI' + new java.rmi.server.UID();
            miscList << o; 
            reloadItems();
        },
        onColumnUpdate: {o,name-> 
            updateBalances();
        },
        onRemoveItem: { o->
            if ( !MsgBox.confirm("You are about to remove this entry. Continue?")) return false;
            miscList.remove( o );
            reloadItems();
            return true;
        }        
    ] as EditorListModel;
    
}