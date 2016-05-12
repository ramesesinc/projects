package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.DownloadStat;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Sector;
import com.rameses.waterworks.bean.SectorReader;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.database.DBContext;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.MobileRuleService;
import com.rameses.waterworks.service.MobileDownloadService;
import com.rameses.waterworks.util.SystemPlatformFactory;
import com.rameses.waterworks.task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Download {
    
    VBox root;
    ListView<Sector> listView;
    Label label;
    ProgressBar progressbar;
    Button download;
    List<Map> accountList;
    int downloadsize;
    int recordcount;
    String batchid;
    String status = "";
    
    Database db;
    boolean has_stat_record;
    boolean download_started = false;
    
    InitDownloadTask initDownload;
    CheckStatusTask checkStatus;
    ProcessDownloadTask processDownload;
    
    public Download() { 
        Header.TITLE.setText("Download");
 
        db = DatabasePlatformFactory.getPlatform().getDatabase();
        
        Callback<Sector,ObservableValue<Boolean>> property = new Callback<Sector,ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(Sector a) {
                return a.selected;
            }
        };
        
        Callback<ListView<Sector>, ListCell<Sector>> forListView = CheckBoxListCell.forListView(property);
        
        listView = new ListView<Sector>();
        listView.setId("download-listview");
        listView.setPrefHeight(Main.HEIGHT*0.50);
        listView.setCellFactory(forListView);
        
        label = new Label("Downloading... Please wait...");
        label.setId("download-status");
        label.setVisible(false);
        
        progressbar = new ProgressBar();
        progressbar.setStyle("-fx-accent: #5cb1e1;");
        progressbar.setPrefWidth(Main.WIDTH);
        progressbar.setPrefHeight(20);
        progressbar.setVisible(false);
        
        download = new Button("Download");
        download.getStyleClass().add("terminal-button");
        download.setPrefWidth(Main.HEIGHT > 700 ? 180 : 140);
        download.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!Dialog.isOpen){
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            Dialog.show("", showProgress());
                        }
                    });
                }
            }
        });
        download.setOnMouseReleased(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        doDownload();
                    }
                });
            }
        });
        download.setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                if(!Dialog.isOpen){
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            Dialog.show("", showProgress());
                        }
                    });
                }
            }
        });
        
        HBox bcontainer = new HBox();
        bcontainer.setAlignment(Pos.CENTER_RIGHT);
        bcontainer.setPadding(new Insets(10, 0, 0, 0));
        bcontainer.getChildren().add(download);
        
        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(20) : new Insets(10));
        root.getChildren().addAll(listView,bcontainer,label,progressbar);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(download_started){
                        if(checkStatus.isRunning()) checkStatus.stop();
                        if(processDownload.isRunning()) processDownload.stop();
                    }
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        
        loadData();
    }
    
    DBContext createDBContext() {
        return DatabasePlatformFactory.getPlatform().getDatabase().createDBContext(); 
    }
    
    private void finishDownload(){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if(Dialog.isOpen) Dialog.hide();
                label.setText("Download Complete! " + downloadsize + " records was downloaded!");
                download.setText("Done");
                download.setOnMousePressed(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        Main.ROOT.setCenter(new Home().getLayout());
                    }
                });
                download.setOnMouseReleased(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        Main.ROOT.setCenter(new Home().getLayout());
                    }
                });
                download.setOnTouchPressed(new EventHandler<TouchEvent>(){
                    @Override
                    public void handle(TouchEvent event) {
                        Main.ROOT.setCenter(new Home().getLayout());
                    }
                });
            }
        });
    }
    
    private void doDownload(){
        List<String> selectedSectors = new ArrayList();
        String sectorid = null;
        for(Sector r : listView.getItems()){
            if(r.isSelected()){
                selectedSectors.add(r.getObjid());
                sectorid = r.getObjid();
            }
        }

        if(selectedSectors.size() > 1){
            Dialog.hide();
            Dialog.showAlert("Multiple downloads are not supported!");
            return;
        }

        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        DownloadStat stat = new DownloadStat().findByUserid( userid );
        batchid = stat.getBatchid(); 
        
        Map params = new HashMap();
        params.put("assigneeid", userid);
        params.put("sectorid", sectorid); 
        has_stat_record = false; 
        if ( batchid != null ) {
            has_stat_record = true; 
            if ( stat.getIndexno() >= stat.getRecordcount() ) {
                stat.delete(); 
                has_stat_record = false; 
            } else {
                params.put("batchid", batchid); 
            }
        }
        
        download_started = true;
        initDownload = new InitDownloadTask(params);
        initDownload.stateProperty().addListener(new ChangeListener<State>(){
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State state) {
                System.out.println("InitDownloadTask is now on " + state + " state.");
                if(state.equals(State.SUCCEEDED)){
                    String error = initDownload.getMessage();
                    System.out.println("ERROR : " + error);
                    if(error.isEmpty()){
                        batchid = initDownload.getValue();
                        if ( !has_stat_record ) {
                            stat.setBatchid( batchid );
                            stat.setAssigneeid(userid );
                            stat.setRecordcount(0);
                            stat.setIndexno(0);
                            stat.create(); 
                        }
                        new Thread(checkStatus).start();
                    }else{
                        Dialog.showAlert(error);
                    }
                }
            }
        });
        new Thread(initDownload).start(); 
        
        checkStatus = new CheckStatusTask(batchid);
        checkStatus.stateProperty().addListener(new ChangeListener<State>(){
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State state) {
                System.out.println("CheckStatusTask is now on " + state + " state.");
                if(state.equals(State.SUCCEEDED)){
                    String error = checkStatus.getMessage();
                    System.out.println("ERROR : " + error);
                    if(error.isEmpty()){
                        recordcount = checkStatus.getValue();
                        if ( recordcount <= 0 ) {
                            Dialog.hide();
                            Dialog.showError("No data to download");
                            return;
                        }
                        stat.setRecordcount(recordcount);
                        stat.update();
                        new Thread(processDownload).start();
                    }else{
                        Dialog.showAlert(error);
                    }
                }
            }
        });
        
        processDownload = new ProcessDownloadTask(batchid, stat.getIndexno(), recordcount);
        processDownload.stateProperty().addListener(new ChangeListener<State>(){
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State state) {
                System.out.println("ProcessDownloadTask is now on " + state + " state.");
                if(state.equals(State.SUCCEEDED)){
                    String error = processDownload.getMessage();
                    System.out.println("ERROR : " + error);
                    if(error.isEmpty()){
                        clearSector();
                        for(Sector r : listView.getItems()){
                            if(r.isSelected()){
                                saveSector(r);
                            }
                        }
                        downloadRules();
                        finishDownload();
                    }else{
                        Dialog.showAlert(error);
                    }
                }
            }
        });
        
    }
    
    private void loadData(){ 
        Thread t2 = new Thread(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
                        Map params = new HashMap();
                        params.put("userid", userid);

                        MobileDownloadService mobileSvc = new MobileDownloadService();
                        List<Map> result = mobileSvc.getSectorByUser(params);

                        ObservableList<Sector> data = FXCollections.observableArrayList();
                        for(Map m : result){
                            String objid = m.get("objid") != null ? m.get("objid").toString() : "";
                            String code = m.get("code") != null ? m.get("code").toString() : "";
                            data.add(new Sector(objid, code, false, userid));
                        }
                        listView.setItems(data);
                        
                        Dialog.hide();
                        if(!mobileSvc.ERROR.isEmpty()){
                            Dialog.showError(mobileSvc.ERROR);
                        }
                    }
                });
            }
        };
        t2.start();
    }
    
    private void downloadRules(){
        MobileRuleService service = new MobileRuleService();
        List<Map> list = service.getRules();
        if(!service.ERROR.isEmpty()){
            Dialog.showError(service.ERROR);
        }
        if(!list.isEmpty()){
            Database db = DatabasePlatformFactory.getPlatform().getDatabase();
            db.clearRule();
        }
        for(Map m : list){
            int salience = m.get("salience") != null ? Integer.parseInt(m.get("salience").toString()) : 0;
            String condition = m.get("condition") != null ? m.get("condition").toString() : "";
            String var= m.get("var") != null ? m.get("var").toString() : "";
            String action= m.get("action") != null ? m.get("action").toString() : "";

            Rule rule = new Rule(salience, condition, var, action);
            Database db = DatabasePlatformFactory.getPlatform().getDatabase();
            db.createRule(rule);
        }
        Main.loadRules();
    }
    
    private void saveSector(Sector s){
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        DatabasePlatformFactory.getPlatform().getDatabase().createSector(s);
        
        Map params = new HashMap();
        params.put("sectorid", s.getObjid());
        params.put("userid", userid);
        
        MobileDownloadService downloadSvc = new MobileDownloadService();
        List<Map> readers = downloadSvc.getReaderBySector(params);
        List<Map> zones = downloadSvc.getZoneBySector(params);
        List<Map> stubouts = downloadSvc.getStuboutsBySector(params);
        Iterator<Map> i = null;
        
        //create sector readers
        i = readers.iterator();
        while(i.hasNext()){
            Map m = i.next();
            String objid = m.get("objid") != null ? m.get("objid").toString() : "";
            String sectorid = m.get("sectorid") != null ? m.get("sectorid").toString() : "";
            String title = m.get("title") != null ? m.get("title").toString() : "";
            Map assignee = m.get("assignee") != null ? (Map) m.get("assignee") : new HashMap();
            String assigneeid = assignee.get("objid") != null ? assignee.get("objid").toString() : "";
            String assigneename = assignee.get("name") != null ? assignee.get("name").toString() : "";
            SectorReader sr = new SectorReader(objid, sectorid, title, assigneeid, assigneename);
            DatabasePlatformFactory.getPlatform().getDatabase().createSectorReader(sr);
        }
        
        //create zones
        i = zones.iterator();
        while(i.hasNext()){
            Map m = i.next();
            String objid = m.get("objid") != null ? m.get("objid").toString() : "";
            String sectorid = m.get("sectorid") != null ? m.get("sectorid").toString() : "";
            String code = m.get("code") != null ? m.get("code").toString() : "";
            String description = m.get("description") != null ? m.get("description").toString() : "";
            String readerid = m.get("readerid") != null ? m.get("readerid").toString() : "";
            Zone zone = new Zone(objid, sectorid, code, description, readerid, userid);
            DatabasePlatformFactory.getPlatform().getDatabase().createZone(zone);
        }
        
        //create stubouts
        i = stubouts.iterator();
        while(i.hasNext()){
            Map m = i.next();
            String objid = m.get("objid") != null ? m.get("objid").toString() : "";
            String code = m.get("code") != null ? m.get("code").toString() : "";
            String description = m.get("description") != null ? m.get("description").toString() : "";
            String zoneid = m.get("zoneid") != null ? m.get("zoneid").toString() : "";
            Map barangay = m.get("barangay") != null ? (Map) m.get("barangay") : new HashMap();
            String barangayid = barangay.get("objid") != null ? barangay.get("objid").toString() : "";
            String barangayname = barangay.get("name") != null ? barangay.get("name").toString() : "";
            Stubout stubout = new Stubout(objid, code, description, zoneid, barangayid, barangayname, userid);
            DatabasePlatformFactory.getPlatform().getDatabase().createStubout(stubout);
        }
    }
    
    private void clearSector(){
        DatabasePlatformFactory.getPlatform().getDatabase().clearStubout();
        DatabasePlatformFactory.getPlatform().getDatabase().clearZone();
        DatabasePlatformFactory.getPlatform().getDatabase().clearSectorReader();
        DatabasePlatformFactory.getPlatform().getDatabase().clearSector();
    }
    
    private Node showProgress(){
        ProgressIndicator indicator = new ProgressIndicator();
        
        Label text = new Label("Processing...  Please wait...");
        text.getStyleClass().add("login-label");
        
        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(15));
        root.setPrefWidth(Main.WIDTH - 100);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(indicator, text);
        
        return root;
    }
    
    public Node getLayout(){
        return root;
    }
    
}
