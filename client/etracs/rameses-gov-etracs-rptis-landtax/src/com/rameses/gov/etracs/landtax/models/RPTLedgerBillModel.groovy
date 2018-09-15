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
        "bill.advancebill":{
            if (!bill.advancebill){
                bill.billdate = null;
                binding.refresh('bill.billdate');
            }
        }
    ]
        
    def entity;
    def bill;
    def qtrs = [1,2,3,4];
    
    def getEntityContext(){
        if (entity){
            return entity;
        }
        return caller.selectedItem;
    }
    
    def init(){
        bill = billSvc.initBill();
        bill.taxpayer = entityContext.taxpayer;
        bill.ledgers << [objid: entityContext.objid, bill:true];
        return 'init';
    }
        
    def preview(){
        def inv = InvokerUtil.lookupOpener('bill:rptledger:preview', [bill:bill, caller:this])
        inv.target = 'self';
        return inv;
    }
        
    def print(){
        return InvokerUtil.lookupOpener('bill:rptledger:print', [bill:bill, caller:this])
    }        

}    