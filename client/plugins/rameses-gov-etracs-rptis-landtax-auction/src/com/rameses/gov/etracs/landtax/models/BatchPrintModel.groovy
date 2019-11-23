package com.ramees.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BatchPrintModel
{
    @Binding
    def binding;
    
    def items;
    def reportHandler;
    def report;
    def printing;
    def cancelled;
    def numCopies;
    
    void init() {
        printing = false;
        numCopies = 1;
        report = Inv.lookupOpener(reportHandler, [:]).handle;
    }
    
    void printAll() {
        new Thread(task).start();
    }
    
    void onComplete() {
        printing = false;
        binding.refresh();
    }
    
    def cancel() {
        if (!printing) {
            return '_close';
        } else {
            printing = false;
            cancelled = true;
            binding.refresh();
        }
    }
    
    def task = [
        run : {
            printing = true; 
            cancelled = false;
            binding.refresh();
            def item = items.remove(0);
            while (item && !cancelled) {
                report.entity = item;
                report.printImmediate();
                sleep(1000);
                item = null;
                if (items) {
                    item = items.remove(0);
                }
            }
            if (cancelled) {
                cancel();
            } else {
                onComplete();
            }
        }
    ] as Runnable
    
    void sleep(millis) {
        try{
            Thread.sleep(millis);
        }catch(e) {
            //ignore 
        }
    }
    
}