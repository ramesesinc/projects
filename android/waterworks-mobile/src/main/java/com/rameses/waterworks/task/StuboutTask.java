package com.rameses.waterworks.task;

import com.rameses.Main;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.page.StuboutAccountList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;

public class StuboutTask extends Task<Void> {
    
    private Stubout stubout;
    private Zone zone;
    
    public StuboutTask(Stubout stubout,Zone zone){
        this.stubout = stubout;
        this.zone = zone;
    }

    @Override
    protected Void call() throws Exception {
        try{ Thread.sleep(200); }catch(Throwable t){ System.err.println(t); }
        if(stubout != null){
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    Node child = new StuboutAccountList(stubout,zone).getLayout();
                    Main.ROOT.setCenter(child);
                }
            });
        }
        return null;
    }
    
}
