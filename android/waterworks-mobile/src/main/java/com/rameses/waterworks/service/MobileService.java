package com.rameses.waterworks.service;

import com.rameses.Main;
import com.rameses.service.ScriptServiceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dino
 */
public class MobileService {
    
    private Service service;
    public String ERROR;
    
    public MobileService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksMobileService", Service.class);
        }catch(Exception e){
            ERROR = "MobileService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> download(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.download(params);
        }catch(Exception e){
            ERROR = "MobileService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public Map upload(Object params){
        ERROR = "";
        Map result = new HashMap();
        try{
            result = service.upload(params);
        }catch(Exception e){
            ERROR = "MobileService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service{
        
        public List<Map> download(Object params);
        
        public Map upload(Object params);
        
    }
    
}
