/*
 * SessionProviderImpl.java
 *
 * Created on January 30, 2014, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import com.rameses.client.android.SessionProvider;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.client.interfaces.UserSetting;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class SessionProviderImpl extends SessionProvider 
{
    private Map source; 
    private Map env;
    private Map settings;
    
    private UserProfileImpl userProfile;
    private UserSettingImpl userSetting;
    
    public SessionProviderImpl(Map source) {
        this.source = (source == null? new HashMap(): source); 
        
        env = (Map) source.remove("env");
        if (env == null) env = new HashMap(); 
        
        settings = (Map) env.remove("SETTINGS"); 
        if (settings == null) settings = new HashMap();  
        
        userProfile = new UserProfileImpl();
        userSetting = new UserSettingImpl();
    }

    public Map getEnv() { return env; }

    public UserProfile getProfile() {
        return userProfile;
    }

    public UserSetting getSettings() {
        return userSetting;
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
    
    private class UserProfileImpl implements UserProfile 
    {
        SessionProviderImpl root = SessionProviderImpl.this; 

        public Object get(Object key) {
            return root.env.get(key); 
        }
        
        public String getUserId() {
            return root.getString(root.env, "USERID");
        }
        
        public String getUserName() {
            return root.getString(root.env, "USER");
        }

        public String getFullName() {
            return root.getString(root.env, "FULLNAME");
        }

        public String getName() {
            return root.getString(root.env, "NAME");            
        }

        public String getJobTitle() {
            return root.getString(root.env, "JOBTITLE");
        }
        
        public Map getRoles() {
            return (Map) root.getEnv().get("ROLES");
        }
    }
    
    private class UserSettingImpl implements UserSetting
    {
        SessionProviderImpl root = SessionProviderImpl.this; 
        
        public Object get(Object key) {
            return root.settings.get(key); 
        }
        
        public String getOnlineHost() {
            return getString(root.settings, "ONLINE_HOST");
        }

        public String getOfflineHost() {
            return getString(root.settings, "OFFLINE_HOST");            
        }

        public int getPort() {
            return getInt(root.settings, "PORT");
        }

        public int getSessionTimeout() {
            return getInt(root.settings, "SESSION_TIMEOUT");
        }

        public int getTrackerDelay() {
            return getInt(root.settings, "TRACKER_DELAY");
        }

        public int getUploadDelay() {
            return getInt(root.settings, "UPLOAD_DELAY");
        }
    }    
}
