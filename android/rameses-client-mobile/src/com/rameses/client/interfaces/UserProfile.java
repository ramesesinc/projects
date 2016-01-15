/*
 * UserProfile.java
 *
 * Created on January 23, 2014, 10:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.interfaces;

import java.util.Map;

/**
 *
 * @author wflores
 */
public interface UserProfile 
{
    Object get(Object key);
    String getUserId();
    String getUserName();
    String getFullName();
    String getName();
    String getJobTitle();
    Map getRoles();
} 
