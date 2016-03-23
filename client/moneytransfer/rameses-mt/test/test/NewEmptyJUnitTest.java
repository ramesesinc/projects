/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.rcp.common.LookupDataSource;
import com.rameses.rcp.common.LookupSelector;
import com.rameses.rcp.common.SimpleLookupDataSource;
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
        new SimpleLookupDataSource() { 
            public void setSearchText(String searchtext) {
            }

            public void setSelector(LookupSelector selector) {
            }
        };
    }
}
