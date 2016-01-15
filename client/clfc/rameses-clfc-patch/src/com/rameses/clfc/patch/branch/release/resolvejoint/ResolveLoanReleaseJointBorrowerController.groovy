package com.rameses.clfc.patch.branch.release.resolvejoint

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

class ResolveLoanReleaseJointBorrowerController 
{
    @Binding
    def binding;
    
    @Service('ResolveLoanReleaseJointBorrowerService')
    def service;
    
    @Service('DateService')
    def dateSvc;
    
    def startdate, enddate;
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);
    
    String title = 'Resolve Loan Release Joint Borrower(s)';
    
    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
    }
    
    def resolve() {
        def handler
        if (!handler) {
            handler = [
                onMessage: { o->
                    //println 'onMessage '  + o;
                    //println 'EOF ' + AsyncHandler.EOF;
                    if (o == AsyncHandler.EOF) {
                        loadingOpener.handle.binding.fireNavigation("_close");
                        return;
                    }        

                    loadingOpener.handle.binding.fireNavigation("_close");
                    
                    def msg = "Successfully resolved joint borrower(s)!";
                    
                    binding?.refresh();
                    MsgBox.alert(msg);
                },
                onTimeout: {
                    handler?.retry(); 
                },
                onCancel: {
                    println 'processing cancelled.';
                    //fires when cancel() method is executed 
                }, 
                onError: { o->
                    loadingOpener.handle.binding.fireNavigation("_close");
                    MsgBox.err(o.message);
                    /*if (o instanceof java.util.concurrent.TimeoutException) {

                    } else {
                        throw new Exception(o.message);
                    }*/
                }
            ] as AbstractAsyncHandler;
        }
        service.resolveJointBorrowers([startdate: startdate, enddate: enddate], handler);
        return loadingOpener;
    }
    
    def close() {
        return '_close';
    }
}

