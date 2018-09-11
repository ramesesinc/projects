/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rameses
 */
public class TestBusinessApplicationService extends AbstractTestCase {
    
    public TestBusinessApplicationService(String testName) {
        super(testName);
    }

    public void testMain() throws Exception {
        Map params = new HashMap(); 
        params.put("businessid", "BUS7f27e63a:1619336d697:-7f76");
        params.put("apptype", "LATERENEWAL");
        
        IService svc = create("BusinessApplicationService", IService.class); 
        Object res = svc.initNew( params); 
        System.out.println( res );
    }
    
    public interface IService {
        Object initNew( Object params ); 
    }
}
