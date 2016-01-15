/*
 * UserSetting.java
 *
 * Created on January 28, 2014, 3:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.interfaces;

/**
 *
 * @author wflors
 */
public interface UserSetting 
{
    Object get(Object key);
    String getOnlineHost();
    String getOfflineHost();
    int getPort();
    int getSessionTimeout();
    int getTrackerDelay();
    int getUploadDelay();
    
}
