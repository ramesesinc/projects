package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;


class AuctionNoticeDeliveryModel
{
    def entity;
    def onupdate;
    
    void init(){
        entity = [:]
    }
    
    def update(){
        if (MsgBox.confirm('Update delivery information?')){
            onupdate(entity);
            return '_close'
        }
    }
}