/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.service.ScriptServiceContext;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author ramesesinc
 */
public class TestService extends TestCase {
    
    private ScriptServiceContext ctx; 
    private IService svc; 
    
    public TestService(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        Map appenv = new HashMap();
        appenv.put("app.host", "localhost:8070");
        appenv.put("app.cluster", "osiris3");
        appenv.put("app.context", "etracs25");        
        ctx = new ScriptServiceContext(appenv);            
        svc = ctx.create("PersistenceService", IService.class);
    }  
    

    public void testRemoveEntity() throws Exception { 
        Map params = new HashMap();
        params.put("_schemaname", "waterworks_migrationitem"); 
        params.put("parentid", "a");
        
        Map findby = new HashMap();
        findby.put("parentid", "a"); 
        params.put("findBy", findby);
        
        svc.removeEntity(params);
    }
    
    public interface IService {
        Object removeEntity( Object params );
    }    
}
