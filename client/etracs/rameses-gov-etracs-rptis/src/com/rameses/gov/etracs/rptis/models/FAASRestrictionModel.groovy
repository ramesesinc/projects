package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*; 
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudFormModel;

class FAASRestrictionModel extends CrudFormModel 
{
    @Service('QueryService')
    def querySvc 
    
    @Service('FAASRestrictionService')
    def svc
    
    String schemaName = 'faas_restriction'
    
    def parent
    def ledger
    def oncreate 
    def onupdate
    
    boolean showConfirm = false
    
    boolean isEditAllowed(){
        def allowed = super.isEditAllowed()
        return entity.state == 'DRAFT' && allowed
    }
    
    boolean isCreateAllowed(){
        def allowed = super.isCreateAllowed()
        return ledger == null && allowed 
    }
    
    public void afterCreate(){
        entity.objid = 'RLS' + new java.rmi.server.UID()
        entity.state = 'DRAFT'
        entity.parent = parent 
        entity.ledger = ledger
    }
    
    public void afterSave(){
        if (mode == 'create'){
            if (oncreate) oncreate(entity) 
        }
        else{
            if (onupdate) onupdate(entity) 
        }
    }
    
    void approve(){
        if (MsgBox.confirm('Approve?')){
            svc.approve(entity)
            reload()
            try{ caller?.refresh()}catch(ign){;}
            if (onupdate) onupdate(entity)
        }
    }
    
    def getLookupFaas(){
        return Inv.lookupOpener('faas:lookup', [
            onselect :{
                if (it.state !=  'CURRENT')
                    throw new Exception('FAAS is invalid. Only Current state is allowed.')
                entity.parent = it;
            },
            onempt : {
                entity.parent = null;
            }
        ])
    }
    
    def getRestrictiontypes(){
        def p = [_schemaname:'faas_restriction_type']
        p.where = ['1=1']
        p.orderBy = 'idx'
        return querySvc.getList(p);
    }
}
