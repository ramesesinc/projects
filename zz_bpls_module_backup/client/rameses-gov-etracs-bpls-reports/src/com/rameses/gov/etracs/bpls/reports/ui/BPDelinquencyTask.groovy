package com.rameses.gov.etracs.bpls.reports.ui;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import java.rmi.server.UID;

class BPDelinquencyTask implements Runnable
{
    def svc;
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
        updateStatus('Processing ledger for ' + selectedBarangay?.name + ' ...');

        def ROWCOUNT = 25;
        def params = [barangayid: selectedBarangay.objid]; 
        params._rowsize = params._limit = ROWCOUNT; 
        params._start = params._toprow = 0;         
        while ( true ) {
            if (cancelled) break;

            def list = svc.getApplications( params ); 
            if ( !list ) break; 

            while ( !list.isEmpty() ) { 
                if (cancelled) break; 

                def o = list.remove(0); 
                try { 
                    updateStatus('Rebuilding ledger ' + o.appno + '.');
                    svc.buildDelinquency( o ); 
                } catch(e) { 
                    def errmsg = 'Error rebuilding ledger '+ o.appno +' caused by ' + e.message;
                    println errmsg; 
                } 
            } 

            if (cancelled) break;

            params._start = params._toprow = (params._start + ROWCOUNT); 
        } 
        
        if (!cancelled) {
            new Thread(iterate_barangay).start(); 
        }
    } as Runnable;
}