/*
 * EnvMap.java
 *
 * Created on January 22, 2014, 11:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class EnvMap extends HashMap
{
    public EnvMap() {
    }
    
    public EnvMap(Map map) {
        if (map != null) this.putAll(map); 
    }    
    
    public Object get(Object key) {
        if( super.containsKey(key)) {
            return super.get(key);
        }
        else {
            key = (key + "").replaceAll("_", ".");
            return System.getProperty( key + "");
        } 
    } 
}
