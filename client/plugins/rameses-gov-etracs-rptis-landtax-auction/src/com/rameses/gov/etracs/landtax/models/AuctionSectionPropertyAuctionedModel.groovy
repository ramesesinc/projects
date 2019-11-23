package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionSectionPropertyAuctionedModel extends CrudListModel
{
    //auction entity
    def entity; 
    
    public def getCustomFilter(){
        return ["parent.objid = :auctionid and state <> :state", [auctionid:entity.objid, state: 'FORAUCTION']]
    }
    
    def showMenu(){
        return null;
    }
    
    boolean isSurroundSearch(){
        return false;
    }
    
    public def getColumnList(){
        def cols = super.getColumnList();
        cols.find{it.name == 'bidder.entity.name'}.caption = 'Bidder Name';
        return cols 
    }
}