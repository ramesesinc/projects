package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AuctionBiddingProcessModel 
{
	@Binding
	def binding;

	@Service('PropertyAuctionBiddingService')
	def svc;


	def entity;
	def bid;

	def onbid = {};

    void init() {
    	bid = [:];
    	bid.parent = [objid: entity.objid];
    }       

    def postBid() {
    	svc.postBid(bid);
    	onbid(bid);
    	init();
    	binding.refresh();
    	binding.requestFocus('bid.bidder')
    }
}