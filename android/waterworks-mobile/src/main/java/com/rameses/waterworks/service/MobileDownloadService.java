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
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
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
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
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
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
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
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
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
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getSectorByUser(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getSectorByUser(params);
        }catch(Exception e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public List<Map> getStuboutsBySector(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getStuboutsBySector(params);
        }catch(Exception e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public List<Map> getReaderBySector(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getReaderBySector(params);
        }catch(Exception e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public List<Map> getZoneBySector(Object params){
        ERROR = "";
        List result = new ArrayList();
        try{
            result = service.getZoneBySector(params);
        }catch(Exception e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
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
        
        public List<Map> getSectorByUser(Object params);
        
        public List<Map> getStuboutsBySector(Object params);
        
        public List<Map> getReaderBySector(Object params);
        
        public List<Map> getZoneBySector(Object params);
        
    }
    
}
