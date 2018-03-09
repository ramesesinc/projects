/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import junit.framework.TestCase;

/**
 *
 * @author Elmo Nazareno
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
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
        //object2.action2 match with object1\\.(action1|create)
//object1.action1 match with object1\\.(action1|create)
//object1.edit match with object1\\.(action1|create)
//object1.create match with object1\\.(action1|create)

        System.out.println( "object1.action1".matches( "object1\\\\.(action1|create)" )); 
        
    }
}
