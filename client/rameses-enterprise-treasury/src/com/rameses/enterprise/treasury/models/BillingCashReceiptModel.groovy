package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*
        
public class BillingCashReceiptModel extends AbstractCashReceipt {
    
    String entityName = "misc_cashreceipt"
    def barcodeid;
    def status;    
    def selectedItem;
    def payOption = [type:'FULL']; 
    
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
    
   

    def itemListModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;
            
    
    def billItemListModel = [
        afterColumnUpdate : {o,colname ->
            if( status.index == 0 ) throw new Exception("Please choose at least one item");
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