/*
 * TimerTaskTest.java
 * JUnit based test
 *
 * Created on January 27, 2014, 4:21 PM
 */

package test;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import junit.framework.*;

/**
 *
 * @author compaq
 */
public class TimerTaskTest extends TestCase {
    
    public TimerTaskTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void testMain() throws Exception {
        Timer timer = new Timer();
        timer.schedule(new TaskImpl("task#1"), 0, 5000);
        timer.schedule(new TaskImpl("task#2"), 0, 6000);
        
        JOptionPane.showMessageDialog(null, "Wait...");
        timer.cancel();
    }

    private class TaskImpl extends TimerTask 
    {
        private String name;
        
        TaskImpl(String name) {
            this.name = name; 
        }
        
        public void run() {
            System.out.println("[" + name + "] run...");
            try { 
                Thread.sleep(8000); 
            } catch(Throwable t) {
                //do nothing
            } finally {
                System.out.println("[" + name + "] sleep finished");
            }
        }
    }
}
