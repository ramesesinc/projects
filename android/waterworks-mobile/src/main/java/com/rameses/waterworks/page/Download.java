package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Sector;
import com.rameses.waterworks.bean.SectorReader;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.MobileRuleService;
import com.rameses.waterworks.service.MobileDownloadService;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Download {
    
    VBox root;
    ListView<Sector> listView;
    Label label;
    ProgressBar progressbar;
    Button download;
    List<Map> indexlist;
    List<Map> accountList;
    int downloadsize;
    int indexposition = 1;
    Task<Void> task;
    boolean continueDownload = true;
    int counter = 0;
    String batchid;
    String status = "";
    List<String> downloadedList;
    
    public Download(){
        Header.TITLE.setText("Download");
        
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
        download.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        Dialog.wait("Processing, please wait...");
                    }
                });
               
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        //GET THE SELECTED AREAS FOR DOWNLOAD
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

                        MobileDownloadService mobileSvc = new MobileDownloadService(); 
                        //INIT DOWNLOAD
                        Map params = new HashMap();
                        params.put("assigneeid", SystemPlatformFactory.getPlatform().getSystem().getUserID());
                        params.put("sectorid", sectorid);
                        batchid = mobileSvc.initForDownload(params);
                        
                        int recordcount = -1;
                        while (true) {
                            int stat = mobileSvc.getBatchStatus(batchid); 
                            if ( stat < 0 ) {
                                try {  
                                    Thread.sleep(2000); 
                                }catch(Throwable t){;} 
                            } else {
                                recordcount = stat; 
                                break; 
                            }
                        }
                        
                        if ( recordcount <= 0 ) {
                            Dialog.hide();
                            Dialog.showError("No data to download");
                            return;
                        }
                        
                        downloadsize = recordcount;
                        accountList = new ArrayList();
                        int start=0, limit=50;
                        while ( start < recordcount ) {
                            params = new HashMap();
                            params.put("batchid", batchid);
                            params.put("_start", start);
                            params.put("_limit", limit); 
                            List<Map> list = mobileSvc.download(params);
                            //if ( list != null ) accountList.addAll( list ); 
                            System.out.println("fetch results is " + list.size());
                            //new Thread( new ProcessDownloadResultTask(start,list)).start(); 
                            start += limit;                             
                        }
                        
                        Dialog.hide();

                        //SAVE AREA, STUBOUTS
                        clearSector();
                        for(Sector r : listView.getItems()){
                            if(r.isSelected()){
                                saveSector(r);
                            }
                        }

                        label.setVisible(true);
                        progressbar.setVisible(true);
                        progressbar.progressProperty().bind(task.progressProperty());
                        new Thread(task).start();
                        download.setText("Cancel");
                        download.setDisable(false);
                        download.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                continueDownload = false;
                                label.setVisible(false);
                                progressbar.setVisible(false);
                                download.setText("Back");
                                download.setOnAction(new EventHandler<ActionEvent>(){
                                    @Override
                                    public void handle(ActionEvent event) {
                                        Main.ROOT.setCenter(new Home().getLayout());
                                    }
                                });
                                root.setOnKeyReleased(new EventHandler<KeyEvent>(){
                                    @Override
                                    public void handle(KeyEvent event) {
                                        if(event.getCode() == KeyCode.ESCAPE){
                                            if(Dialog.isOpen){ Dialog.hide(); return; }
                                            Main.ROOT.setCenter(new Home().getLayout());
                                        }
                                    }
                                });
                                Map params = new HashMap();
                                params.put("batchid", batchid);
                                params.put("downloadedlist", downloadedList);
                                MobileDownloadService svc = new MobileDownloadService();
                                svc.cancelDownload(params);
                            }
                        });
                        download.setDisable(false);
                            }
                });
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
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        
        loadData();
        
        task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                downloadedList = new ArrayList<String>();
                counter = 0;
                Iterator<Map> it = accountList.iterator();
                while(it.hasNext()){
                    if(!continueDownload){
                        return null;
                    }
                    Map account = it.next();
                    account.put("batchid", batchid);
                    String objid = account.get("objid") != null ? account.get("objid").toString() : "";
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    Account result = db.findAccountByID(objid);
                    if(result == null){
                        db.createAccount(account);
                        if(db.getError().isEmpty()){
                            ++counter;
                            downloadedList.add(objid);
                        }
                    }else{
                        db.updateAccount(account);
                        if(db.getError().isEmpty()){
                            ++counter;
                            downloadedList.add(objid);
                        }
                    }
                    double percent = indexposition/downloadsize;
                    updateProgress(indexposition,downloadsize);
                    indexposition++;
                }
                
                //download water-rates
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
                
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                label.setText("Download Complete!    " + counter + " / " +0);
                                download.setDisable(false);
                                download.setText("Done");
                                download.setOnAction(new EventHandler<ActionEvent>(){
                                    @Override
                                    public void handle(ActionEvent event) {
                                        Main.ROOT.setCenter(new Home().getLayout());
                                    }
                                });
                                root.setOnKeyReleased(new EventHandler<KeyEvent>(){
                                    @Override
                                    public void handle(KeyEvent event) {
                                        if(event.getCode() == KeyCode.ESCAPE){
                                            if(Dialog.isOpen){ Dialog.hide(); return; }
                                            Main.ROOT.setCenter(new Home().getLayout());
                                        }
                                    }
                                });
                            }
                        });
                    }
                };
                t.start();
                
                return null;
            }
        };
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
    
    public Node getLayout(){
        return root;
    }
    
}
