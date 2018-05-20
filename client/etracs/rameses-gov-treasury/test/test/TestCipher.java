/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.util.Base64Cipher;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author Elmo Nazareno
 */
public class TestCipher extends TestCase {
    
    public TestCipher(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        System.out.println("println test");
        Base64Cipher cipher = new Base64Cipher();
        Map data = new HashMap();
        data.put("name","elmo");
        Object r = cipher.encode(data);
        System.out.println("decoded data " + cipher.decode(r));
    }
}
