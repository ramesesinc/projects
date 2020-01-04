package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class BuildDeliquentLedgerTask implements Runnable
{ 
    def threadPool;
    def svc;
    def entity;
    def barangay;
    def cancelled = false;

    def updateStatus = {};
    def afterLoadLedgersLoad = {};
    def afterLoadLedgers = {}
    
    public void start(){
        new Thread(this).start();
    }
    
    public void cancel(){
        
    }
    
    public void run(){
        def list = entity.barangays.findAll{it.count == 0}
        if (list){
            threadPool = Executors.newFixedThreadPool(1);
            list.each{
                def task = new LoadLedgerTask([svc: svc, entity: entity, item: it, updateStatus: updateStatus]);
                threadPool.execute(task);
            }
            threadPool.shutdown();
            threadPool.awaitTermination(15, TimeUnit.DAYS);
        }
        sleep();
        afterLoadLedgers();
    }
    
    private void sleep(){
        try{
            Thread.sleep(50);
        }catch(e){
            //
        }
    }
    
}




class LoadLedgerTask implements Runnable{
    def svc;
    def entity;
    def item;
    def updateStatus;
    def asyncHandler;
    
    public void run(){
        asyncHandler = [
            onError: {o-> 
            }, 
            onTimeout: {
            },
            onCancel: {
            }, 
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                    //do nothing
                } else if ( o instanceof Throwable){
                    //do noting
                } else{
                    item.putAll(svc.getDelinquentBarangayInfo(o));
                    updateStatus(item);
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler; 
        
        updateStatus(item);
        svc.loadLedgers([entity:[objid: entity.objid], item: item], asyncHandler);
        sleep();
    }
    
    private void sleep(){
        try{
            Thread.sleep(500);
        }catch(e){
            //
        }
    }
}