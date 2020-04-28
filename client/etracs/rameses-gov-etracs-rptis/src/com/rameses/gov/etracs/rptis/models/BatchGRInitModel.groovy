package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

public class BatchGRInitModel 
{
    @Binding
    def binding;
    
    @Service('BatchGRService')
    def svc 
    
    @Service('QueryService')
    def querySvc
    
    @Service("LGUService")
    def lguSvc 
    
    def entity;
    def lgus;

    String title = 'Batch General Revision'
    
    void init() {
        entity = [:]
        entity.memoranda = 'REVISED AS PER SECTION 219 OF R.A. 7160.'
    }
    
    def process(){
        entity.objid = 'GR' + new java.rmi.server.UID();
        entity.filetype = 'batchgr';
        def newentity = svc.create(entity)
        return InvokerUtil.lookupOpener('batchgr:open', [entity:newentity]);
    }
        
    def getLgus() {
        if (!lgus){
            lgus = lguSvc.getLgus();
        }
        return lgus;
    }
    
    def getBarangays(){
        return lguSvc.getBarangaysByParentId(entity.lgu?.objid)
    }
    
    def getRputypes(){
        return ['land', 'bldg', 'mach', 'planttree', 'misc']
    }
    
    def getRylist(){
        def p = [_schemaname:'rysetting_land']
        p.where = ['1=1'];
        p.orderBy = 'ry desc';
        return querySvc.getList(p).ry.unique();
    }
    
    def getClassifications(){
        def p = [_schemaname:'propertyclassification']
        p.findBy = [state:'APPROVED']
        p.orderBy = 'orderno'
        return querySvc.getList(p)
    }
}