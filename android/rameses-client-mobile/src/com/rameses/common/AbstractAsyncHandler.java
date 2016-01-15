/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.common;

/**
 *
 * @author wflores 
 */
public abstract class AbstractAsyncHandler implements AsyncHandler 
{
    private boolean has_request_retry;
    private boolean has_request_cancel;
    
    public void call(Object o) {
        //do nothing
    }
    
    public void onTimeout() {
        //do nothing
    }
    
    public void onCancel() {
        //do nothing
    }
    
    public final boolean hasRequestRetry() { 
        return has_request_retry; 
    } 
    
    public void retry() {
        has_request_retry = true; 
    } 
    
    public final boolean hasRequestCancel() {
        return has_request_cancel; 
    }
    
    public void cancel() {
        has_request_cancel = true; 
    }
}
