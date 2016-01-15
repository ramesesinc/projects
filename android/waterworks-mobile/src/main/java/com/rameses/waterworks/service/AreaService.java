package com.rameses.waterworks.service;

import com.rameses.Main;
import com.rameses.service.ScriptServiceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dino
 */
public class AreaService {
    
    private Service service;
    public String ERROR;
    
    public AreaService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksAreaService", Service.class);
        }catch(Exception e){
            ERROR = "AreaService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("AreaService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getMyAssignedAreas(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getMyAssignedAreas(params);
        }catch(Exception e){
            ERROR = "AreaService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("AreaService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service{
        
        public List<Map> getMyAssignedAreas(Object params);
        
    }
    
}
