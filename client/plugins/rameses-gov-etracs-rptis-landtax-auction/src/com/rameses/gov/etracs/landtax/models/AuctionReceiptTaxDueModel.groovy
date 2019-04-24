package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;

class AuctionReceiptTaxDueModel extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt 
{
    @Service('RPTReceiptService')
    def svc;
    
    @Service('RPTBillingService')
    def billSvc;
    
    def bill = [:];
    
    void init(){
        super.init();
        bill = billSvc.initBill(null);
        entity.billid = bill.objid; 
        entity.txntype = 'rptauction';
        calcReceiptAmount();
    }
    
    void buildBillParams(prop){
        bill.rptledgerid = prop.rptledger.objid;
        bill.billdate   = entity.receiptdate;
        bill.billtoyear = prop.notice.toyear;
    }
    
    def getLookupAuctionProperty(){
        return Inv.lookupOpener('propertyauction_property:lookup', [
            state: 'FORPAYMENT', 
            
            onselect : {
                it.collectiontype = entity.collectiontype;
                entity.property = it;
                entity.payer = it.rptledger.taxpayer;
                entity.paidby = it.soldto.name;
                entity.paidbyaddress = it.soldto.address.text;
                loadItemByLedger(it);
                calcReceiptAmount();
            },
            
            onempty : {
                entity.property = null;
                entity.items = [];
                listHandler.reload();
                calcReceiptAmount();
            }            
        ])
    }
    
    void calcReceiptAmount(){
        entity.amount = 0.0;
        if (entity.rptitems) {
            entity.amount = entity.rptitems.amount.sum();
        }
        updateBalances();
        binding?.refresh('entity.*')
    }    
    
    void loadItemByLedger(prop){
        def rptledger = prop.rptledger;
        buildBillParams(prop);
        entity.rptitems = [];
        entity.rptitems << svc.buildPaymentInfoByLedger(rptledger, bill);
        calcReceiptAmount();
    }
}

