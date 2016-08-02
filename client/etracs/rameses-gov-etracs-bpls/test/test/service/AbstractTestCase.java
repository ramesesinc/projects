/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.service;

import com.rameses.service.ScriptServiceContext;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author rameses
 */
public abstract class AbstractTestCase extends TestCase {
    
    private ScriptServiceContext ctx;
    
    private Map appenv = new HashMap();
    private Map env = new HashMap();
    
    public AbstractTestCase(String testName) {
        super(testName);
    }
    
    public ScriptServiceContext getContext() { return ctx; } 
    public Map getAppEnv() { return appenv; }
    public Map getEnv() { return env; } 

    protected void setUp() throws Exception {
        env = new HashMap();
        env.put("USER", "ADMIN");
        env.put("USERID", "ADMIN");
        env.put("FULLNAME", "ADMIN A. ADMIN");
        env.put("NAME", "ADMIN, ADMIN A.");
        loadEnv( env );
        
        appenv = new HashMap();
        appenv.put("app.host", "localhost:8070");
        appenv.put("app.cluster", "osiris3");
        appenv.put("app.context", "etracs25");
        appenv.put("readTimeout", "60000");
        appenv.put("connectTimeout", "60000");
        loadAppEnv( appenv ); 
        
        ctx = new ScriptServiceContext(appenv); 
        afterSetup();
    } 
    
    protected void loadEnv( Map env ) {
        //do nothing 
    }
    protected void loadAppEnv( Map env ) {
        //do nothing 
    }
    protected void afterSetup() {
    }
    protected <T> T create(String serviceName) {
        return create( serviceName, null ); 
    }
    protected <T> T create(String serviceName,  Class<T> clz) {
        return ctx.create( serviceName, env, clz );
    }
}
