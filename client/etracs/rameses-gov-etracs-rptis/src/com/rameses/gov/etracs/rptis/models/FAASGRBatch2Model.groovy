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
    
    @Service('QueryService')
    def querySvc 
    
    @Service("BatchGR2Service")
    def svc
            
     @Service("LGUService")
     def lguSvc 
     
    @Service('Var')
    def var 
    
    
    def params;
    def processing = false;
    def msg;
            
    String title = 'Batch General Revision'
            
    def batchTask = null
    def lgus = null;
            
    void init() {
        processing = false;
        def memoranda = var.get("gr_default_memoranda")
        if (!memoranda) memoranda = 'REVISED PURSUANT TO SECTION 219 OF RA 7160.'
        params = [
            memoranda   : memoranda, 
            appraiser   : [:],
            taxmapper   : [:],
            recommender : [:],
            approver    : [:],
            inititems   : true,
            autoapprove : false,
            interval    : 50,
            continueonerror: true,
        ]
        lgus = lguSvc.getLgus();
    }         
            
    void revise() {
        if( !MsgBox.confirm("Revise all faas? ")) return;
        msg = 'Loading properties to revise.'
        batchTask = new BatchGRTask(svc:svc, params:params, rputypes:rputypes, showMessage:showMessage);
        Thread t = new Thread(batchTask);
        t.start();
        
        processing = true;
    }
    
    def showMessage = {
        msg = it.msg;
        if ('COMPLETED'.equalsIgnoreCase(it.status)){
            processing = false;
            binding.refresh();
        }
        else{
            binding.refresh('msg');
        }
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
    
    def getClassifications(){
        def p = [_schemaname:'propertyclassification']
        p.findBy = [state:'APPROVED']
        p.orderBy = 'orderno'
        return querySvc.getList(p)
    }
    
    void cancel() {
        processing = false;
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
    def rputypes;
    def showMessage;
    def cancelled;

    public void run(){
        cancelled = false;
        def types = rputypes 
        def oldtype = params.rputype 
        
        if(params.rputype){
            types = [params.rputype]
        }
        
        types.each{ rputype ->
            params.rputype = rputype 

            if (params.inititems == true){
                showMessage([msg:'Generating ' + params.rputype + ' items for revision.']);
                svc.buildItemsForRevision(params);
                if (cancelled) return
            }

            try{
                if ('land'.equalsIgnoreCase(params.rputype)){
                    showMessage([msg:'Revising real properties ...'])
                    svc.reviseRealProperties(params)
                    if (cancelled) return
                }

                showMessage([msg:'Revising real property units ...'])
                svc.reviseRpus(params)
                if (cancelled) return;

                showMessage([msg:'Revising FAAS ...'])
                svc.reviseFaases(params)
                if (cancelled) return;

                showMessage([msg:'Generating FAAS listing ...'])
                svc.reviseFaasList(params)
                if (cancelled) return;

                showMessage([msg:'Generating new signatories ...'])
                svc.reviseFaasSignatories(params)
                if (cancelled) return;

                showMessage([msg:'Generating superseded information ...'])
                svc.reviseFaasPreviousList(params)
                if (cancelled) return;

                if ('land'.equalsIgnoreCase(params.rputype)){
                    showMessage([msg:'Revising land real property units ...'])
                    svc.reviseLandRpus(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising land appraisals ...'])
                    svc.reviseLandAppraisals(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising plant/tree appraisals ...'])
                    svc.revisePlantTreeAppraisals(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising land adjustments ...'])
                    svc.reviseLandAdjustments(params)
                    if (cancelled) return;
                }
                else if ('bldg'.equalsIgnoreCase(params.rputype)){
                    showMessage([msg:'Revising building real property units ...'])
                    svc.reviseBldgRpus(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising building structures ...'])
                    svc.reviseBldgStructures(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising building structural types ...'])
                    svc.reviseBldgStructureTypes(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising building uses ...'])
                    svc.reviseBldgUses(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising building floors ...'])
                    svc.reviseBldgFloors(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising building additional items ...'])
                    svc.reviseBldgAdditionalItems(params)
                    if (cancelled) return;

                    showMessage([msg:'Revising building additional item parameters ...'])
                    svc.reviseBldgAdditionalItemParams(params)
                    if (cancelled) return;
                }
                else if ('mach'.equalsIgnoreCase(params.rputype)){
                    showMessage([msg:'Revising machinery real property units ...'])
                    svc.reviseMachRpus(params)
                    if (cancelled) return;
                    
                    showMessage([msg:'Revising machinery uses ...'])
                    svc.reviseMachUses(params)
                    if (cancelled) return;
                    
                    showMessage([msg:'Revising machinery informations ...'])
                    svc.reviseMachDetails(params)
                    if (cancelled) return;
                }
                else if ('planttree'.equalsIgnoreCase(params.rputype)){
                    showMessage([msg:'Revising plants/trees real property units ...'])
                    svc.revisePlantTreeRpus(params)
                    if (cancelled) return;
                    
                    showMessage([msg:'Revising plants/trees appraisals ...'])
                    svc.revisePlantTreeDetails(params)
                    if (cancelled) return;
                }
                else if ('misc'.equalsIgnoreCase(params.rputype)){
                    showMessage([msg:'Revising miscellaneous real property units ...'])
                    svc.reviseMiscRpus(params)
                    if (cancelled) return;
                    
                    showMessage([msg:'Revising miscellaneous items appraisal ...'])
                    svc.reviseMiscRpuItems(params)
                    if (cancelled) return;
                    
                    showMessage([msg:'Revising miscellaneous item parameters ...'])
                    svc.reviseMiscRpuItemParams(params)
                    if (cancelled) return;
                }
            }
            catch(e){
                e.printStackTrace();
            }
        }
        
        params.rputype = oldtype 
        
        if (params.autoapprove){
            params.counter = [success:0, error:0]
            params.interval = 250;
            params.count = 25;
            def error = false;
            def items = svc.getFaasesForRevision(params)
            
            while (items && !error){
                for(int i=0; i<items.size(); i++) {
                    if (cancelled) break;
                    try{
                        params.faas = items[i]
                        svc.approveFaas(params);
                        params.counter.success++;
                        showMessage([msg:'Successfully approved FAAS ' + params.faas.tdno + '.'])
                    }
                    catch(err){
                        err.printStackTrace()
                        if (params.continueonerror){
                            params.counter.error++;
                            err.printStackTrace();
                        }
                        else{
                            error = true 
                            showMessage([status:'ERROR', msg:'Error processing FAAS ' + params.faas.tdno + '. [ERROR] ' + err.message])
                            break;
                        }
                    }
                }
                if (!error){
                    items = svc.getFaasesForRevision(params)
                }
            }
            showMessage([status:'COMPLETED', msg:'Batch revision completed. Processed: ' + params.counter.success + ' Error: ' + params.counter.error])
        }
        else{
            def count = svc.getRevisedCount(params).revisedcount
            showMessage([status:'COMPLETED', msg:'Batch revision has successfully completed. Total Processed: ' + count]);
        }
        
    }
}

