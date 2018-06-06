package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class DeleteDelinquencyTask implements Runnable
{ 
    def svc;
    def entity;
    def afterDelete = {};
    def asyncHandler;
    
    public void start(){
        new Thread(this).start();
    }
    
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
                    afterDelete();
                }
            } 
        ] as com.rameses.common.AbstractAsyncHandler; 
        
        svc.delete([entity:[objid: entity.objid]], asyncHandler);
        sleep();
    }
    
    private void sleep(){
        try{
            Thread.sleep(3000);
        }catch(e){
            //
        }
    }
    
}
