package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionBiddingModel extends CrudFormModel 
{
    @Service('PropertyAuctionBiddingService')
    def svc;

    def callformats = ['STANDARD', 'MODIFIED'];
    def selectedItem;
    
    
    def startBidding() {
        if (MsgBox.confirm('Start bidding session?')) {
            entity.putAll(svc.startBidding(entity));
            return continueBidding();
        }
    }
    
    def continueBidding() {
        def params = [:]
        params.entity = entity;
        params.onbid = {bid ->
            entity.bidder = bid.bidder;
            entity.bidamt = bid.amount;
            reloadCalls();
            binding.refresh('entity.bidder.*|entity.bidamt');
        }
        return Inv.lookupOpener('propertyauction_bidding:session', params);
    }
    
    def closeBidding() {
        if (MsgBox.confirm('Close bidding session?\n')) {
            entity.putAll(svc.closeBidding(entity));
            return '_close';
        }
    }

    @PropertyChangeListener
    def listener = [
        'entity.callformat' : {
            reloadCalls();
        }
    ]
    
    void reloadCalls() {
        def p = [objid: entity.objid];
        p.parent = entity.parent;
        p.property = entity.property;
        p.callformat = entity.callformat;
        entity.calls = svc.getCalls(p);
        listHandler.reloadAll();
    }


    def getLookupProperty() {
    	return Inv.lookupOpener('propertyauction_property:lookup', [
    		state: 'FORAUCTION',
    		
    		onselect : {
    			entity.property = it;
    			entity.parent = it.parent;
    		},
    		onempty: {
    			entity.property = null;
    			entity.parent = null; 
    		}
		]);
    }


    def listHandler = [
    	fetchList : { entity.calls },
    	getColumnList : { getColumnList() },

    ] as BasicListModel

    def getColumnList() {
    	def cols = [];
    	def col = [:];
        
        if (entity.callformat == 'STANDARD') {
            col.name = 'callno';
            col.caption =  'Bid Call No.';
        } else {
            col.name = 'rank';
            col.caption =  'Bid Rank';
        }
    	col.type = 'integer';
    	cols << col;
        
        if (entity.callformat == 'STANDARD') {
            entity.bidders.each {
                cols << createCol(it.bidderno, it.bidderno, 'decimal');
            }
        } else {
            cols << createCol('bidder.bidderno', 'Bidder No.', 'text');
            cols << createCol('bidder.entity.name', 'Bidder', 'text');
            cols << createCol('amount', 'Amount', 'decimal');
        }

    	return cols;
    }

    def createCol(name, caption, type) {
    	return [
    		name: name,
    		caption: caption,
    		type: type,
    	]
    }
        
}