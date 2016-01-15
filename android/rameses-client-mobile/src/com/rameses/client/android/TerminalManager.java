/*
 * TerminalManager.java
 *
 * Created on February 2, 2014, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflore 
 */
public final class TerminalManager 
{
    private final static String PREFS_KEY = "d6d9202e331f741988ff2189d217594f";
    private final static Object LOCKED = new Object();
    private static TerminalManager instance;
    
    static TerminalManager getInstance() { 
        synchronized (LOCKED) {
            if (instance == null) {
                instance = new TerminalManager();
                instance.load();
            } 
            return instance; 
        }
    }
    
    public static String getTerminalId() {
        Object value = getInstance().data.get("terminalkey");
        return (value == null? null: value.toString()); 
    }
    
    public static String getDeviceId() {
        try { 
            return getInstance().getDeviceIdImpl(); 
        } catch(Throwable t) {
            System.out.println("[TerminalManager] failed to get device id caused by " + t.getClass().getName() + ": " + t.getMessage()); 
            return null;
        }
    }

    public static void save(String terminalkey) {
        getInstance().saveImpl(terminalkey); 
    }
    
    
    private Map data;
    
    private TerminalManager() {
        data = new HashMap();
    }
    
    private void load() {
        try {
            data.clear();       
            
            SharedPreferences prefs = Platform.getApplication().getSharedPreferences(PREFS_KEY, 0);
            Map map = prefs.getAll(); 
            if (map != null) data.putAll(map); 
        } catch(Exception e) {
            System.out.println("[TerminalManager] failed to load caused by " + e.getClass().getName() + ": " + e.getMessage());
        } 
    }
    
    private void saveImpl(String terminalkey) {
        try {
            HashMap map = new HashMap();
            map.putAll(data);
            map.put("terminalkey", terminalkey.toString()); 
            
            SharedPreferences prefs = Platform.getApplication().getSharedPreferences(PREFS_KEY, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("terminalkey", terminalkey);
            editor.commit();
            data.putAll(map); 
        } catch(Exception e) {
            System.out.println("[TerminalManager] failed to load caused by " + e.getClass().getName() + ": " + e.getMessage());
        } 
    }    
    
    private synchronized String getDeviceIdImpl() {
        Object devid = data.get("deviceid");
        if (devid == null) {
            UIApplication uiapp = Platform.getApplication();
            TelephonyManager telmgr = (TelephonyManager) uiapp.getSystemService(Context.TELEPHONY_SERVICE);
            devid = telmgr.getDeviceId();
            data.put("deviceid", devid); 
        }
        return (devid == null? null: devid.toString());
    }
}
