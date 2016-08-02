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
public class TestService extends AbstractTestCase {
    
    public TestService(String testName) {
        super(testName);
    }

    public void testMain() throws Exception {
        Map params = new HashMap(); 
        params.put("applicationid", "BARNW-2109352c:1562602cefa:-7e63"); 
        params.put("_silent", true); 
        params.put("_with_taxfees", false); 
        params.put("_with_items", false); 
        
        IService svc = create("BusinessCashReceiptService", IService.class); 
        Object res = svc.getBillingForPayment( params ); 
        System.out.println( res );
    }
    
    
    public interface IService {
        Object getBillingForPayment( Object params ); 
    }
}
