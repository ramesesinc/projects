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
public class ReadingService {
    
    private Service service;
    public String ERROR;
    
    public ReadingService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksMeterReadingService", Service.class);
        }catch(Exception e){
            ERROR = "ReadingService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("ReadingService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public Map create(Object params){
        Map data = new HashMap();
        try{
            data = service.create(params);
        }catch(Exception e){
            ERROR = "ReadingService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("ReadingService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return data;
    }
    
    static interface Service{
        
        public Map create(Object params);
        
    }
    
}
