package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*


class FAASInitCancelModel extends FAASInitTxnModel
{
    @Service('CancelledFAASService')
    def cancelSvc;
        
    void afterInit(){
        entity.objid = 'CTD' + new java.rmi.server.UID();
        entity.state = 'DRAFT';
        entity.online = 1;
    }
    
    def process(){
        def cancelledfaas = cancelSvc.initCancelledFaas( entity );
        return Inv.lookupOpener('cancelledfaas:online:open', [entity:cancelledfaas]);
    }
    
}
