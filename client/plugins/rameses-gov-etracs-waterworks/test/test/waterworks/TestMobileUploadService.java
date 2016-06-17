/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.waterworks;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

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

    public void xtestMain() throws Exception { 
        System.out.println("** upload ");
        Map params = new HashMap(); 
        Map acct = new HashMap();
        acct.put("objid", "bc5432b9-f728-11e5-b778-40364febaa91-A-136"); 
        params.put("account", acct);
        params.put("batchid", "WBTC731898a9:1555b699006:-7ff4");
        params.put("objid", "WAC-" + new UID());
        params.put("userid", userid);
        params.put("name", userfullname);
        params.put("dtreading", "2016-05-11");
        params.put("reading", 4453);
        params.put("amount", 120.0);

        System.out.println( svc.upload( params ));
    }
    
    public interface IService {
        Object upload( Object o );
    }
}
