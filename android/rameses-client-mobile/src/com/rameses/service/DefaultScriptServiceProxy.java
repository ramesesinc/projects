/*
 * DefaultServiceProxy.java
 * Created on September 21, 2011, 9:04 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service;

import java.util.Map;

/**
 *
 * @author jzamss
 * Path example:
 * http://localhost:8080/cluster1/services/context/MyService.sayTest
 */
public class DefaultScriptServiceProxy extends AbstractServiceProxy  implements ServiceProxy{
    
    private Map env;
    private String skey = "";
    
    public DefaultScriptServiceProxy(String scriptName, Map conf, Map env) {
        super(scriptName, conf);
        this.env = env;
    }
    
    public Object invoke(String action, Object[] params) throws Exception {
        Object[] args = new Object[] {
            params,
            this.env
        };
        String appContext = (String) super.conf.get("app.context");
        String path = "services/" + appContext+"/"+serviceName+"."+action;
        String cluster = (String) super.conf.get("app.cluster");
        if( cluster !=null ) path = cluster + "/" + path;
        return client.post(path, args );
    }
    
    public Object invoke(String action) throws Exception {
        return invoke(action, null);
    }
}
