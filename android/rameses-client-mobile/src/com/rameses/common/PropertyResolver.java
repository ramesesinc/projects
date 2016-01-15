/*
 * PropertyResolver.java
 *
 * Created on June 7, 2010, 8:12 AM
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
public abstract class PropertyResolver {
    
    //implementation
    private static PropertyResolver instance;
    
    public static PropertyResolver getInstance() {
        if(instance==null) {
            Iterator e = Service.providers(PropertyResolver.class,PropertyResolver.class.getClassLoader());
            if(e.hasNext()) {
                instance = (PropertyResolver)e.next();
            }
        }
        if(instance==null)
            throw new RuntimeException("There is no property resolver specified");
        return instance;
    }
    
    public abstract boolean setProperty(Object bean, String propertyName, Object value );
    public abstract Class getPropertyType(Object bean, String propertyName );
    public abstract Object getProperty( Object bean, String propertyName );
}
