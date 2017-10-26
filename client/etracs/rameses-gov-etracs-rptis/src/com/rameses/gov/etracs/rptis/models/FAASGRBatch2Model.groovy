package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*
import java.rmi.server.UID;
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

public class FAASGRBatch2Model 
{
    @Binding
    def binding;
    
    @Service("BatchGR2Service")
    def svc
            
     @Service("LGUService")
     def lguSvc 
    
    
    def params;
    def processing = false;
    def cancelled = false;
    def msg;
            
    String title = 'Batch General Revision'
    def counter = [success:0, error:0]
            
    def batchTask = null
    def lgus = null;
            
    void init() {
        processing = false;
        cancelled = false;
        params = [
            memoranda   : 'REVISED PURSUANT TO SECTION 219 OF RA 7160.',
            appraiser   : [:],
            taxmapper   : [:],
            recommender : [:],
            approver    : [:],
            inititems   : true,
            autoapprove : false,
            interval    : 100,
        ]
        lgus = lguSvc.getLgus();
    }         
            
    void revise() {
        if( !MsgBox.confirm("Revise all faas? ")) return;
        counter = [success:0, error:0];
        cancelled = false;
        msg = 'Loading properties to revise.'
        batchTask = new BatchGRTask(svc:svc, params:params,cancelled:cancelled, oncomplete:oncomplete, onrevise:onrevise,showMessage:showMessage, onerror:onerror, rputypes:rputypes);
        Thread t = new Thread(batchTask);
        t.start();
        
        processing = true;
    }
    
    
    def oncomplete = {cnt ->
        msg = 'Batch revision complete. Total records processed : ' + cnt 
        processing = false;
        binding.refresh();
    }
    
    def onrevise = {
        counter.success += it.success;
        counter.error += it.error;
        msg = 'Successfully Processed: ' + counter.success + '  Errors: ' + counter.error;
        binding.refresh('msg');
    }
    
    def showMessage = {
        msg = it;
        binding.refresh('msg');
    }
    
    def onerror = {
        msg = it;
        processing = false;
        binding.refresh();
    }
    
    def getBarangays(){
        return lguSvc.getBarangaysByParentId(params.lgu?.objid)
    }
    
    def getRputypes(){
        return ['land', 'bldg', 'mach']
        // return ['land', 'bldg', 'mach', 'planttree', 'misc']
    }
    
    def getRylist(){
        return svc.getRyList(params.lgu?.objid, null,null)   
    }
    
    void cancel() {
        processing = false;
        cancelled = true;
        batchTask.cancelled = true;
        msg = null;
    }
    
    /*===============================================
     * Signatory Lookup Support
     *===============================================*/
            
    def getLookupAppraiser(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTAPPRAISER',
            onselect : { 
                if (!params.appraiser) 
                    params.appraiser = [:]
                params.appraiser.putAll(it)
            },
            onempty  : {clearSignatory(params.appraiser)},
        ])
        
    }
    
    def getLookupRecommender(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTRECOMMENDER',
            onselect : { 
                if ( !params.recommender )
                    params.recommender = [:]
                params.recommender.putAll(it) 
            },
            onempty  : { clearSignatory(params.recommender)},
        ])
        
    }
    
    def getLookupTaxmapper(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTTAXMAPPER',
            onselect : { 
                if (!params.taxmapper)
                    params.taxmapper = [:]
                params.taxmapper.putAll(it) 
            },
            onempty  : { clearSignatory(params.taxmapper) },
        ])
        
    }
    
    def getLookupApprover(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTAPPROVER',
            onselect : { 
                if (!params.approver)
                    params.approver = [:]
                params.approver.putAll(it)
            },
            onempty  : { clearSignatory(params.approver)},
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

public class BatchGRTask implements Runnable{
    def svc;
    def params;
    def items;
    def cancelled;
    def oncomplete;
    def onrevise;
    def onerror;
    def showMessage;
    def rputypes;

    public void run(){
        cancelled = false;
        
        params.processallrpus = true
        
        if(params.rputype){
            rputypes = [params.rputype]
            params.processallrpus = false
        }
        
        rputypes.each{ rputype ->
            params.rputype = rputype 

            if (params.inititems == true){
                showMessage('Generating ' + params.rputype + ' items for revision.');
                svc.buildItemsForRevision(params);
                if (cancelled) return
            }

            try{
                if ('land'.equalsIgnoreCase(params.rputype)){
                    showMessage('Revising real properties ...')
                    svc.reviseRealProperties(params)
                    if (cancelled) return
                }

                showMessage('Revising real property units ...')
                svc.reviseRpus(params)
                if (cancelled) return;

                showMessage('Revising FAAS ...')
                svc.reviseFaases(params)
                if (cancelled) return;

                showMessage('Generating FAAS listing ...')            
                svc.reviseFaasList(params)
                if (cancelled) return;

                showMessage('Generating new signatories ...')
                svc.reviseFaasSignatories(params)
                if (cancelled) return;

                showMessage('Generating superseded information ...')
                svc.reviseFaasPreviousList(params)
                if (cancelled) return;

                if ('land'.equalsIgnoreCase(params.rputype)){
                    showMessage('Revising land real property units ...')
                    svc.reviseLandRpus(params)
                    if (cancelled) return;

                    showMessage('Revising land appraisals ...')
                    svc.reviseLandAppraisals(params)
                    if (cancelled) return;

                    showMessage('Revising plant/tree appraisals ...')
                    svc.revisePlantTreeAppraisals(params)
                    if (cancelled) return;

                    showMessage('Revising land adjustments ...')
                    svc.reviseLandAdjustments(params)
                    if (cancelled) return;
                }
                else if ('bldg'.equalsIgnoreCase(params.rputype)){
                    showMessage('Revising building real property units ...')
                    svc.reviseBldgRpus(params)
                    if (cancelled) return;

                    showMessage('Revising building structures ...')
                    svc.reviseBldgStructures(params)
                    if (cancelled) return;

                    showMessage('Revising building structural types ...')
                    svc.reviseBldgStructureTypes(params)
                    if (cancelled) return;

                    showMessage('Revising building uses ...')
                    svc.reviseBldgUses(params)
                    if (cancelled) return;

                    showMessage('Revising building floors ...')
                    svc.reviseBldgFloors(params)
                    if (cancelled) return;

                    showMessage('Revising building additional items ...')
                    svc.reviseBldgAdditionalItems(params)
                    if (cancelled) return;

                    showMessage('Revising building additional item parameters ...')
                    svc.reviseBldgAdditionalItemParams(params)
                    if (cancelled) return;
                }
                else if ('mach'.equalsIgnoreCase(params.rputype)){
                    showMessage('Revising machinery real property units ...')
                    svc.reviseMachRpus(params)
                    if (cancelled) return;
                    
                    showMessage('Revising machinery uses ...')
                    svc.reviseMachUses(params)
                    if (cancelled) return;
                    
                    showMessage('Revising machinery informations ...')
                    svc.reviseMachDetails(params)
                    if (cancelled) return;
                }
            }
            catch(e){
                e.printStackTrace();
                onerror('Error: ' + e.message);
            }
        }
        
        if (params.processallrpus) 
            params.rputype = null 
            
        oncomplete(svc.getRevisedCount(params).revisedcount)
    }
}

