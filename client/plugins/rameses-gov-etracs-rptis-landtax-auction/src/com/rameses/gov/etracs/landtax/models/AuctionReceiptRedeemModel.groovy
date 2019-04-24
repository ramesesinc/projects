package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;

class AuctionReceiptRedeemModel extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt 
{
    @Service('PersistenceService')
    def persistenceSvc;
    
    void init(){
        super.init();
        calcReceiptAmount();
    }
    
    def getLookupRedemption(){
        return Inv.lookupOpener('propertyauction_property:lookup', [
            onselect : {
                entity.property = persistenceSvc.read([_schemaname:'propertyauction_property', objid:it.objid])
                entity.payer = it.rptledger.taxpayer;
                entity.paidby = it.rptledger.taxpayer.name;
                entity.paidbyaddress = it.rptledger.taxpayer.address.text;
                entity.items = []
                entity.property.charges.each{
                    def item = [:];
                    item.putAll(it);
                    item.objid = 'PPAC' + new java.rmi.server.UID();
                    entity.items << item;
                }
                listHandler.reload();
                calcReceiptAmount();
            },
            
            onempty : {
                entity.property = null;
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
        if (entity.property?.charges) {
            entity.amount = entity.property.charges.amount.sum();
        }
        updateBalances();
        binding?.refresh('entity.*')
    }    
}

