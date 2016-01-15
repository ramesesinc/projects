/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on January 22, 2014, 1:21 PM
 */

package test;

import android.os.Bundle;
import junit.framework.*;

/**
 *
 * @author compaq
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void testMain() throws Exception {
        String datetime = new java.sql.Timestamp(System.currentTimeMillis()).toString().replaceAll("-|:|\\.", "");
        System.out.println(datetime);
    }
}
