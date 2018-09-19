package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class BuildDelinquencyTask implements Runnable
{ 
    def threadPool;
    def svc;
    def entity;
    def updateStatus = {};
    def onComplete = {};
    def tasks = [];
    def cancelled = false;
    def threadCount = 1;
    
    public void start(){
        new Thread(this).start();
    }
    
    public void cancel(){
        cancelled = true;
        tasks.each{
            it.cancel();
        }
    }
    
    public void run(){
        println 'Build Delinquency threadCount => ' + threadCount
        threadPool = Executors.newFixedThreadPool(threadCount);
        
        entity.barangays.each{
            def task = new DelinquencyTask([svc: svc, entity: entity, item: it, updateStatus: updateStatus]);
            tasks << task;
            threadPool.execute(task);
        }
        
        threadPool.shutdown();
        threadPool.awaitTermination(15, TimeUnit.DAYS);
        
        if (!cancelled){
            entity.putAll(svc.closeBuild([objid:entity.objid]));
        }
        
        onComplete();
    }
}


class DelinquencyTask implements Runnable{
    def svc;
    def entity;
    def item;
    def updateStatus;
    def cancelled = false;

    public void cancel(){
    	cancelled = true;
    }

    public void run(){
        if (cancelled) {
            return;
        }
        
        def list = svc.getLedgersForProcess(item);
        while (list.size() > 0){
            if (cancelled) {
                return;
            }
            
            for (int i = 0; i < list.size(); i++){
                if (cancelled) {
                    return;
                }
                
                def ledger = list[i];
                try { 
                    ledger.builditem = item;
                    ledger.parentid = entity.objid;
                    ledger.billdate = entity.dtcomputed;
                    def retval = svc.buildDelinquency(ledger);
                    item.processed += retval.processed;
                    item.errors += retval.errors;
                    if (item.count == item.processed + item.ignored){
                        item.completed = true;
                    }
                    updateStatus(item);
                    sleep();
                } catch(e) { 
                    println 'Error rebuilding ledger '+ ledger.tdno +' caused by ' + e.message;
                } 
            } 

            list = svc.getLedgersForProcess(item);
        }
    }
    
    private void sleep(){
        try{
            Thread.sleep(500);
        }
        catch(e){
            //
        }
        
    }
}