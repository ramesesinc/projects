package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;

public class MarketAccountModel extends CrudFormModel {
    
    def viewUnit() {
        if(!entity.unit?.objid) 
            throw new Exception("Please select a unit first");
        def op = Inv.lookupOpener("market_rentalunit:open", [ entity: [objid: entity.unit?.objid ]])
        op.target = "popup";
        return  op;
    }
    
}