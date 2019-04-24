package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;


class AuctionNoticeNodModel 
{
    @Binding
    def binding
    
    @Service('PropertyAuctionNoticeNodService')
    def svc
    
    def entity  //initially set to ledger 
    def rptledger
    def mode 
    
    
    def init(){
        rptledger = entity
        entity = svc.loadNotice([objid:rptledger.objid])
        if (entity.isnew){
            mode = 'init'
            return 'init'
        }
        entity._schemaname = 'propertyauction_notice'
        return Inv.lookupOpener('propertyauction_notice:open', [entity:entity])
    }
    
    def create(){
        entity = svc.createNotice(entity)
        return Inv.lookupOpener('propertyauction_notice:report', [entity:entity])
    }
    
    def close(){
        binding.fireNavigation('_close')
    }
    
    
    
}