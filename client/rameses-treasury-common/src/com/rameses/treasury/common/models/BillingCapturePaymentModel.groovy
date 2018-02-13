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
    
    @Binding
    def binding;  
    
    @Service("BillingCapturePaymentService")
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
    def refTypes = ["cashreceipt", "adjustment","creditpayment"];
    
    def miscList = [];
    def billItemList = [];
    
    def amtpaid = 0;
    
    boolean isAmountSpecified() {
        return (amtpaid != 0 ); 
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

    public String getContextName() {
        def pfn = invoker.properties.contextName;
        if(pfn) return pfn;
        pfn = workunit?.info?.workunit_properties?.contextName;
        if ( pfn ) return pfn; 
        return super.getSchemaName(); 
    }
    
    
    public String getDetails() {
        return "Details";
    }

    void afterLoadInfo() {;}
    
    void loadInfo(def p) {
        if( parent?.objid ) {
            p.objid = parent.objid;
        }
        else {
            p.txnid = txnid;
        }
        if(amtpaid > 0 ) p.amtpaid = amtpaid;
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.refdate;
        p.rulename = getRulename();
        p.txntype = contextName;
        def info = billingSvc.getInfo( p );
        billItemList = info.billitems;
        entity.putAll(info);
        entity.objid = null;
        
        //place this everytime billing is done.
        if(amtpaid > 0 && entity.reftype == 'creditpayment' ) {
            //remove credit in billitems that are more than the amount
            def adv = entity.billitems.find{ it.txntype == 'credit' };
            if( adv ) entity.billitems.remove( adv );
            updateBalances();
            amount = entity.amount;
            amtpaid = entity.amount;
        }
        
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
        billAmount = NumberUtil.round( billItemList.sum{ it.total } );
        miscAmount = 0;
        if( miscList ) {
            miscAmount = NumberUtil.round( miscList.sum{ it.amount } );
        }
        entity.amount = billAmount + miscAmount;
    }

    void init() {
        def objid = parent?.objid;
        if(!objid) {
            def o = MsgBox.prompt("Enter Transaction No");
            if(!o) throw new BreakException();
            txnid = o;
        }
        loadInfo([action:'open']);
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
            loadInfo( [action:'payoption', payoption: o ] );
        }
        return Inv.lookupOpener( getPayOption(), m);
    }
    
    void specifyPayAmount() {
        def o = MsgBox.prompt("Enter Pay Amount");
        if(!o) return null;
        amtpaid = new BigDecimal(o);
        def p = [action:'open' ];
        loadInfo( p );
    }
    
    void payAll() {
        amtpaid = 0;
        def p = [action:'open' ];
        loadInfo( p );
    }
    
    def resetPayOption() {
        amtpaid = 0;
        loadInfo( [:] );
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
    
    public def save() {
        if( entity.amount != amount )
            throw new Exception("Amount must equal the total");
        def t = MsgBox.confirm("Please ensure that all entries are correct. Proceed?") 
        if(!t) return null;
        if(refid) entity.refid = refid;
        
        billingSvc.post( entity );
        return "_close";
    }
    
}