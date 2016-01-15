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
 * @author rameses
 */
public class TestService extends TestCase {
    
    public TestService(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public interface MyService {
        Object publish (Object data);
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        Map map = new HashMap();
        map.put("app.host", "localhost:8070");
        map.put("app.cluster", "osiris3");
        map.put("app.context", "etracs25");
        ScriptServiceContext sc = new ScriptServiceContext(map);
        MyService svc = sc.create( "QueueNotificationService", MyService.class  );
        
        Map data = new HashMap();
        data.put("msg", "hello world"); 
        System.out.println( svc.publish( data ) );
    }
}
