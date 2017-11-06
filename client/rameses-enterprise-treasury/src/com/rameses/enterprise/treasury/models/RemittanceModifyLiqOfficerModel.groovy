package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class RemittanceModifyLiqOfficerModel {

    @Service('PersistenceService')
    def persistSvc; 
    
    @Service('RemittanceService') 
    def remittanceSvc; 
    
    def title = 'Change Liquidating Officer';
    def entity = [:];
    def info = [:];
    def handler;
    def reason;
    
    void init() { 
        if ( !entity?.objid ) throw new Exception('remittance objid is required');
        
        info = [ liquidatingofficer: entity.liquidatingofficer ]; 
        def liqrem = persistSvc.read([_schemaname:'liquidation_remittance', objid: entity.objid ]); 
        if ( liqrem ) throw new Exception('Remittance has already been liquidated');         
    }

    def getLookupHandler() { 
        def p = [:]; 
        p.onselect = { o-> 
            info.liquidatingofficer = [
                objid : o.objid, 
                name  : o.name, 
                title : o.title 
            ]; 
        } 
        return Inv.lookupOpener('liquidatingofficer:lookup', p); 
    }
    
    def doOk() {
        if ( !MsgBox.confirm('You are about to submit this transaction. Continue?')) return null; 

        def p = [ action: 'change-liquidating-officer' ];
        p.objid = entity.objid; 
        p.reason = reason; 
        p.info = info; 
        remittanceSvc.modify( p ); 
        entity.liquidatingofficer = info.liquidatingofficer; 
        
        if ( handler ) handler( p ); 
        return '_close'; 
    }
    
    def doCancel() { 
        return '_close'; 
    } 
}    