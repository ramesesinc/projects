/*
 * AppSettings.java
 *
 * Created on February 4, 2014, 10:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author wflores 
 */
public abstract class AppSettings 
{
    private final static String PREFS_NAME = "0d0e28fb5a2806efa45d379ea9db9c9a";
    private final static int DEFAULT_SESSION_TIMEOUT = 3;
    
    private SharedPreferences prefs;
    
    private synchronized SharedPreferences getPrefs() {
        UIApplication uiapp = Platform.getApplication();
        return uiapp.getSharedPreferences(PREFS_NAME, 0);
    }
    
    public void init() {        
    }

    public int getSessionTimeout() {
        try {
            return Integer.parseInt(getString("session_timeout")); 
        } catch(Throwable t) { 
            return DEFAULT_SESSION_TIMEOUT; 
        } 
    }
    
    public String getString(String name) {
        return getString(name, null);
    }     
    public String getString(String name, String defaultValue) {
        return getPrefs().getString(name, defaultValue); 
    }
    
    public int getInt(String name) {
        return getInt(name, 0); 
    }
    public int getInt(String name, int defaultValue) {
        return getPrefs().getInt(name, defaultValue);
    }
    
    public long getLong(String name) {
        return getLong(name, 0); 
    }
    public long getLong(String name, long defaultValue) {
        return getPrefs().getLong(name, defaultValue);
    }
    
    public boolean getBoolean(String name) {
        return getBoolean(name, false); 
    }
    public boolean getBoolean(String name, boolean defaultValue) {
        return getPrefs().getBoolean(name, defaultValue);
    } 
    
    protected void beforeClear() {}
    protected void afterClear() {}
    
    public void clear() {
        beforeClear();
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.clear().commit();
        afterClear();
    }
    
    protected void beforeRemove(String name) {}
    protected void afterRemove(String name) {}
    
    public void remove(String name) {
        if (name == null) return;
    
        beforeRemove(name);
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.remove(name).commit();
        afterRemove(name);
    }
    
    protected void beforeSave(Map data) {} 
    protected void afterSave(Map data) {} 
    
    public void put(String name, Object value) { 
        if (name == null) return;
        
        Map data = new HashMap();
        data.put(name, value);
        beforeSave(data);
        SharedPreferences.Editor editor = getPrefs().edit();
        put(editor, name, value); 
        editor.commit(); 
        afterSave(data);
    } 
    
    public void putAll(Map<String,Object> map) {
        if (map == null) return;
        
        beforeSave(map);
        SharedPreferences.Editor editor = getPrefs().edit(); 
        Iterator<String> itr = map.keySet().iterator(); 
        while (itr.hasNext()) { 
            String key = itr.next();
            Object val = map.get(key);
            put(editor, key, val);
        }
        editor.commit(); 
        afterSave(map); 
    } 
    
    public Map<String,?> getAll() {
        return getPrefs().getAll();
    }
    
    private void put(SharedPreferences.Editor editor, String name, Object value) {
        if (value == null) {
            editor.putString(name, null); 
        } else if (value instanceof Boolean) {
            editor.putBoolean(name, (Boolean)value);
        } else if (value instanceof Long) {
            editor.putLong(name, (Long)value);
        } else if (value instanceof Integer) {
            editor.putInt(name, (Integer)value);
        } else if (value instanceof Float) {
            editor.putFloat(name, (Float)value);
        } else if (value instanceof String) {
            editor.putString(name, value.toString());
        } else {
            editor.putString(name, value+"");
        }
    }
    
    static class DefaultImpl extends AppSettings 
    {
        //default implementation
    }
}
