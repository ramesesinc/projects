/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

import java.util.HashMap;
import java.util.Map;

public class RemoteConfig {
    private RemoteConfig(){}
    
    public static Map getEnv(){
        String ipaddress = "localhost";
        Map env = new HashMap(); 
        env.put("app.debug",   true); 
        env.put("app.host",    ipaddress+":8071"); 
        env.put("app.context", "etracs25"); 
        env.put("app.cluster", "osiris3"); 
        env.put("readTimeout", "300000");
        env.put("ORGID", "000");
        env.put("ORGCODE", "000");
        env.put("ORGNAME", "CEBU");
        env.put("ORGCLASS", "PROVINCE");
        env.put("USERID", "delete from payable_summary;");
        env.put("USERNAME", "ADMIN");
        env.put("NAME", "ADMIN");
        env.put("FULLNAME", "ADMIN");
        env.put("JOBTITLE", "ADMIN");
        env.put("ws.host", ipaddress+":8061");
        return env;
    }
}
