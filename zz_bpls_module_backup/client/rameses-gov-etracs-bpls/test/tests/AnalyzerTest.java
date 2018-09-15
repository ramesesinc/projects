/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.rameses.osiris2.test.OsirisTestPlatform;

/**
 *
 * @author dell
 */
public class AnalyzerTest extends AbstractTest {
    
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        OsirisTestPlatform.setProfile(profile);
        OsirisTestPlatform.testWorkunit("bpanzalyzer:test", null);
    }
}
