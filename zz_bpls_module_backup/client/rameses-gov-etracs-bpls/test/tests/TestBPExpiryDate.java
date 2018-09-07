/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.rameses.osiris2.test.OsirisTestPlatform;
import java.util.HashMap;
import junit.framework.TestCase;

/**
 *
 * @author dell
 */
public class TestBPExpiryDate extends TestCase {
    
    public TestBPExpiryDate(String testName) {
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
    public void testHello() throws Exception {
        OsirisTestPlatform.testWorkunit( "bpexpiry:list" , new HashMap());
    }
}
