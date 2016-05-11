package com.rameses.waterworks.task;

import com.rameses.Main;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.page.StuboutList;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ZoneTask extends Task<Void>{
    
    private Zone zone;
    
    public ZoneTask(Zone zone){
        this.zone = zone;
    }

    @Override
    protected Void call() throws Exception {
        try{ Thread.sleep(200); }catch(Throwable t){ System.err.println(t); }
        if(zone != null){
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    Main.ROOT.setCenter(new StuboutList(zone).getLayout());
                }
            });
        }
        return null;
    }
    
}
