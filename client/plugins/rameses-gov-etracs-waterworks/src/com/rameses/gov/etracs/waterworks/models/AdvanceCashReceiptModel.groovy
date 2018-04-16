package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class AdvanceCashReceiptModel extends AbstractCashReceipt  {
    
    @Controller
    def workunit;
        
    @Invoker
    def invoker;
    
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
    
    void loadInfo(def p) {
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.receiptdate;
        
        /*
        p.rulename = getRulename();
        def info = cashReceiptSvc.getInfo( p );
        entity.putAll(info);
        if( !info.billitems ) {
            def b = onNoItemsFound();
            if(!b) throw new Exception("No bill items found");
        }
        else {
            billItemList = info.items;
            reloadItems(); 
            //afterLoadInfo();
            //loadPayOptions();
        }
        */
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
    
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    
    
    
}

