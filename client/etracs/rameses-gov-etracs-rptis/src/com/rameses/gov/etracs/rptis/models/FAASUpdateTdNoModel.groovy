package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class FAASUpdateTdNoModel 
{
    @Binding
    def binding
    
    @Caller
    def caller 
    
    @Service('FAASService')
    def svc
    
    
    def entity;
    
    void init(){
        
    }
    
    def updateTdno(){
        if (MsgBox.confirm('Update FAAS TD No.?')){
            svc.updateTdNoByRecord([objid:entity.objid, tdno:entity.newtdno]);
            entity.tdno = entity.newtdno;
            caller?.refreshForm();
            return '_close';
        }
    } 
    
}
    