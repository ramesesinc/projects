package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;

class AuctionReceiptBidModel extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt 
{
    @Service('PropertyAuctionPropertyService')
    def svc;
    
    void init(){
        super.init();
        calcReceiptAmount();
    }
    
    def getLookupAuctionProperty(){
        return Inv.lookupOpener('propertyauction_property:lookup', [
            state: 'FORPAYMENT', 
            
            onselect : {
                it.collectiontype = entity.collectiontype;
                entity.items = svc.getBidItemsForPayment(it);
                entity.property = it;
                entity.payer = it.rptledger.taxpayer;
                entity.paidby = it.rptledger.taxpayer.name;
                entity.paidbyaddress = it.rptledger.taxpayer.address.text;
                listHandler.reload();
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
    
    def listHandler = [
        fetchList : { entity.items }
    ] as BasicListModel
    
    void calcReceiptAmount(){
        entity.amount = 0.0;
        if (entity.items) {
            entity.amount = entity.items.amount.sum();
        }
        updateBalances();
        binding?.refresh('entity.*')
    }    
}

