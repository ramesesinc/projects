/*
 * AsyncPoller.java
 *
 * Created on May 30, 2014, 2:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.service;

import com.rameses.common.AsyncToken;
import com.rameses.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class AsyncPoller 
{
    private AsyncToken token;
    private HttpClient client;
    private Map conf;
    
    public AsyncPoller(Map conf, AsyncToken token) {
        this.conf = conf;        
        this.token = token;
        
        String host = (String) conf.get("app.host");
        client = new HttpClient(host, true);
    }
    
    public Object poll() throws Exception {
        String path = "async/poll";        
        String appcontext = (String) conf.get("app.context");
        String cluster = (String) conf.get("app.cluster");
        if (cluster != null) path = cluster + "/" + path;
        
        Map params = new HashMap();
        params.put("id", token.getId());
        params.put("connection", token.getConnection());
        params.put("context", appcontext);
        return client.post(path, new Object[]{params});
    }
    
    public void close() {
        new Thread(new CloseSessionProcess()).start(); 
    }
    
    private class CloseSessionProcess implements Runnable {

        public void run() {
            try {
                String path = "async/unregister";        
                String appcontext = (String) conf.get("app.context");
                String cluster = (String) conf.get("app.cluster");
                if (cluster != null) path = cluster + "/" + path;

                Map params = new HashMap();
                params.put("id", token.getId());
                params.put("connection", token.getConnection());
                params.put("context", appcontext);
                client.post(path, new Object[]{params});
            } catch(Throwable t) {
                t.printStackTrace(); 
            }
        }
    }
}
