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

    def callformats = ['STANDARD', 'MODIFIED']

    @PropertyChangeListener
    def listener = [
        'entity.callformat' : {
            def p = [objid: entity.objid];
            p.parent = entity.parent;
            p.property = entity.property;
            p.callformat = entity.callformat;
            entity.calls = svc.getCalls(p);
            listHandler.reloadAll();
        }
    ]


    def getLookupProperty() {
    	return Inv.lookupOpener('propertyauction_property:lookup', [
    		state: 'FORSALE',
    		
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
    	def col = [:]
    	col.name = 'callno'
    	col.caption =  'Bid Call No.'
    	col.type = 'integer'
    	cols << col 

    	entity.bidders.each {
    		cols << createCol(it.bidderno, it.bidderno, 'decimal')
    	}

    	return cols
    }

    def createCol(name, caption, type) {
    	return [
    		name: name,
    		caption: caption,
    		type: type,
    	]
    }
        
}