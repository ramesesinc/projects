/*
 * ClientContext.java
 *
 * Created on January 22, 2014, 12:02 PM
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
public final class ClientContext 
{
    private final static Object LOCK = new Object();
    private static TaskManager taskManager;
    
    static {
        synchronized (LOCK) {
            taskManager = new TaskManager();
        }
    }
    
    private static ClientContext current;     
    public static ClientContext getCurrentContext() { return current; }    
    static synchronized void setCurrentContext(ClientContext newContext) {
        ClientContext old = current;
        if (old != null) old.close();
        
        current = newContext; 
        System.out.println("[ClientContext] current " + current);
        if (current != null) current.init();
    }
        
    
    private UIApplication uiapp;
    private AppContext appContext;
    private SessionContext sessContext;
    
    ClientContext(UIApplication uiapp, AppContext appContext) {
        this.uiapp = uiapp;
        this.appContext = appContext;
        println("without session context");
    }
    
    ClientContext(UIApplication uiapp, AppContext appContext, SessionContext sessContext) {
        this.uiapp = uiapp;
        this.appContext = appContext;
        this.sessContext = sessContext;
        println("with session context");
    }

    private void init() { 
        println("init");
        println("session context: " + sessContext);
        if (this.sessContext != null) {
            AppContext.setInstance(this.appContext, this.sessContext);
        } else {
            AppContext.setInstance(this.appContext); 
        }
    } 
    
    public Map getAppEnv() { 
        return appContext.getEnv();  
    } 
    
    public Map getEnv() {
        SessionContext sess = appContext.getSession(); 
        return (sess == null? null: sess.getHeaders()); 
    }
        
    public TaskManager getTaskManager() { 
        return ClientContext.taskManager; 
    }
    
    private void println(String str) {
        System.out.println("[ClientContext] " + str);
    }
    
    void close() { 
        println("close1");
        AppContext.setInstance(null);
        println("close2");
        
        try { 
            taskManager.close();  
        } catch(Throwable t) { 
            t.printStackTrace(); 
        } 
        
        try { 
            appContext.close(); 
        } catch(Throwable t) { 
            t.printStackTrace(); 
        }         
    }
}
