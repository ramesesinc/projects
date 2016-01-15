/*
 * SessionProvider.java
 *
 * Created on January 30, 2014, 10:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import com.rameses.client.interfaces.UserProfile;
import com.rameses.client.interfaces.UserSetting;
import java.util.Map;

/**
 *
 * @author wflores
 */
public abstract class SessionProvider 
{
    
    public SessionProvider() {
    }
    
    public abstract Map getEnv();    
    public abstract UserProfile getProfile();
    public abstract UserSetting getSettings();
    
}
