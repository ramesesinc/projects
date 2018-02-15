package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import java.util.concurrent.*;
import com.rameses.treasury.common.models.*;

public abstract class MultiBillingModel extends PageFlowController {
    
    private ExecutorService thread = Executors.newSingleThreadExecutor();
    
    int totalcount;
    
    abstract def fetchList( def o );
    abstract void processEntry( def o );

    void onComplete( def o ) {
        def ou = super.signal( "complete" );
        binding.fireNavigation( ou );
    }
    
    int getTotalCount() {
        return processor.totalcount;
    }
    
    int getCounter() {
        return processor.counter;
    }

    boolean isCancelled() {
        return processor.cancelled;
    }
    
    public void processBill() {
        thread.submit(processor);
    }

    def processor = [
        onStart: {
            onStart();
        },
        fetchList :  { o->
            return fetchList( o );
        },
        onBatchEnd: { batchno -> 
            
        },
        process: { o->
            binding.refresh( "message" );
            processEntry(o);
        },
        onComplete : {
            onComplete();
        }
    ] as AsyncProcessorModel;
    
    
    public void cancel() {
        boolean t = MsgBox.confirm("This will abort the existing process but can be resumed next time");
        if(!t) return;
        processor.cancel();
    }
    
    public void resume() {
        processor.resume();
        thread.submit(processor);
    }
    
    
}