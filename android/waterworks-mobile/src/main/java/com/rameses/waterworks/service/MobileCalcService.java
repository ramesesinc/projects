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
public class MobileCalcService {
    
    private Service service;
    public String ERROR;
    
    public MobileCalcService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksMobileCalcService", Service.class);
        }catch(Exception e){
            ERROR = "MobileCalcService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileCalcService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getFormula(){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getFormula();
        }catch(Exception e){
            ERROR = "MobileCalcService: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileCalcService",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service{
        
        public List<Map> getFormula();
        
    }
    
}
