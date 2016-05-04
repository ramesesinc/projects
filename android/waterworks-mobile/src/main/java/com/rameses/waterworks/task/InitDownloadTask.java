package com.rameses.waterworks.task;

import com.rameses.waterworks.service.MobileDownloadService;
import java.util.Map;
import javafx.concurrent.Task;

public class InitDownloadTask extends Task<String> {
    
    private Map params;
    private MobileDownloadService mobileSvc;
    
    public InitDownloadTask(Map params){
        this.params = params;
        mobileSvc = new MobileDownloadService();
    }

    @Override
    protected String call() throws Exception {
        String batchid = mobileSvc.initForDownload( params );
        if(!mobileSvc.ERROR.isEmpty()) updateMessage("init download failed caused by " + mobileSvc.ERROR);
        return batchid;
    }
    
}
