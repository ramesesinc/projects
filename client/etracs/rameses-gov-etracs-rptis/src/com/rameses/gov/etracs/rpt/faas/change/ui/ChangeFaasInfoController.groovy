package com.rameses.gov.etracs.rpt.faas.change.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public abstract class ChangeFaasInfoController 
{
    @Caller
    def caller;
    
    @Invoker
    def inv 

    @Binding
    def binding;
    
    @Service('FAASChangeInfoService')
    def svc;
    
    def entity;
    def changeinfo;
    
    
    public def getModifiedEntity(){
        return [:]
    }
    
    public void updateEntityInfo(newinfo){
        
    }
    
    public def getAction(){
        return inv.properties.actiontype;
    }
    
    void init(){
        changeinfo = [
            objid       : 'CI' + new java.rmi.server.UID(),
            refid   : entity.objid,
            faasid 	: entity.objid,
            rpid   	: entity.rp.objid,
            rpuid  	: entity.rpu.objid,
            action      : getAction(),
            redflagid : entity._redflag?.objid,
        ];
        
        changeinfo.newinfo = getModifiedEntity();
        changeinfo.previnfo = [:];
        changeinfo.previnfo.putAll(changeinfo.newinfo);
    }
    
    
    def save(){
        if (MsgBox.confirm('Save and apply changes?')){
            changeinfo.putAll(svc.updateInfo(changeinfo));
            updateEntityInfo(changeinfo.newinfo);
            caller.refreshForm();
            return '_close';
        }
        return null;
    }
    
    void cancel(){
        
    }
    
    def getTxntypes(){
        return svc.getTxnTypes();
    }
}