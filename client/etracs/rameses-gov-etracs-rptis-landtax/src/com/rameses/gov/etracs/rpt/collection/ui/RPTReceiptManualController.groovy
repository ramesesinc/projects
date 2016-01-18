package com.rameses.gov.etracs.rpt.collection.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;


class RPTReceiptManualController extends com.rameses.gov.etracs.rpt.collection.ui.RPTReceiptController
{
    def selectedBillItem;
    def billItems;
    
    void init(){
        super.init();
        entity.txntype = 'rptonline';
        bill.remove('itemcount');
    }
    
    void calcReceiptAmount(){
        entity.amount = 0.0
         if (itemsforpayment){
              entity.amount = itemsforpayment.amount.sum();
         }
         updateBalances();
         listHandler.load();
         binding?.refresh('totalGeneral|totalSef|entity.*')
    }    
    
    
    def billitems = []
    def itemListHandler = [
        fetchList : { return getBillItems() },
        
        onColumnUpdate : { item, colname ->
            if (colname.matches('basic.*')){
                if (colname == 'basic') item.sef = item.basic;
                if (colname == 'basicdisc') item.sefdisc = item.basicdisc;
                if (colname == 'basicint') item.sefint = item.basicint;
            }
            item.basicnet = item.basic + item.basicint - item.basicdisc;
            item.sefnet = item.sef + item.sefint - item.sefdisc;
            item.total = item.basicnet + item.basicidle + item.sefnet;
            item.totalgeneral = item.basicnet;
            item.totalsef = item.sefnet;
        },
        
        validate : {li -> 
            def item = li.item;
            billSvc.saveManualBillItem(bill, item);
            def items = svc.getItemsForPayment(bill);
            if (items){
                selectedItem.putAll(items[0]);
            }
            calcReceiptAmount();
        },
    ] as EditorListModel
    
    def getBillItems(){
        billitems = [];
        if (selectedItem){
            bill.rptledgerid = selectedItem.rptledgerid;
            billitems = billSvc.getBillItems(bill);
        }
        return billitems;
    }
    
    void updateItemDue(item){
        super.updateItemDue(item);
        itemListHandler.load();
    }
        
}

