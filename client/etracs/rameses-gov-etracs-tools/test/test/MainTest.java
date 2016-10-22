/*
 * MainTest.java
 * JUnit based test
 *
 * Created on November 7, 2013, 2:19 PM
 */

package test;

import com.rameses.io.FileUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author Rameses
 */
public class MainTest extends TestCase {
    
    public MainTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void xtestExport() {
        FileUtil fu = new FileUtil();
        File f = new File("d:/temp/test.rem");
        Map data = new HashMap();
        data.put("name", "SANTOS");
        data.put("title", "RCC II");
        fu.writeObject(f, data);
    }
    
    public void xtestImport(){
        FileUtil fu = new FileUtil();
        File f = new File("d:/temp/test.rem");
        Map data = (Map)fu.readObject(f);
        System.out.println("name -> " + data.get("name"));
        System.out.println("title -> " + data.get("title"));
    }
    
    public void test1() throws Exception {
        String str = "SAN     PEDRO 20160929 1046";
        System.out.println( str.replaceAll("[\\s]{1,}", "_") );
    }

}
