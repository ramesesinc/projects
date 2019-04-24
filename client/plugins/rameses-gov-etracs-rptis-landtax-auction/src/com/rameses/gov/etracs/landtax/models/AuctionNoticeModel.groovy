package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionNoticeModel extends CrudFormModel
{
    @Service('PropertyAuctionNoticeService')
    def svc;
    
    def onupdate = {
        entity.putAll(it);
        entity.putAll(svc.delivered(entity));
        binding.refresh();
    }
    
    def deliver(){
        svc.associateAuction(entity)
        return Inv.lookupOpener('propertyauction_notice_delivery:open', [onupdate:onupdate])
    }
    
    def preview() {
        return Inv.lookupOpener('propertyauction_notice:report', [entity: entity]);
    }
    
}