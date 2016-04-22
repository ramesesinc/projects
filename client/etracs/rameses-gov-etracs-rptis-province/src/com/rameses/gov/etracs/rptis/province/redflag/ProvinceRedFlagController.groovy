package com.rameses.gov.etracs.rptis.province.redflag;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ProvinceRedFlagController 
{
    @Service('ProvinceRedFlagService')
    def svc;
    
    def entity;
    def redflag;
    
    def mode;
    def MODE_CREATE = 'create';
    def MODE_READ = 'read';
    def action;
    
    void init(){
        redflag = [
            objid   : 'RF' + new java.rmi.server.UID(),
            parentid : entity.objid,
            state   : 'OPEN',
            refid   : entity.objid,
            refno   : getRefNo(entity),
            lguid   : entity.lguid,
            filedby : getFiledBy(),
            info    : [:],
        ]
        entity.redflag = redflag;
        mode = MODE_CREATE;
    }
    
    def getRefNo(entity){
        def refno = entity.tdno;
        if (!refno) refno = entity.utdno;
        if (!refno) refno = entity.txnno;
        return refno;
    }
    
    void post(){
        if (MsgBox.confirm('Post red flag?')){
            redflag.action = action.action;
            redflag.putAll(svc.postRedFlag(redflag));
            mode = MODE_READ;
        }
    }
    
    def getFiledBy(){
        return [
            objid : OsirisContext.env.USERID,
            name  : OsirisContext.env.USER,
        ]
    }
    
    String getOpenerName(){
        return 'faas:redflag:action';
    }
    
    def atypes;
    def getActiontypes(){
        if (!atypes){
            atypes = Inv.lookupOpeners(getOpenerName()).collect{
                return [caption:it.caption, action:it.properties.actiontype]
            }
        }
        return atypes;
    }
    
    def getOpener(){
        if (action) {
            return Inv.lookupOpener('redflag:'+action.action, [entity:entity]);
        }
        return null;
    }
}

    