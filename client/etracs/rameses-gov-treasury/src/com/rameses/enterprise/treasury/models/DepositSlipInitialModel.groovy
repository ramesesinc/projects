package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class DepositSlipInitialModel {

    @Caller 
    def caller;
    
    @Binding
    def binding;
    
    
    @Service("DepositSlipService")
    def depositSlipSvc;
    
    def entity;
    def limit; //the maximum amount
    def handler;
    def depositvoucher;
    def fundid; 
    
    def selectedCheck;
    def checkList;
    
    
    void create() {
       entity = [:];
       entity.state = "DRAFT";
       entity.depositvoucherid = depositvoucher.objid;
       entity.fundid = fundid;
       entity.totalcash = 0;
       entity.totalcheck = 0;
       entity.cashbreakdown = [];
       entity.numcheckslimit = 0;
       entity.amount = depositvoucher.amount - depositvoucher.amountdeposited;
       limit = entity.amount;
       checkList = depositSlipSvc.getAvailableChecks( [depositvoucherid: depositvoucher.objid, fundid: fundid ] );
    } 
    
    
    def cashBreakdownHandler = { o->
        entity.totalcash = o.total;
        binding.refresh("entity.totalcash|balance|total");
    }
    
    def getBalance() {
        return entity.amount - entity.totalcheck - entity.totalcash;
    }
    
    def getTotal() {
        return entity.totalcheck + entity.totalcash;
    }
    
    
    def save() {
        if( balance != 0 )
            throw new Exception("Please ensure that there are no balances remaining" );
        if( entity.amount > limit ) throw new Exception("Total amount must be less than "+limit);
        
        entity.checks = checkList.findAll{ it.selected == true }.collect{ [amount:it.amount, checkid: it.refid, deposittype: it.deposittype] };
        def r = depositSlipSvc.create( entity );
        if(caller)caller.updateVoucher(r.amountdeposited);
        return "_close";
    }
    
    def getBankAccountLookup() {
       def h = { o->
           entity.bankaccount = o;
           binding.refresh("bankaccount.*");
       }
       return Inv.lookupOpener("bankaccount:lookup", [fundid: fundid, onselect: h] );
   } 
    
   def checkListModel = [
        fetchList: { o->
            return checkList;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: [objid: o.refid ]] );
            op.target = "popup";
            return op;
        }
        ,
        onColumnUpdate: {o,colName->
            if( o.selected ) {
                entity.totalcheck += o.amount;
            }
            else {
                entity.totalcheck -= o.amount;
            }
            binding.refresh("entity.totalcash|balance|total");
        }
    ] as EditorListModel;     
    
    
    void selectAll() {
        checkList.each {it.selected = true };
        entity.totalcheck = checkList.sum{ it.amount };
        binding.refresh("entity.totalcash|balance|total");
    }
    
    void deselectAll() {
        checkList.each {it.selected = false };
        entity.totalcheck = 0;
        binding.refresh("entity.totalcash|balance|total");
    }
    
    
}    