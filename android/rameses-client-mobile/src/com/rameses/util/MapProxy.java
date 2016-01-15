/*
 * MapProxy.java
 *
 * Created on February 5, 2014, 12:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author wflores
 */
public class MapProxy implements Map 
{
    public static synchronized String getString(Map map, Object key) {
        return new MapProxy().getStringImpl(map, key);
    }
    
    public static synchronized Integer getInteger(Map map, Object key) {
        return new MapProxy().getIntegerImpl(map, key);
    }
    
    public static synchronized Double getDouble(Map map, Object key) {
        return new MapProxy().getDoubleImpl(map, key);
    }    

    public static synchronized Boolean getBoolean(Map map, Object key) {
        return new MapProxy().getBooleanImpl(map, key);
    }    

    
    private Map source;
    
    private MapProxy() {}
    
    public MapProxy(Map source) {
        this.source = (source == null? new HashMap(): source);
    }

    public int size() { 
        return source.size();
    }

    public boolean isEmpty() {
        return source.isEmpty();
    }

    public boolean containsKey(Object key) {
        return source.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return source.containsValue(value);
    }

    public Object get(Object key) {
        return source.get(key);
    }

    public Object put(Object key, Object value) {
        return source.put(key, value);
    }

    public Object remove(Object key) {
        return source.remove(key);
    }

    public void putAll(Map t) {
        source.putAll(t); 
    }

    public void clear() {
        source.clear();
    }

    public Set<Object> keySet() {
        return source.keySet();
    }

    public Collection<Object> values() {
        return source.values();
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return source.entrySet();
    }

    public String getString(Object key) {
        return getStringImpl(source, key);
    }

    private String getStringImpl(Map map, Object key) {
        Object value = (map == null? null: map.get(key));
        return (value == null? null: value.toString()); 
    }
    
    public Integer getInteger(Object key) {
        return getIntegerImpl(source, key);
    }
    
    private Integer getIntegerImpl(Map map, Object key) {
        Object value = (map == null? null: map.get(key));
        try {
            if (value instanceof Integer) {
                return (Integer)value;
            }
            int num = Integer.parseInt(value.toString()); 
            return new Integer(num);
        } catch(Throwable t) {
            return null; 
        }
    }
    
    public Double getDouble(Object key) {
        return getDoubleImpl(source, key);
    }
    
    private Double getDoubleImpl(Map map, Object key) {
        Object value = (map == null? null: map.get(key));
        try {
            if (value instanceof Double) {
                return (Double)value;
            }
            double num = Double.parseDouble(value.toString()); 
            return new Double(num);
        } catch(Throwable t) {
            return null; 
        }
    }    
    
    public Boolean getBoolean(Object key) {
        return getBooleanImpl(source, key);
    }
    
    private Boolean getBooleanImpl(Map map, Object key) {
        Object value = (map == null? null: map.get(key));
        try {
            if (value instanceof Boolean) {
                return (Boolean)value;
            }
            if ("true".equals(value)) {
                return true;
            } else if ("false".equals(value)) {
                return false;
            } else {
                return null; 
            }
        } catch(Throwable t) {
            return null; 
        }
    }        
}
