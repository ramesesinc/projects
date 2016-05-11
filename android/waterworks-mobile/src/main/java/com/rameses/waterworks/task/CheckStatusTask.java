package com.rameses.waterworks.task;

import com.rameses.waterworks.service.MobileDownloadService;
import javafx.concurrent.Task;

public class CheckStatusTask extends Task<Integer> {
    
    private String batchid;
    private MobileDownloadService mobileSvc;
    
    public CheckStatusTask(String batchid){
        this.batchid = batchid;
        mobileSvc = new MobileDownloadService();
    }

    @Override
    protected Integer call() throws Exception {
        int recordcount = -1;
        while (true) {
            if(isCancelled()) break;
            
            int stat = mobileSvc.getBatchStatus(batchid); 
            if (!mobileSvc.ERROR.isEmpty() && !isCancelled()) { 
                updateMessage(mobileSvc.ERROR);
                break;
            }   
            
            if ( stat < 0 ) {
                try {  
                    Thread.sleep(2000); 
                }catch(Throwable t){;} 
            } else {
                recordcount = stat; 
                break; 
            }
        }
        return recordcount;
    }
    
}
