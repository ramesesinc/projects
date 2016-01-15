/*
 * LogoutService.java
 *
 * Created on January 23, 2014, 11:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import com.rameses.client.android.AppContext;
import com.rameses.client.android.SessionContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class LogoutService extends AbstractService
{
    
    public String getServiceName() {
        return "LogoutService"; 
    }
    
    public void logout() {
        String sessionid = SessionContext.getSessionId();
        if (sessionid == null) return;
        
        Map param = new HashMap();
        param.put("SESSIONID", sessionid); 
        try { 
            invoke("logout", param);
            //AppContext.getSession().setProvider(null); 
        } catch(RuntimeException re) {
            throw re; 
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e); 
        }
        /*finally {
            AppContext.getSession().setProvider(null); 
        }*/
    }
}
