/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author dell
 */
public abstract class AbstractTest extends TestCase {
    
    protected Map profile;
    
    public AbstractTest() {
        super("Tester");
    }
    
    @Override
    protected void setUp() throws Exception {
        profile = new HashMap();
        profile.put("ORGCLASS", "municipality");
        profile.put("ORGID", "010-01");
        profile.put("USERID", "USR-26566a55:147bedb945a:-7fee");
        profile.put("USERNAME", "NAZARENO, ELMO");
        profile.put("JOBTITLE", "RC1");
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
}
