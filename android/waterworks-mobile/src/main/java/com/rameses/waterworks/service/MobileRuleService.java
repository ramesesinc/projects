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
public class MobileRuleService {
    
    private Service service;
    public String ERROR;
    
    public MobileRuleService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksMobileRuleService", Service.class);
        }catch(Exception e){
            ERROR = "WaterworksMobileRuleService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("WaterworksMobileRuleService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getRules(){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getRules();
        }catch(Exception e){
            ERROR = "WaterworksMobileRuleService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("WaterworksMobileRuleService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service{
        public List<Map> getRules();  
    }
    
}
