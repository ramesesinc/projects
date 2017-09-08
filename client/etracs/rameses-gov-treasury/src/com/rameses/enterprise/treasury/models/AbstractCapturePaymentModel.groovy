package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

/****************************************************
* service must be a specific PaymentService
* it must be overriden by the service
****************************************************/
public abstract class AbstractCapturePaymentModel  { 

    @Binding
    def binding;
    
    public abstract def getService();
    
    boolean restrictPayPriority = true;
    def ref = [:];
    def entity = [:];
    def handler;
    def status;
    
    boolean showDiscount = false;
    boolean showSurcharge = true;
    boolean showInterest = true;
    
    def reftypes = ["cashreceipt"];
    def excludeFields = "selected|item.*|remarks|amtdue|balance|total";
    
    void init() {
        entity = service.init(ref);
        //we will number each item beacuse it is not being handled properly by the status
        int i = 0;
        entity.items.each {
            it._index = i++;
            it.selected = true;
            it.amount = it.amtdue;
            if(showDiscount == true) it.discount = 0;
            if( showSurcharge == true ) it.surcharge = 0;
            if( showInterest == true ) it.interest = 0;
            it.total = calcTotal(it);
        }
        updateBalance();
    }
    
    def calcTotal(def item) {
        def tot = item.amount;
        //if( showDiscount == true  ) tot += item.discount;
        if( showSurcharge == true  ) tot += item.surcharge;
        if( showInterest == true  ) tot += item.interest;
        return tot;
    }
    
    void updateBalance() {
        entity.total = entity.items.sum{ it.total };
    }
    
    def itemHandler = [
        fetchList: {o->
            return entity.items;
        },
        afterColumnUpdate: { o, colName->
            if(colName == 'selected') {
                recalc(o);
            }
            o.total = calcTotal(o);
            updateBalance();
            binding.refresh("entity.total");
        }
    ] as EditorListModel;
    
    def post() {
        if(entity.amount!=entity.total) 
            throw new Exception("Amount Must match the total of the items");
        if(MsgBox.confirm('You are about to save this payment and update the ledger. Proceed?')) {
            service.post(entity);
            if(handler) handler();
            return "_close";
        }
    }
    
    void recalc(def o) {
        if( o.selected == true ) {
            o.amount = o.amtdue;
        }
        else {
            o.amount = 0;
            if(showDiscount == true) o.discount = 0;
            if( showSurcharge == true  ) o.surcharge = 0;
            if( showInterest == true  ) o.interest = 0;
        }
        /*
         items.eachWithIndex { o, idx->
             if(idx <= index){
                if( idx < index ) {
                    o.selected = true;
                }
                if(o.selected ) {
                    o.amount = o.balance;
                    entity.items << [item:o.item, amount: o.amount, remarks: o.remarks, refid: o.refid, ticketid: o.ticketid ];
                }
                else {
                    o.amount = 0;
                }
             }
             else{
                o.selected = false;
                o.amount = 0.0;
             }
        }
        entity.amount = entity.items.sum{it.amount};
        updateBalances();
        */
    }

    
} 