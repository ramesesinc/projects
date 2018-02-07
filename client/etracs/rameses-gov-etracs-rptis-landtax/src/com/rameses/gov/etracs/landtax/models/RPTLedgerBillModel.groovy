package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RPTLedgerBillModel
{
    @Binding
    def binding;
        
    @Caller
    def caller;
        
    @Service('RPTBillingService')
    def billSvc;
        
    @Service('DateService')
    def dtSvc;
        
    String title = 'Real Property Tax Bill';
        
    @PropertyChangeListener
    def listener = [
        "advancebill":{
            if (!advancebill){
                billdate = null;
                binding.refresh('billdate');
            }
        }
    ]
        
    def entity;
    def billtoyear;
    def billtoqtr;
    def advancebill = false;
    def billdate;
    def qtrs = [1,2,3,4];
        
    def init(){
        billtoyear = dtSvc.getServerYear();
        billtoqtr = 4;
        return 'init';
    }
        
    def initBill(){
        if (!entity)
            entity = caller.selectedItem;
    
        def bill = billSvc.initBill(entity.objid);
        bill.billtoyear = billtoyear;
        bill.billtoqtr = billtoqtr;
        bill.taxpayer = entity.taxpayer;    
        bill.advancebill = advancebill;
        bill.billdate = billdate;
        return bill;
    }
        
    def preview(){
        def bill = initBill()
        def inv = InvokerUtil.lookupOpener('rptbill:previewSingleBill', [bill:bill, caller:this, showBack:false])
        inv.target = 'self';
        return inv;
    }
        
    def print(){
        def bill = initBill()
        return InvokerUtil.lookupOpener('rptbill:printSingleBill', [bill:bill, caller:this, showBack:false])
    }        

}    