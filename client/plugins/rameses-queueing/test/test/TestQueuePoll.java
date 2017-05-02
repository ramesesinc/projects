/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.http.HttpClient;
import junit.framework.TestCase;

/**
 *
 * @author rameses
 */
public class TestQueuePoll extends TestCase {
    
    public TestQueuePoll(String testName) {
        super(testName);
    }

    public void test1() throws Exception {
        HttpClient c = new HttpClient("http://localhost:8080/");
    }
}
