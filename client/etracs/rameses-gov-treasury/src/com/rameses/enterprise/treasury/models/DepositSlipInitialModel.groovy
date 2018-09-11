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
    
    @Service("QueryService")
    def queryService;
    
    def entity;
    def depositvoucherfund;
    
    def selectedCheck;
    def checkList;
    def handler;
    
    void create() {
       entity = [:];
       entity.state = "DRAFT";
       entity.depositvoucherfundid = depositvoucherfund.objid;
       entity.totalcash = 0;
       entity.totalcheck = 0;
       entity.cashbreakdown = [];
       entity.numcheckslimit = 0;
       entity.amount = depositvoucherfund.amount - depositvoucherfund.amountdeposited;
       
       def m = [_schemaname: 'checkpayment'];
       m.findBy = [depositvoucherid: depositvoucherfund.parentid];
       m.where = [ "depositslipid IS NULL AND fundid = :fundid", [fundid: depositvoucherfund.fundid ]];
       m.orderBy = "refno";
       checkList = queryService.getList( m );
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
        entity.amount = entity.totalcash + entity.totalcheck;
        entity.checks = checkList.findAll{ it.selected == true }.collect{ [amount:it.amount, checkid: it.objid, deposittype: it.bank.deposittype] };
        def r = depositSlipSvc.create( entity );
        handler(r)
        return "_close";
    }
    
    def getBankAccountLookup() {
       def h = { o->
           entity.bankaccount = o;
           binding.refresh("bankaccount.*");
       }
       def fundid = depositvoucherfund.fund.objid;
       return Inv.lookupOpener("bankaccount:lookup", [fundid: fundid, onselect: h] );
   } 
    
   def checkListModel = [
        fetchList: { o->
            return checkList;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("checkpayment:open", [entity: [objid: o.refid ]] );
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