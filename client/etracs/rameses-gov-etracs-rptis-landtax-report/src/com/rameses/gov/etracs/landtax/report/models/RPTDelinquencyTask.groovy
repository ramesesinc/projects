package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;


class RPTDelinquencyTask implements Runnable
{
    def svc;
    def currentdate;
    def oncancel = {};    
    def oncomplete = {};
    def updateStatus = {};
    def cancelled = false;
    def params = [:]; 
    
    def barangays = []; 
    def selectedBarangay;
    def cleanupAsyncHandler;
    
    public void start() { 
        new Thread(this).start(); 
    } 
    
    public void run(){
        cancelled = false; 
        selectedBarangay = null; 
        cleanupAsyncHandler = null; 
        
        cleanup();
    }
    
    public void cancel() {
        cancelled = true; 
        cleanupAsyncHandler?.cancel();
    }
    
    private void cleanup() {
        updateStatus('Cleanup previous records...');
        cleanupAsyncHandler = [
            onError: {o-> 
                MsgBox.err(o); 
                if (oncancel) oncancel();
            }, 
            onTimeout: {
                if (cancelled) {
                    cleanupAsyncHandler.cancel();
                } else {
                    cleanupAsyncHandler.retry(); 
                }
            },
            onCancel: {
                if (oncancel) oncancel(); 
            }, 
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                    updateStatus('Loading barangays please wait...');
                    barangays = [];                     
                    selectedBarangay = null; 
                    if ( params.barangay?.objid ) { 
                        barangays = [params.barangay]; 
                    } else { 
                        barangays.addAll( params.barangaylist ); 
                    } 
                    new Thread(iterate_barangay).start(); 
                } 
            } 
        ] as com.rameses.common.AbstractAsyncHandler; 
        
        svc.cleanup([barangay: params.barangay], cleanupAsyncHandler);
    }
    
    def iterate_barangay = {
        if (barangays) {
            selectedBarangay = barangays.remove(0); 
            new Thread(process_barangay).start(); 
        } else { 
            if (!cancelled) oncomplete(); 
        } 
        
    } as Runnable;
    
    def process_barangay = {
        updateStatus('Processing ledgers for ' + selectedBarangay?.name + ' ...');

        def list = svc.getOpenLedgersByBarangay(selectedBarangay);
        for (int i = 0; i < list.size(); i++){
            if (cancelled) break;

            def ledger = list[i];
            try { 
                updateStatus('Rebuilding ledger ' + ledger.tdno + '.');
                svc.buildDelinquency(ledger, currentdate);
            } catch(e) { 
                def errmsg = 'Error rebuilding ledger '+ ledger.tdno +' caused by ' + e.message;
                println errmsg; 
            } 
        } 
        
        if (!cancelled) {
            new Thread(iterate_barangay).start(); 
        }
    } as Runnable;
}