package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public abstract class AsyncProcessorModel implements Runnable {

    int totalcount = 0;
    int counter = 1;
    int rows = 10;
    int batchno = 1;
    
    boolean cancelled = false;    
    
    public final void cancel() {
        cancelled = true;
    }
    
    public final void resume() {
        cancelled = false;
    }
    
    abstract List fetchList( def o );
    
    abstract void process( def o );
    public void onStart() {;}
    public void onRecordStart(def o) {;}
    public void onRecordEnd(def o) {;}
    public void onBatchStart( def o) {;}
    public void onBatchEnd(def o) {;}
    public void onComplete() {;}
    public void onCancel() {;}
    
    public void run() {
        
        onStart();
        while( !cancelled ) {
            def list = fetchList( [_limit: rows] );
            if(!list) break;
            def newList = [];
            newList.addAll( list );
            onBatchStart(batchno);
            for( o in newList ) {
                if(cancelled) break;
                onRecordStart( counter );
                process(o);
                onRecordEnd(counter);
                counter++;
            }    
            onBatchEnd(batchno);
            batchno++;
        }
        if( cancelled ) {
            onCancel();
        }
        else {
            onComplete();
        }
    }
    
}