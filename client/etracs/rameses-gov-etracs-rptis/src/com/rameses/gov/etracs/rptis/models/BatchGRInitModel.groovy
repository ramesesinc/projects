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
    def processing = false;
    def lgus;
    def msg;
    
    String title = 'Batch General Revision'
    
    def init() {
        entity = [:]
        entity.memoranda = 'REVISED AS PER SECTION 219 OF R.A. 7160.'
        processing = false;
        loadLgus();
    }
    
    void create() {
        if (MsgBox.confirm('Save and build list of FAAS for revision?')) {
            processing = true;
            new Thread(task).start();
        }
    }
    
    def onComplete = {batch->
        batch.state = 'FORREVISION';
        svc.update(batch);
        binding.fireNavigation(Inv.lookupOpener('batchgr:open', [entity:batch]));
        processing = false;
        entity.section = null;
        binding.refresh();
    }
    
    def onError = {e->
        processing = false;
        binding.refresh();
        MsgBox.alert('Unable to create Batch Revision. Fix the error and recreate the transaction.\nERROR: ' + e.message);
    }
    
    def showStatus = {
        msg = it;
        binding.refresh('msg');
    }
    
    def task = {
        run : {
            try {
                showStatus('Creating batch revision...');
                def batch = svc.create(entity);
                
                showStatus('Loading items for revision...');
                svc.getItems(batch).each {
                    showStatus('Updating item status: ' + it.tdno);
                    it.putAll(svc.updateItemStatus(it));
                }
                onComplete(batch);
            } catch(e) {
                onError(e);
            }
        }
    } as Runnable
    
    
    
    
    void loadLgus() {
        if (!lgus){
            lgus = lguSvc.getLgus();
        }
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
    
    /*===============================================
     * Signatory Lookup Support
     *===============================================*/
            
    def getLookupAppraiser(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTAPPRAISER',
            onselect : { 
                if (!entity.appraiser) 
                    entity.appraiser = [:]
                entity.appraiser.putAll(it)
            },
            onempty  : {clearSignatory(entity.appraiser)},
        ])
        
    }
    
    def getLookupRecommender(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTRECOMMENDER',
            onselect : { 
                if ( !entity.recommender )
                    entity.recommender = [:]
                entity.recommender.putAll(it) 
            },
            onempty  : { clearSignatory(entity.recommender)},
        ])
        
    }
    
    def getLookupTaxmapper(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTTAXMAPPER',
            onselect : { 
                if (!entity.taxmapper)
                    entity.taxmapper = [:]
                entity.taxmapper.putAll(it) 
            },
            onempty  : { clearSignatory(entity.taxmapper) },
        ])
        
    }
    
    def getLookupApprover(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTAPPROVER',
            onselect : { 
                if (!entity.approver)
                    entity.approver = [:]
                entity.approver.putAll(it)
            },
            onempty  : { clearSignatory(entity.approver)},
        ])
        
    }
    
    void clearSignatory(signatory){
        if (signatory){
            signatory.personnelid = null;
            signatory.name = null;
            signatory.title = null;
        }
    }        
    
        
}