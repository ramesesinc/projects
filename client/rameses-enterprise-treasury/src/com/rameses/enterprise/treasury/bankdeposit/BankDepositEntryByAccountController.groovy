package com.rameses.enterprise.treasury.bankdeposit;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
        
class BankDepositEntryByAccountController
{

    @Binding 
    def binding;

    def entity;
    def selectedFund;

    def undepositedChecks;

    def handler;

    def cashBreakdown;
    def selectedCheck;
    def total = 0;
    def breakdown = 0;
    def fundSummaries;

    
    
    
    void init() {
        entity = [amount:0.0, totalcash:0.0, totalnoncash:0.0, amount:0.0];
        entity.checks = [];
        entity.cashbreakdown = [];
        cashBreakdown = InvokerUtil.lookupOpener("cash:breakdown", 
          [ entries: entity.cashbreakdown,
            onupdate: { o->
                breakdown = o
                total = breakdown+entity.totalnoncash;
                binding.refresh("breakdown|total");
            }
           ]
        );
    }

    def getLookupBankAccount() {
        return InvokerUtil.lookupOpener( "bankaccountspecialfund:lookup", [
            "query.fundid": '%',
                
            onselect : {
                entity.bankaccount = it;
                fundSummaryModel.load();
                calcAmounts();
            },
                
            onempty : {
                entity.bankaccount = null;
                fundSummaryModel.load();
            }
                
        ]);
    }
    
    void calcAmounts(){
        entity.amount = 0.0;
        if (summaries) { 
            entity.amount = roundoff( summaries.balance.sum());
        }
        
        entity.totalnoncash = 0.0;
        if (entity.checks){
            entity.totalnoncash = roundoff( entity.checks.amount.sum());
        }
        entity.totalcash =  entity.amount - entity.totalnoncash
        total = entity.totalcash+entity.totalnoncash;
        checkModel.reload();
        binding.refresh("entity.amount|entity.totalnoncash|total|entity.totalcash");
    }

    def addCheck() {
        return InvokerUtil.lookupOpener( "undepositedcheck:lookup", 
            [
                undepositedChecks: undepositedChecks,
                onselect: { arr->
                    if (arr) {
                        arr.each {
                            entity.checks << it;
                            undepositedChecks.remove(it);
                        }
                        calcAmounts();
                        checkModel.reload();
                    }
                }
            ]
       );
    }

    void removeCheck() {
        if(!selectedCheck) return;
        entity.checks.remove( selectedCheck );
        selectedCheck.selected = false
        undepositedChecks << selectedCheck;

        if( entity.checks ) {
            entity.totalnoncash = roundoff( entity.checks.sum{ it.amount });
        } else {
            entity.totalnoncash = 0;
        }
        total = entity.totalcash+entity.totalnoncash;
        binding.refresh("entity.amount|entity.totalnoncash|total");
    }

    def formatter = new java.text.DecimalFormat("0.00"); 

    def roundoff( Number amount ) { 
        def str = formatter.format( amount ); 
        return new java.math.BigDecimal( str ); 
    } 
    
    def doOk() {
        if (!entity.bankaccount) 
            throw new Exception('Bank Account is required.');
            
        def breakdownamt = roundoff( breakdown );
        def totalcashamt = roundoff( entity.totalcash );        
        if( breakdownamt != totalcashamt )
            throw new Exception("Cash breakdown must be equal to total cash.");
            
        def amount  = roundoff( entity.amount );
        def totalamt = roundoff( total );       
        if( amount != totalamt ) 
            throw new Exception("Total cash and non cash must equal amount to deposit"); 
            
        if ( handler ) {
            handler( entity );
        } 
        return "_close";
    }

    def doCancel() {
        return "_close";
    }

    def checkModel = [
        fetchList : { o->
            return entity.checks;
        }
    ] as BasicListModel;
    
    
    
    def summaries = []
            
    def fundSummaryModel = [
        fetchList: { o->
            summaries = fundSummaries;
            if (entity.bankaccount){
                summaries = getFundSummariesByAccount()
                entity.summaries = summaries
                entity.bankaccount.fundsummaries = entity.summaries
            }
            return summaries; 
        }
    ] as BasicListModel;    
    
    
    def getFundSummariesByAccount(){
        def summaries = [];
        fundSummaries.each{ fs ->
            if ( fs.bankaccounts.find{ it.code == entity.bankaccount.code}){
                summaries << fs;
            }
        }
        return summaries;
    }

}      