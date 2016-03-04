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
public class ReadingGroupService {
    
    private Service service;
    public String ERROR;
    
    public ReadingGroupService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksReadingGroupService", Service.class);
        }catch(Exception e){
            ERROR = "ReadingGroupService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("ReadingGroupService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getListByAssignee(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getListByAssignee(params);
        }catch(Exception e){
            ERROR = "ReadingGroupService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("ReadingGroupService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service{
        
        public List<Map> getListByAssignee(Object params);
        
    }
    
}
