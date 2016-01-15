/*
 * AbstractService.java
 *
 * Created on January 22, 2014, 11:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import com.rameses.client.android.InvokerProxy;
import com.rameses.service.ServiceProxy;

/**
 *
 * @author wflores
 */
public abstract class AbstractService 
{
    private ServiceProxy proxy;
    
    public abstract String getServiceName(); 
 
    protected ServiceProxy getProxy() {
        if (proxy == null) {
            proxy = InvokerProxy.create(getServiceName()); 
        }
        return proxy; 
    }    
    
    public Object invoke(String action) {
        return invoke(action, null); 
    }

    public Object invoke(String action, Object arg) {
        try { 
            Object[] params = new Object[]{};
            if (arg != null) params = new Object[]{arg}; 
            
            return getProxy().invoke(action, params); 
        } catch(RuntimeException re) {
            throw re;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e); 
        }    
    }
}
