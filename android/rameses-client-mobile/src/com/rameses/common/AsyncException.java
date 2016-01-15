/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.common;

/**
 *
 * @author wflores
 */
public class AsyncException extends RuntimeException {
    
    static final long serialVersionUID = 1L;
    
    public AsyncException(String message) {
        super(message); 
    }
    
    public AsyncException(String message, Throwable cause) {
        super(message, cause); 
    }
}
