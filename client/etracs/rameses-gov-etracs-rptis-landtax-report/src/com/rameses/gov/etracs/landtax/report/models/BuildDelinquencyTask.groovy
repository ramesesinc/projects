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
    def barangays;
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
        tasks.clear();
    }
    
    public void run(){
        threadPool = Executors.newFixedThreadPool(threadCount);
        
        barangays.each{
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
        def list = svc.getLedgersForProcess(item);
        while (!cancelled) {
            if (list.size() == 0) break;
            def ledger = list[0]
            ledger.builditem = item;
            ledger.parentid = entity.objid;
            ledger.billdate = entity.dtcomputed;

            def retval = svc.buildDelinquency(ledger);
            item.processed += retval.processed;
            item.errors += retval.errors;
            item.completed = (item.count == item.processed + item.errors + item.ignored)
            list.remove(ledger);
            if (list.size() % 25 == 0) {
                updateStatus(item);
                sleep(1500);
            } else {
                sleep(50);
            }
        }
    }

    private void sleep(ms){
        try{
            Thread.sleep(ms);
        }
        catch(e){
            //
        }
        
    }


}