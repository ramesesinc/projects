package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class CashDepositModel extends PageFlowController { 

    @Service("CashDepositService")
    def depositSvc;
    
    @Service("QueryService")
    def querySvc;
    
    @Binding
    def binding;
    
    def fundList;
    def entity;
    def items;

    @PropertyChangeListener
    def listener = [
        "entity.fund" : {
            fundListHandler.reload();
            entity.items.clear();
            entity.bankaccount = null;
            entity.totalcash = 0;
            entity.totalnoncash = 0;
            entity.cashbreakdown = [];
            entity.amount = 0;
            binding.refresh("entity.amount");
        }
    ]
    
    void create() {
        fundList = depositSvc.getFundList();
        entity = [:]
        entity.items = [];
        entity.checks = [];
        entity.cashbreakdown = [];
        entity.amount = 0;
    }
    
    def fundListHandler = [
        fetchList: { o->
            if(!entity.fund) return [];
            items = depositSvc.getFundItems( entity.fund );
            return items;
        }, 
        isMultiSelect : {
            return true;
        },
        afterSelectionChange: {
            entity.items = fundListHandler.selectedValue;
            entity.amount = entity.items.sum{ it.amount };
            binding.refresh("entity.amount");
        }
    ] as BasicListModel;

    def getBankAccountList() {
        def h = { o->
            entity.bankaccount = o;
        }
        return Inv.lookupOpener("bankaccount:lookup", ["fundid":entity.fund.objid, onselect:h ] );
    }
    
    def addCheck() {
        def h = { o->
            entity.checks.addAll( o );
            entity.checks = entity.checks.unique();
            entity.totalnoncash = entity.checks.sum{ it.amount };
            entity.totalcash = entity.amount - entity.totalnoncash;
            binding.refresh("entity.(totalnoncash|totalcash|cashbreakdown)");
            checkModel.reload();
        }
        return Inv.lookupOpener("undeposited_check:lookup", [onselect:h, refids: entity.items*.liquidationid ]);
    }
    
    def checkModel = [
        fetchList: {
            return entity.checks;
        }
    ] as BasicListModel;

    
    void fetchDetails() {
        if(!entity.items)
            throw new Exception("Please select at least one item");
        entity.totalcash = entity.amount;
        entity.totalnoncash = 0;
    }
    
} 