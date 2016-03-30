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
public class MobileDownloadService {
    
    private Service service;
    public String ERROR;
    
    public MobileDownloadService(){
        ERROR = "";
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("WaterworksMobileDownloadService", Service.class);
        }catch(Exception e){
            ERROR = "MobileService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public Map initForDownload(Object params){
        ERROR = "";
        Map result = new HashMap();
        try{
            result = service.initForDownload(params);
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
    
    public String confirmDownload(Object params){
        ERROR = "";
        String result = "";
        try{
            result = service.confirmDownload(params);
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
    
    public void cancelDownload(Object params){
        ERROR = "";
        try{
            service.cancelDownload(params);
        }catch(Exception e){
            ERROR = "MobileService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileService Error",e.toString());
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
            ERROR = "MobileService Error: " + e.toString();
            result = new HashMap();
            if(Main.LOG != null){
                Main.LOG.error("MobileService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public List<Map> getAreasByUser(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getAreasByUser(params);
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
    
    public List<Map> getStuboutsByArea(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getStuboutsByArea(params);
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
        
        public Map initForDownload(Object params);
        
        public List<Map> download(Object params);
        
        public String confirmDownload(Object params);
        
        public void cancelDownload(Object params);
        
        public Map upload(Object params);
        
        public List<Map> getAreasByUser(Object params);
        
        public List<Map> getStuboutsByArea(Object params);
        
    }
    
}
