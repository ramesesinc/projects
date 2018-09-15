/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.rameses.osiris2.test.OsirisTestPlatform;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author dell
 */
public class TestReport extends TestCase {
    
    public TestReport(String testName) {
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
        OsirisTestPlatform.setConf( new HashMap());
        Map map = new HashMap();
        map.put("lastname", "Flores");
        
        Map m = new HashMap();
        m.put("entity", map);
        OsirisTestPlatform.testWorkunit( "sample:report" , m);
    }
}
