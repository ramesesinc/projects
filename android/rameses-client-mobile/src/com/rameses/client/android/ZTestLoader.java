/*
 * TestLoaderService.java
 *
 * Created on February 3, 2014, 2:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import java.util.Properties;

/**
 *
 * @author compaq
 */
public class ZTestLoader {
    
    public ZTestLoader() {
    }
    
    public void load(Properties appenv) {
        AppContextImpl ac = new AppContextImpl(appenv);
        if (appenv.getProperty("readTimeout") == null) {
            appenv.put("readTimeout", "20000");
        } 
        
        ClientContext cctx = new ClientContext(null, ac); 
        ClientContext.setCurrentContext(cctx); 
    }

}
