package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Area;
import com.rameses.waterworks.bean.Formula;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.AreaService;
import com.rameses.waterworks.service.MobileCalcService;
import com.rameses.waterworks.service.MobileService;
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

/**
 *
 * @author Dino
 */
public class Download {
    
    VBox root;
    ListView<Area> listView;
    Label label;
    ProgressBar progressbar;
    Button download;
    List<Map> indexlist;
    List<Map> accountList;
    int downloadsize;
    int indexposition = 1;
    Task<Void> task;
    boolean continueDownload = true;
    int initCount = 0;
    int counter = 0;
    String batchid;
    String status = "";
    
    public Download(){
        Header.TITLE.setText("Download");
        
        Callback<Area,ObservableValue<Boolean>> property = new Callback<Area,ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(Area a) {
                return a.selected;
            }
        };
        
        Callback<ListView<Area>, ListCell<Area>> forListView = CheckBoxListCell.forListView(property);
        
        listView = new ListView<Area>();
        listView.setStyle("-fx-font-size: 25px;");
        listView.setPrefHeight(Main.HEIGHT*0.50);
        listView.setCellFactory(forListView);
        
        label = new Label("Downloading... Please wait...");
        label.setStyle("-fx-font-size: 28px; -fx-padding: 25 0 0 0;");
        label.setVisible(false);
        
        progressbar = new ProgressBar();
        progressbar.setStyle("-fx-accent: #5cb1e1;");
        progressbar.setPrefWidth(Main.WIDTH);
        progressbar.setPrefHeight(20);
        progressbar.setVisible(false);
        
        download = new Button("Download");
        download.getStyleClass().add("terminal-button");
        download.setStyle("-fx-font-size: 26px;");
        download.setPrefWidth(180);
        download.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                //GET THE SELECTED AREAS FOR DOWNLOAD
                List<String> areas = new ArrayList();
                for(Area a : listView.getItems()){
                    if(a.isSelected()){
                        areas.add(a.getObjid());
                        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                        if(!db.downloadableArea(a.getObjid())){
                            Dialog.showAlert(a.getName() + " has already been downloaded. You must upload the data first.");
                            return;
                        }
                    }
                }
                
                //INIT DOWNLOAD
                Map params = new HashMap();
                params.put("areaids", areas);
                MobileService s1  = new MobileService();
                Map initialData = s1.initForDownload(params);
                
                batchid = initialData.get("batchid") != null ? initialData.get("batchid").toString() : "";
                initCount = initialData.get("count") != null ? Integer.parseInt(initialData.get("count").toString()) : 0;
                
                String error = "";
                accountList = new ArrayList<Map>();
                Map params2 = new HashMap();
                params2.put("batchid", batchid);
                MobileService mobileSvc = new MobileService();
                List<Map> result = mobileSvc.download(params2);
                if(!mobileSvc.ERROR.isEmpty()) error = mobileSvc.ERROR;
                for(Map account: result){
                    accountList.add(account);
                }
                
                downloadsize = accountList.size();
                if(!error.isEmpty()) {
                    Dialog.showError(error.toString());
                    return;
                }
                if(downloadsize < 1){
                    Dialog.showError("No data to download");
                    return;
                }
                
                label.setVisible(true);
                progressbar.setVisible(true);
                progressbar.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
                download.setText("Cancel");
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
                                        Main.ROOT.setCenter(new Home().getLayout());
                                    }
                                }
                            });
                        }
                    });
            }
        });
        
        HBox bcontainer = new HBox();
        bcontainer.setAlignment(Pos.CENTER_RIGHT);
        bcontainer.setPadding(new Insets(10, 0, 0, 0));
        bcontainer.getChildren().add(download);
        
        root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(listView,bcontainer,label,progressbar);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        
        loadData();
        
        task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
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
                        if(db.getError().isEmpty()) ++counter;
                    }else{
                        db.updateAccount(account);
                        if(db.getError().isEmpty()) ++counter;
                    }
                    double percent = indexposition/downloadsize;
                    updateProgress(indexposition,downloadsize);
                    indexposition++;
                }

                if(counter == initCount){
                    Map params = new HashMap();
                    params.put("batchid", batchid);
                    MobileService svc = new MobileService();
                    status = svc.confirmDownload(params);
                }
                
                //download water-rates
                MobileCalcService service = new MobileCalcService();
                List<Map> list = service.getFormula();
                if(!service.ERROR.isEmpty()){
                    Dialog.showError(service.ERROR);
                }
                if(!list.isEmpty()){
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    db.clearFormula();
                }
                for(Map m : list){
                    String classificationid = m.get("classificationid") != null ? m.get("classificationid").toString() : "";
                    String var = m.get("var") != null ? m.get("var").toString() : "";
                    String expr= m.get("expr") != null ? m.get("expr").toString() : "";
                    
                    Formula f = new Formula(classificationid,var,expr);
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    db.createFormula(f);
                }
                
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                label.setText("Download Complete!    " + counter + " / " +initCount);
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
            public void run(){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        Map params = new HashMap();
                        params.put("userid", SystemPlatformFactory.getPlatform().getSystem().getUserID());

                        AreaService areaSvc = new AreaService();
                        List<Map> result = areaSvc.getListByAssignee(params);

                        ObservableList<Area> data = FXCollections.observableArrayList();
                        for(Map m : result){
                            String objid = m.get("objid").toString();
                            String name = m.get("name").toString();
                            String description = m.get("description").toString();
                            data.add(new Area(objid,name,description,true));
                        }
                        listView.setItems(data);
                        
                        Dialog.hide();
                        if(!areaSvc.ERROR.isEmpty()){
                            Dialog.showError(areaSvc.ERROR);
                        }
                    }
                });
            }
        };
        t2.start();
    }
    
    public Node getLayout(){
        return root;
    }
    
}
