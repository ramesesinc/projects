package com.rameses.waterworks.task;

import com.rameses.waterworks.bean.DownloadStat;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.service.MobileDownloadService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Task;

public class ProcessDownloadTask extends Task<Void> {
    
    private String batchid;
    private int indexno, recordcount;
    private MobileDownloadService mobileSvc;
    private Database db;
    private boolean cancelled = false;
    
    public ProcessDownloadTask(String batchid, int indexno, int recordcount){
        this.batchid = batchid;
        this.indexno = indexno;
        this.recordcount = recordcount;
        mobileSvc = new MobileDownloadService();
        db = DatabasePlatformFactory.getPlatform().getDatabase();
    }

    @Override
    protected Void call() throws Exception {
        Map params = new HashMap();
        int limit=50, start=(indexno < 0 ? 0 : indexno);  
        DownloadStat stat = new DownloadStat().findByPrimary(batchid);
        while ( start < recordcount ) {
            if(cancelled) break;
            params.put("batchid", batchid);
            params.put("_start", start);
            params.put("_limit", limit); 
            System.out.println("Start: " + start + " Limit: " + limit);
            List<Map> list = mobileSvc.download(params);
            if(!mobileSvc.ERROR.isEmpty()) updateMessage("Process download failed caused by " + mobileSvc.ERROR);
            if( list != null) { 
                for (int i=0; i<list.size(); i++) {
                    Map data = list.get(i);
                    db.createAccount(data);
                    
                    stat = stat.findByPrimary(batchid);
                    stat.setIndexno( start + i ); 
                    System.out.println("update indexno to " + stat.getIndexno()); 
                    stat.update();
                }
            } 
            start += limit; 
        } 
        
        if ( start >= recordcount ) { 
            stat.findByPrimary(batchid).delete(); 
        }
        return null;
    }
    
    public void stop(){
        cancelled = true;
    }
    
}
