package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionSectionBidderModel extends CrudListModel
{
    //auction entity
    def entity; 
    
    public def getCustomFilter(){
        return ["parent.objid = :auctionid", [auctionid:entity.objid]]
    }
    
    def showMenu(){
        return null;
    }
    
    boolean isSurroundSearch(){
        return false;
    }
    
}