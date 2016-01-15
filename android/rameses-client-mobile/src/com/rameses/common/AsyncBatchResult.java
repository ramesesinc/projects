/*
 * AsyncBatchList.java
 *
 * Created on May 29, 2014, 7:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Elmo
 */
public class AsyncBatchResult extends ArrayList implements Serializable  
{
    public AsyncBatchResult() {
    }
    
    public boolean hasEOF() {
        Object o = isEmpty()? null: get(size()-1); 
        if (o instanceof AsyncToken) {
            AsyncToken at = (AsyncToken)o;
            return at.isClosed(); 
        } else {
            return false; 
        }
    }
}
