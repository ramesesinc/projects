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
        batchTask = new BatchGRTask(svc:svc, params:params,cancelled:cancelled, oncomplete:oncomplete, showMessage:showMessage, onerror:onerror, rputypes:rputypes);
        Thread t = new Thread(batchTask);
        t.start();
        
        processing = true;
    }
    
    
    def oncomplete = {
        msg = 'Batch general revision has completed successfully.';
        processing = false;
        binding.refresh();
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
        return ['land', 'bldg', 'mach', 'planttree', 'misc']
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
    def onerror;
    def showMessage;
    def rputypes;

    public void run(){
        cancelled = false;
        def types = rputypes 
        def oldtype = params.rputype 
        def error = false;
        def errormsg = null;
        
        if(params.rputype){
            types = [params.rputype]
        }
        
        types.each{ rputype ->
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
                else if ('planttree'.equalsIgnoreCase(params.rputype)){
                    showMessage('Revising plants/trees real property units ...')
                    svc.revisePlantTreeRpus(params)
                    if (cancelled) return;
                    
                    showMessage('Revising plants/trees appraisals ...')
                    svc.revisePlantTreeDetails(params)
                    if (cancelled) return;
                }
                else if ('misc'.equalsIgnoreCase(params.rputype)){
                    showMessage('Revising miscellaneous real property units ...')
                    svc.reviseMiscRpus(params)
                    if (cancelled) return;
                    
                    showMessage('Revising miscellaneous items appraisal ...')
                    svc.reviseMiscRpuItems(params)
                    if (cancelled) return;
                    
                    showMessage('Revising miscellaneous item parameters ...')
                    svc.reviseMiscRpuItemParams(params)
                    if (cancelled) return;
                }
            }
            catch(e){
                error = true;
                errormsg = e.message;
                e.printStackTrace();
            }
        }
        
        if (error){
            onerror('Error: ' + errormsg);
        }
        else{
            params.rputype = oldtype 
            oncomplete()
        }
    }
}

