package com.rameses.waterworks.service;

import com.rameses.Main;
import com.rameses.service.ScriptServiceContext;
import com.rameses.util.Encoder;
import java.util.HashMap;
import java.util.Map;

public class LoginService {
    
    private Service service;
    public String ERROR;
    
    public LoginService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            Map env = new HashMap();
            env.put("CLIENTTYPE","web");
            service = (Service) ctx.create("LoginService", env, Service.class);
        }catch(Exception e){
            ERROR = "LoginService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("LoginService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public Map login(Map params){
        ERROR = "";
        Map result = null;
        try{
            String pwd = params.get("password")+""; 
            String user = params.get("username")+"";
            String encpwd = Encoder.MD5.encode(pwd, user.toLowerCase()); 
            
            Map map = new HashMap();
            map.putAll( params ); 
            map.put("password", encpwd);             
            result = service.login( map );
        }catch(Exception e){
            ERROR = "LoginService Error: " + e.toString();
            result = null;
            if(Main.LOG != null){
                Main.LOG.error("LoginService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service {
        
        public Map login(Object params);
        
    }
    
}
