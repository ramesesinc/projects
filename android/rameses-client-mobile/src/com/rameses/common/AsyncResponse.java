/*
 * AsyncResponse.java
 *
 * Created on December 2, 2010, 9:56 AM
 * @author jaycverg
 */

package com.rameses.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class AsyncResponse extends HashMap implements Serializable 
{
    
    private static final long serialVersionUID = 1L;
    
    public static final int STARTED = 0;
    public static final int PROCESSING = 1;
    public static final int COMPLETED = 2;
    public static final int TIMEOUT = 5;
    
    private int status;
     
    private LinkedBlockingQueue queue = new LinkedBlockingQueue();
    
    
    public AsyncResponse(Object value) {
        addValue( value );
        this.status = COMPLETED;
    }
    
    public AsyncResponse() {
        this.status = COMPLETED;
    }
    
    public AsyncResponse(Object value, int status) {
        addValue( value );
        this.status = status;
    }
    
    public Object getNextValue() {
        return queue.poll();
    }
    
    public void addValue(Object value) {
        queue.add( value );
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public void completed() {
        this.status = COMPLETED;
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
}
