package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionBidderModel extends CrudFormModel
{
    @Caller 
    def caller;
    
    @Service('PropertyAuctionBidderService')
    def svc;
    
    def selectedItem;
    
    boolean isShowConfirm() { return false; }
    
    public void afterCreate(){
        setParentAuction();
        entity.bondamt = 0.0;
    }
    
    void setParentAuction() {
        if (hasCallerProperty('entity')) {
            entity.parent = caller.entity;
        } else {
            entity.parent = svc.getActiveAuction();
        }
    }
    
    void approve(){
        if (MsgBox.confirm('Approve bidder information?')){
            svc.approve(entity);
            reload();
        }
    }
    
    def getLookupProperty() {
        return Inv.lookupOpener('propertyauction_property:lookup', [state: null]);
    }
    
    public void afterColumnUpdate(String name, def item, String colName ) {
        def property = item.property;
        item.rptledger = [objid: property.rptledger.objid];
        item.tdno = property.rptledger.tdno;
        item.fullpin = property.rptledger.fullpin;
        item.owner = property.rptledger.owner;
        item.totalmv = property.rptledger.totalmv;
        item.totalav = property.rptledger.totalav;
        item.totalareaha = property.rptledger.totalareaha;
    }
    
    public boolean beforeRemoveItem(String name, def item ) {
        return MsgBox.confirm('Remove selected item?');
    }
}