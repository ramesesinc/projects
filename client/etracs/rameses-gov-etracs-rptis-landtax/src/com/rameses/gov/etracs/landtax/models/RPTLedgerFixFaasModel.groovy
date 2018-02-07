package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPTLedgerFixFaasModel 
{
    @Binding
    def binding;
    
    @Service('QueryService')
    def qrySvc;
    
    def svc 
    def oncomplete
    def entity 
    
    String title = 'Update FAAS Information'
    
    def states = ['APPROVED', 'PENDING', 'CANCELLED']
    
    void init(){
        entity.classification = classifications.find{it.objid == entity.classification.objid}
        entity.actualuse = classifications.find{it.objid == entity.actualuse.objid}
    }
    
    
    def doUpdate(){
        if (entity.fromqtr == null || entity.fromqtr == 0)
            throw new Exception('From year must be greater than zero.');
        if (entity.fromyear > entity.toyear && entity.toyear != 0)
            throw new Exception('From Year must be less than or equal to To Year.');
        if (entity.fromyear == entity.toyear && entity.fromqtr > entity.toqtr && entity.toqtr != null)
            throw new Exception('From Qtr must be less than or equal to To Qtr.');

        if (MsgBox.confirm('Update ledger faas data?') ){
            if (entity.toqtr == null ) 
                entity.toqtr = 0;
            svc.fixLedgerFaas(entity)
            if (oncomplete) 
                oncomplete(entity);
            return doClose();
        }
        return null 
    }
    
    
    def doClose(){
        return '_close' 
    }
    
    
    def getQuarters(){
        return [1,2,3,4]
    }
    
    def classificationList;
     
    List getClassifications() {
        if (!classificationList){
            def p = [_schemaname:'propertyclassification']
            p.select ='objid,code,name'
            p.findBy = [state:'APPROVED']
            p.orderBy = 'orderno'
            classificationList = qrySvc.getList(p);
        }
        return classificationList;
    }
        
    
}