/*
 * SecurityManager.java
 *
 * Created on January 27, 2014, 12:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import java.util.Map;

/**
 *
 * @author wflores
 */
public class SecurityManager 
{
    private static Object LOCK = new Object();
    private static SecurityManager currentSecurityManager;
    
    static SecurityManager getCurrent() { return currentSecurityManager; } 
    static void setCurrent(SecurityManager newSecurityManager) { 
        SecurityManager old = currentSecurityManager;
        if (old != null) old.close();
        
        currentSecurityManager = newSecurityManager;  
    } 

    public static synchronized boolean checkPermission(String domain, String role, String name) { 
        if (currentSecurityManager == null) return false; 
        
        SessionContext sess = currentSecurityManager.sessionContext;
        if (sess == null) return false; 
        
        Map headers = sess.getHeaders();
        Object value = (headers == null? null: headers.get("ROLES"));
        if (!(value instanceof Map)) return false; 

        Map roles = (Map) value; 
        if (role != null && role.length() > 0) {
            String[] arrays = role.split(",");
            for (int i=0; i<arrays.length; i++) {
                String srole = arrays[i].trim();
                if (domain != null) srole = domain+"."+srole;
                if (!roles.containsKey(srole)) continue;
                
                if (name == null || name.trim().length() == 0) return true;
                
                String disallowed = (String) roles.get(srole);
                if (disallowed != null && name.matches(disallowed)) continue;
                
                return true;
            } 
            return false; 
        } 
        
        if (name != null && name.length() > 0) { 
            String allowed = (String)roles.get("ALLOWED");
            if (allowed != null && name.matches(allowed)) 
                return true; 
            else 
                return false;
        }
        
        return (role == null && name == null? true: false);
    } 
    
    private SessionContext sessionContext;
    
    SecurityManager(SessionContext sessionContext) {
        this.sessionContext = sessionContext; 
    }
    
    void close() {
    }
    
}
