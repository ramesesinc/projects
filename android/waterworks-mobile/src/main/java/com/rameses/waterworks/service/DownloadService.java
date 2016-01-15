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
public class DownloadService {
    
    private Service service;
    public String ERROR;
    
    public DownloadService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksDownloadService", Service.class);
        }catch(Exception e){
            ERROR = "DownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("DownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getDownloadIndexes(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getDownloadIndexes(params);
        }catch(Exception e){
            ERROR = "DownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("DownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public Map getAccount(Object params){
        ERROR = "";
        Map result = new HashMap();
        try{
            result = service.getAccount(params);
        }catch(Exception e){
            ERROR = "DownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("DownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static interface Service{
        
        public List<Map> getDownloadIndexes(Object params);
        public Map getAccount(Object params);
        
    }
    
}
