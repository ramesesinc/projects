/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.waterworks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rameses
 */
public class TestMobileUploadService extends AbstractTestCase {
    
    final LinkedBlockingQueue LOCK = new LinkedBlockingQueue(); 
    
    IService svc; 
    String userid; 
    String userfullname; 
    
    public TestMobileUploadService(String testName) {
        super(testName);
    }

    protected void afterSetup() { 
        svc = create("WaterworksMobileUploadService", IService.class); 
        userid = "USR-56b200c1:153d499966d:-7fe9"; 
        userfullname = "FERNANDO ESTRADA"; 
    }

    public void testMain() throws Exception { 
        System.out.println("** upload ");
        Map params = new HashMap(); 
        params.put("batchid", "WBTC-2d17ca2e:154c26b8ea5:-7ff2");
        params.put("objid", "f988887b-8279-4758-8c2f-974f8e8e73c6");
        params.put("userid", userid);
        params.put("name", userfullname);
        params.put("dtreading", "2016-05-18");
        params.put("reading", 2658);
        params.put("amount", 253.0);

        Map acct = new HashMap();
        acct.put("objid", "bc567d08-f728-11e5-b778-40364febaa91-B-189"); 
        params.put("account", acct);

        System.out.println( svc.upload( params ));
    }
    
    public interface IService {
        Object upload( Object o );
    }
}
