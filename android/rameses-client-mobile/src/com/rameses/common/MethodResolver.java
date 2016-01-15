/*
 * MethodResolver.java
 *
 * Created on August 27, 2009, 8:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.common;

import com.rameses.util.Service;
import java.util.Iterator;

/**
 *
 * @author elmo
 */
public abstract class MethodResolver {
    
    //implementation
    private static MethodResolver instance;
    
    public static MethodResolver getInstance() {
        if(instance==null) {
            Iterator e = Service.providers(MethodResolver.class,MethodResolver.class.getClassLoader());
            if(e.hasNext()) {
                instance = (MethodResolver)e.next();
            }
        }
        if(instance==null)
            throw new RuntimeException("There is no method resolver specified");
        return instance;
    }
    
    public abstract Object invoke( Object bean, String action, Class[] paramTypes, Object[] args ) throws Exception;
    public abstract Object invoke( Object bean, String action, Object[] args ) throws Exception;
    
    
}
