package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class CreditMemoModel extends CrudFormModel { 
    
    def page;
    
    public def viewPostToIncome() {
        entity.items = [];
        edit();
        page = "income";
        return page;
    }
    
    public def postToIncome() {
        if( !entity.items ) 
        throw new Exception("Please specify at least one item");
        if( entity.itemtotal != entity.amount )
            throw new Exception("Amount must be equal to items amount");
            
        entity._action = 'postToIncome'; 
        entity.issuereceipt = 0; 
        save();
        MsgBox.alert('Successfully Posted');
        return "_close";
    }
    
    public String getConfirmMessage() {
        return "You are about to post this to the bank account record. Proceed?";
    }
    
    def getItemAccountLookup() {
        if( !entity.bankaccount?.fund?.objid )
            throw new Exception("Please select a bank account first");
        return Inv.lookupOpener( "itemaccount:lookup", [fundid: entity.bankaccount.fund.objid] );
    }
    
    //def df = new java.text.DecimalFormat("#,##0.00")

    public void afterAddItem(String name, def item ) {
        entity.itemtotal  = entity.items.sum{ it.amount };
        binding.refresh("entity.itemtotal");
    }
    
    public void afterColumnUpdate(String name, def item, String colName ) {
        if(colName == "amount") {
            if( entity.items ) {
                entity.itemtotal  = entity.items.sum{ it.amount };
                binding.refresh("entity.itemtotal");
            }
        }
    }
    
} 