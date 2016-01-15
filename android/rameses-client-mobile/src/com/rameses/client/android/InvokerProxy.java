/*
 * InvokerProxy.java
 *
 * Created on January 22, 2014, 11:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;

/**
 *
 * @author wflores
 */
public final class InvokerProxy 
{
    private static Object LOCK = new Object();
    
    public static ServiceProxy create(String name) {
        synchronized (LOCK) { 
            ClientContext cctx = ClientContext.getCurrentContext(); 
//            System.out.println("app env " + cctx.getAppEnv());
            ScriptServiceContext ssc = new ScriptServiceContext(cctx.getAppEnv()); 
            return ssc.create(name, cctx.getEnv()); 
        } 
    }

    private InvokerProxy() {
    }
}
