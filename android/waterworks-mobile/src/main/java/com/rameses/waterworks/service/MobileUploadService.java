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
public class MobileUploadService {
    
    private Service service;
    public String ERROR;
    
    public MobileUploadService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksMobileUploadService", Service.class);
        }catch(Exception e){
            ERROR = "MobileUploadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileUploadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    
    public Map upload(Object params){
        ERROR = "";
        Map result = new HashMap();
        try{
            result = service.upload(params);
        }catch(Exception e){
            ERROR = "MobileUploadService Error: " + e.toString();
            result = new HashMap();
            if(Main.LOG != null){
                Main.LOG.error("MobileUploadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    
    static interface Service{  
        public Map upload(Object params);
    }
    
}
