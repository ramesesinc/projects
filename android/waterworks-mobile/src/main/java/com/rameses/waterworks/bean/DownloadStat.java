package com.rameses.waterworks.bean;

import com.rameses.waterworks.database.DBContext;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import java.util.HashMap;
import java.util.Map;

public class DownloadStat {
    
    private final String TABLE_NAME = "download_stat"; 
    
    private String batchid;
    private String assigneeid;
    private int recordcount;
    private int indexno;
    
    public DownloadStat(){
    }
    
    public DownloadStat( String batchid, String assigneeid, int recordcount, int indexno ){ 
        this.batchid = batchid;
        this.assigneeid = assigneeid;
        this.recordcount = recordcount;
        this.indexno = indexno; 
    }
    
    public DownloadStat( Map data ){ 
        if ( data == null || data.isEmpty() ) return; 
        
        
        this.batchid = (String) data.get("batchid"); 
        this.assigneeid = (String) data.get("assigneeid"); 
        this.recordcount = getInteger(data, "recordcount"); 
        this.indexno = getInteger(data, "indexno"); 
    } 
    
    DBContext createDBContext() {
        return DatabasePlatformFactory.getPlatform().getDatabase().createDBContext(); 
    }
    
    public DownloadStat findByPrimary( String objid ) {
        DBContext ctx = createDBContext(); 
        try {
            Map params = new HashMap();
            params.put("batchid", objid);
            Map map =  ctx.findBy( TABLE_NAME, params );
            return new DownloadStat( map ); 
        } catch(RuntimeException re) {
            throw re; 
        } catch(Throwable t) {
            throw new RuntimeException(t.getMessage(), t);  
        } finally {
            try { ctx.close(); }catch(Throwable t){;} 
        }
    }
    
    public DownloadStat findByUserid( String userid ) {
        DBContext ctx = createDBContext(); 
        try {
            Map params = new HashMap();
            params.put("assigneeid", userid);
            Map map =  ctx.findBy( TABLE_NAME, params );
            return new DownloadStat( map ); 
        } catch(RuntimeException re) {
            throw re; 
        } catch(Throwable t) {
            throw new RuntimeException(t.getMessage(), t);  
        } finally {
            try { ctx.close(); }catch(Throwable t){;} 
        }
    }
    
    public void create() {
        DBContext ctx = createDBContext(); 
        try {
            Map params = new HashMap();
            params.put("batchid", getBatchid());
            params.put("assigneeid", getAssigneeid());
            params.put("recordcount", getRecordcount());
            params.put("indexno", getIndexno());
            ctx.insert( TABLE_NAME, params ); 
        } catch(RuntimeException re) { 
            throw re; 
        } catch(Throwable t) {
            throw new RuntimeException(t.getMessage(), t);  
        } finally {
            try { ctx.close(); }catch(Throwable t){;} 
        }
    }
    
    public void delete() {
        DBContext ctx = createDBContext(); 
        try {
            Map params = new HashMap();
            params.put("batchid", getBatchid());
            ctx.delete( TABLE_NAME, params, "batchid=$P{batchid}");
        } catch(RuntimeException re) { 
            throw re; 
        } catch(Throwable t) {
            throw new RuntimeException(t.getMessage(), t);  
        } finally { 
            try { ctx.close(); }catch(Throwable t){;} 
        } 
    }
    
    public void update() {
        DBContext ctx = createDBContext(); 
        try {
            Map params = new HashMap();
            params.put("batchid", getBatchid());
            params.put("assigneeid", getAssigneeid());
            params.put("recordcount", getRecordcount());
            params.put("indexno", getIndexno());
            ctx.update( TABLE_NAME, params, "batchid=$P{batchid}");
        } catch(RuntimeException re) { 
            throw re; 
        } catch(Throwable t) {
            throw new RuntimeException(t.getMessage(), t);  
        } finally { 
            try { ctx.close(); }catch(Throwable t){;} 
        } 
    } 

    /**
     * @return the batchid
     */
    public String getBatchid() {
        return batchid;
    }

    /**
     * @param batchid the batchid to set
     */
    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    /**
     * @return the assigneeid
     */
    public String getAssigneeid() {
        return assigneeid;
    }

    /**
     * @param assigneeid the assigneeid to set
     */
    public void setAssigneeid(String assigneeid) {
        this.assigneeid = assigneeid;
    }

    /**
     * @return the recordcount
     */
    public int getRecordcount() {
        return recordcount;
    }

    /**
     * @param recordcount the recordcount to set
     */
    public void setRecordcount(int recordcount) {
        this.recordcount = recordcount;
    }

    /**
     * @return the indexno
     */
    public int getIndexno() {
        return indexno;
    }

    /**
     * @param indexno the indexno to set
     */
    public void setIndexno(int indexno) {
        this.indexno = indexno;
    }
    
    private int getInteger(Map data, String name) {
        Object val = (data == null? null: data.get(name)); 
        if (val instanceof Number) {
            return ((Number) val).intValue(); 
        }
        if ( val instanceof String ) {
            return new Integer( val.toString() ); 
        } 
        return 0; 
    }
}
