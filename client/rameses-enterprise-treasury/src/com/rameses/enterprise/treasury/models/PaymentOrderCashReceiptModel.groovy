package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*
import com.rameses.util.*;
        
public class PaymentOrderCashReceiptModel extends AbstractCashReceipt {
    
    @Service("PaymentOrderCashReceiptService")
    def pmtOrderCashReceiptSvc;        

    String entityName = "misc_cashreceipt"
    def prefix;
    def barcodeid;

    def status;   
    def payOption;
    def selectedItem;
    
    public String getPayOptionOpener() {
        return null;
    }
    
    public String getDetails() {
        return "Details";
    }

    void init() {
        def o = MsgBox.prompt("Enter code");
        if(!o) throw new BreakException();
        barcodeid = o;
        def m = [prefix: entity.collectiontype.barcode, barcodeid: barcodeid, collectiontype: entity.collectiontype];
        def zinfo = pmtOrderCashReceiptSvc.getInfo( m );
        entity.putAll(zinfo);
        reloadItems();    
        super.init(); 
    }
    
    void loadBarcode() {
        def zinfo = pmtOrderCashReceiptSvc.getInfo( [prefix:prefix, barcodeid: barcodeid, collectiontype: entity.collectiontype] ); 
        entity.putAll( zinfo);
        reloadItems();
        super.init();
    }   
    
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    def showPayOption() {
        if(getPayOptionOpener()==null) return null;
        def h = { o->
            payOption = o;
            reloadItems();
        }
        return Inv.lookupOpener( getPayOptionOpener(), [handler: h]);
    }
    
    void reloadItems() {
        def m = [prefix: entity.collectiontype.barcode, barcodeid: barcodeid];
        def sb = new StringBuilder();
        sb.append("formtype|formno|txnmode|objid|receiptdate|");
        sb.append("org|user|items|amount|totalcash|totalnoncash|");
        sb.append("balancedue|cashchange|totalcredit|paymentitems|collector|");
        sb.append("stub|receiptno|controlid|series|endseries|prefix|suffix|serieslength|");
        sb.append("payer|paidby|paidbyaddress");
        String excl = sb.toString();
        entity.each{k,v-> 
            if(!k.matches(excl)) {
                m.put(k,v);
            }
        };
        if(payOption!=null) {
            m.payOption = payOption;
        }
        entity.items = pmtOrderCashReceiptSvc.getItems( m );
        entity.amount = NumberUtil.round( entity.items.sum{ it.amount } );  
        updateBalances();
        itemListModel.reload();
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;
}       