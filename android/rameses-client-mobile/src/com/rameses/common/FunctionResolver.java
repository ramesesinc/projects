/*
 * FunctionProvider.java
 *
 * Created on June 18, 2013, 1:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.common;

import com.rameses.util.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public abstract class FunctionResolver {
    
    private static FunctionResolver instance;
    
    public static FunctionResolver getInstance() {
        if(instance==null) {
            Iterator e = Service.providers(FunctionResolver.class,FunctionResolver.class.getClassLoader());
            if(e.hasNext()) {
                instance = (FunctionResolver)e.next();
            }
        }
        if(instance==null)
            throw new RuntimeException("There is no function provider specified");
        return instance;
    }
    
    public abstract String findStringFunction( String key );
    public abstract Map getFunctionInfo(String key);
    public abstract List<Map> getAllFunctions(String n);
    public abstract List<Map> getFunctionsByGroup(String n);
    
}
