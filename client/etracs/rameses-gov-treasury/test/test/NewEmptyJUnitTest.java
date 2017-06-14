/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.util.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

/**
 *
 * @author rameses
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    public void testMain() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        Date dt = new java.sql.Date( sdf.parse("2016-01-01").getTime() );
        System.out.println( dt );
                
        System.out.println( DateUtil.add(dt, "12M")); 
    }
}
