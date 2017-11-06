package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*
import java.rmi.server.UID;
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

public class FAASGRBatchModel 
{
    @Binding
    def binding;
    
    @Service("BatchGRService")
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
        batchTask = new BatchGRTask(svc:svc, params:params,cancelled:cancelled, oncomplete:oncomplete, onrevise:onrevise);
        Thread t = new Thread(batchTask);
        t.start();
        
        processing = true;
    }
    
    
    def oncomplete = {
        msg = 'Successfully Processed: ' + counter.success + '  Errors: ' + counter.error;
        processing = false;
        binding.refresh();
    }
    
    def onrevise = {
        counter.success += it.success;
        counter.error += it.error;
        msg = 'Successfully Processed: ' + counter.success + '  Errors: ' + counter.error;
        binding.refresh('msg');
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
    // ExecutorService executor;

    def svc;
    def params;
    def items;
    def cancelled;
    def oncomplete;
    def onrevise;

    public void run(){
        println 'params.inititems -> ' + params.inititems
        if (params.inititems == true){
            svc.buildItemsForRevision(params);
        }
        
        if (params.interval == null) params.interval = 10;
        params.count = 50;
        items = svc.getFaasesForRevision(params)
        
        while (items){
            for(int i=0; i<items.size(); i++) {
                if (cancelled) break;
                try{
                    params.faas = items[i];
                    params.datacapture = true;
                    def retval = svc.reviseFaas(params);
                    onrevise(retval);
                    sleep(params.interval);
                }
                catch(err){
                    err.printStackTrace();
                }
            }
            items = svc.getFaasesForRevision(params)
        }
        
        
        oncomplete()
    }
}

