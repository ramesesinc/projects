/*
 * SessionContext.java
 *
 * Created on January 22, 2014, 10:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import com.rameses.client.interfaces.UserProfile;
import com.rameses.client.interfaces.UserSetting;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores 
 */
public class SessionContext 
{
    private static Object LOCK = new Object();
    private static SessionContext currentSession;
    
    static SessionContext getCurrent() { return currentSession; } 
    static void setCurrent(SessionContext newSession) { 
        SessionContext old = currentSession;
        if (old != null) old.close();
        
        currentSession = newSession;  
        System.out.println("[SessionContext] new session " + newSession);
    } 
    
    public static String getSessionId() {
        SessionContext sc = getCurrent();
        if (sc == null) return null;
        
        Map headers = (sc == null? null: sc.getHeaders()); 
        return sc.getString(headers, "SESSIONID");
    }
    
    public static UserProfile getProfile() {
        SessionContext sc = getCurrent();
        SessionProvider sp = (sc == null? null: sc.provider); 
        return (sp == null? null: sp.getProfile());
    }
    
    public static UserSetting getSettings() {
        SessionContext sc = getCurrent();
        SessionProvider sp = (sc == null? null: sc.provider); 
        return (sp == null? null: sp.getSettings());
    }
    
    public static String getPassword() {
        return (String) get("encpwd"); 
    } 
    
    public static Object get(String name) {
        SessionContext sc = getCurrent();
        return (sc == null? null: sc.properties.get(name)); 
    }
    
    public static void set(String name, Object value) {
        SessionContext sc = getCurrent();
        if (sc != null) sc.properties.put(name, value); 
    }
    
    
    private SessionProvider provider;    
    private AppContext appContext;
    private Map properties;
    private Map env; 
    
    SessionContext(AppContext appContext) { 
        this.appContext = appContext; 
        
        properties = new HashMap(); 
        setProvider(new SessionProviderImpl()); 
    }

    public Map getProperties() { return properties; } 
    
    public SessionProvider getProvider() { return provider; }  
    public void setProvider(SessionProvider provider) {
        if (provider == null) provider = new SessionProviderImpl();
        
        this.provider = provider;
        env = provider.getEnv();
        if (env == null) env = new HashMap();
    }
    
    void close() {
        if (properties != null) properties.clear();
        if (env != null) env.clear(); 
        
        setProvider(new SessionProviderImpl());         
    }
    
    public final Map getHeaders() { 
        env.put("CLIENTTYPE", "mobile"); 
        return env; 
    }
    
    private String getString(Map map, String name) {
        Object value = (map == null? null: map.get(name));
        return (value == null? null: value.toString()); 
    }

    private int getInt(Map map, String name) {
        try {
            return Integer.parseInt(map.get(name).toString()); 
        } catch(Throwable t) {
            return -1;
        }
    }  
    
    
    
    private class SessionProviderImpl extends SessionProvider 
    {
        private Map env = new HashMap();
        private UserProfileImpl profile = new UserProfileImpl();
        
        public Map getEnv() { 
            return env; 
        }

        public UserProfile getProfile() {
            return profile; 
        }

        public UserSetting getSettings() {
            return null; 
        }
    }
    
    private class UserProfileImpl implements UserProfile 
    {
        SessionContext root = SessionContext.this; 

        public Object get(Object key) { return null; } 
        public String getUserId() { return null; }
        public String getUserName() { return null; }
        public String getFullName() { return null; }
        public String getName() { return null; }
        public String getJobTitle() { return null; }
        public Map getRoles() { return null; }
    } 
}
