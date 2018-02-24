package com.rameses.gov.etracs.landtax.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;


class CashReceiptManualModel extends CashReceiptModel
{
    def selectedBillItem;
    def billItems;
    
    void init(){
        super.init();
        entity.txntype = 'online';
        bill.remove('itemcount');
    }
    
    void updateItemDue(item){
        super.updateItemDue(item);
        itemListHandler.reload();
    }
        
    def itemListHandler = [
        fetchList : { return selectedItem?.postingitems },
        
        onColumnUpdate : { item, colname ->
            if (!item[colname]){
                throw new Exception('Value cannot be empty.');
            }
            item.total = item.amount + item.interest - item.discount 
        },
        
        validate : {li -> 
            buildShares(selectedItem);
            updateItemTotals();
            calcReceiptAmount();
        },
    ] as EditorListModel
    
    void updateItemTotals(){
        selectedItem.total = selectedItem.postingitems.sum{ (it.total ? it.total : 0.0) }
        binding.refresh('selectedItem.total');
    }
        
}

