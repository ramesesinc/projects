package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*
        
public class PaymentOrderCashReceiptModel extends BasicCashReceipt {
    
    @Service("PaymentOrderService")
    def pmtSvc;        

    String entityName = "misc_cashreceipt"
    def barcodeid;

    def status;    

    void buildReceiptItems( billitems ) {
        def items = [];
        billitems.each { 
            items << [ item: it.item, amount: it.amount, remarks: it.remarks ];
            if( it.surcharge > 0 ) {
                items << [ item: it.surchargeAccount, amount: it.surcharge, remarks: it.remarks ];
            }
            if( it.interest > 0 ) {
                items << [ item: it.interestAccount, amount: it.interest, remarks: it.remarks ];
            }
        }
        entity.items = items;
        entity.amount = entity.items.sum{ it.amount };
    }
    
    def loadInfo(def info) {
        entity.payer = info.payer;
        entity.amount = info.amount;
        entity.paidby = info.paidby;
        entity.paidbyaddress = info.paidbyaddress;
        entity.remarks = info.remarks;
        entity.info = info.info; 
        super.init(); 
        if ( info.billitems ) {
            entity.billitems = info.billitems; 
            entity.billitems.each {
                it.selected = true;
                it._amount = it.amount;
                it._discount = it.discount;
                it._surcharge = it.surcharge;
                it._interest = it.interest;
                it._total = it.total;
            }
            buildReceiptItems( entity.billitems );
            return 'billing';
        } else { 
            entity.items = info.items;
            return 'default';
        } 
    }
    
    def loadBarcode() {
        def pmtOrder = pmtSvc.open( [txnid: barcodeid] ); 
        def info = pmtOrder.info;
        entity = [formtype: "serial", formno:"51", txnmode: 'ONLINE'];
        entity.collectiontype = info.collectiontype;
        entity = service.init( entity );
        entity.paymentorder = [ 
            txnid: pmtOrder.txnid, 
            txntype: pmtOrder.txntype,
            refid: pmtOrder.refid 
        ]; 
        return loadInfo(entity.info);
    }   

    def billItemListModel = [
        afterColumnUpdate : {o,colname ->
            updateItems(status.index, o.selected );
        },
        fetchList: { o-> 
            return entity.billitems; 
        } 
    ] as EditorListModel;

    void updateItems(int index, boolean selected){
        entity.items.clear();
        def bItems = [];
        entity.billitems.eachWithIndex { o, idx->
            if( idx < index  || (idx == index && selected==true) ) {
                o.selected = true;
                o.amount = o._amount;
                o.discount = o._discount;
                o.surcharge = o._surcharge;
                o.interest = o._interest;
                o.total = o._total;
                bItems.add( o );
            }
            else {
                o.selected = false;
                o.amount = 0;
                o.discount = 0;
                o.surcharge = 0;
                o.interest = 0;
                o.total = 0;
            }
        }
        buildReceiptItems( bItems );
        billItemListModel.reload();
        updateBalances();
    }
    
}       