package com.rameses.waterworks.service;

import com.rameses.Main;
import com.rameses.service.ScriptServiceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public String initForDownload(Object params){
        try{
            return (String) service.initForDownload(params);
        } catch(RuntimeException re){
            ERROR = "MobileDownloadService Error: " + re.toString();
            throw re; 
        } catch(Throwable e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            throw new RuntimeException(e.getMessage(), e);
        } 
    }
    
    public int getBatchStatus(String batchid){
        ERROR = "";
        try{
            return (int) service.getBatchStatus(batchid);
        }catch(Exception e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            if(Main.LOG != null){
                Main.LOG.error("MobileDownloadService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    public List<Map> download(Object params){
        try{
            return service.download(params);
        } catch(RuntimeException re){
           throw re; 
        } catch(Throwable e){
            ERROR = "MobileDownloadService Error: " + e.toString();
            throw new RuntimeException(e.getMessage(), e);
        } 
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
        
        public String initForDownload(Object params);
        
        public int getBatchStatus(String batchid);
        
        public List<Map> download(Object params);
        
        public String confirmDownload(Object params);
        
        public void cancelDownload(Object params);
        
        public List<Map> getSectorByUser(Object params);
        
        public List<Map> getStuboutsBySector(Object params);
        
        public List<Map> getReaderBySector(Object params);
        
        public List<Map> getZoneBySector(Object params);
        
    }
    
}
