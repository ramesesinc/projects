package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;


class DepositFundTransferModel  {
    
    @Service("DepositFundTransferService")
    def service;
    
    @Service("QueryService")
    def queryService;
    
    def entity;
    def depositvoucherfund;
    def itemList;
    def handler;
    
    public void create() {
        entity = [:];
        //assumption is amount is less than amtdeposited.
        entity.amount =  depositvoucherfund.amountdeposited - depositvoucherfund.amount;
        if( entity.amount < 0 )
            throw new Exception("amount must be greater than zero");
        
        entity.fromdepositvoucherfundid = depositvoucherfund.objid;
        
        def m = [_schemaname: 'depositvoucher_fund'];
        m.findBy = [parentid: depositvoucherfund.parentid ];
        m.where = ["NOT( objid = :objid)", [objid: depositvoucherfund.objid ] ];
        itemList = queryService.getList(m).findAll{  (it.amount-it.totaldr)!=(it.amountdeposited-it.totalcr )};
        itemList = itemList.collect{[fund:it.fund, todepositvoucherfundid: it.objid, amtdue: it.amount - it.amountdeposited, amount:0 ] }; 
        if( itemList.size() == 0 )
            throw new Exception("No available funds to transfer to. Please check deposit voucher");
    }
    
    def listModel  = [
        fetchList: { o->
            return itemList;
        }
    ] as EditorListModel;

    def doCancel() {
        return "_close";
    }

    def doOk() {
        entity.items = itemList.findAll{ it.amount > 0 };
        if( entity.items.sum{it.amount } != entity.amount )
            throw new Exception("Amount to transfer must equal to " + entity.amount );
        service.create( entity );
        if(handler) handler(entity);
        return "_close";
    } 
    
    
}    