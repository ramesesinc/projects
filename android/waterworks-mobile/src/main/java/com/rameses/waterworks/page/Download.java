package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Area;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.AreaService;
import com.rameses.waterworks.service.DownloadService;
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
    int downloadsize;
    int indexposition = 1;
    Task<Void> task;
    
    public Download(){
        Header.TITLE.setText("Download");
        
        Callback<Area,ObservableValue<Boolean>> property = new Callback<Area,ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(Area a) {
                return a.selected;
            }
        };
        
        Callback<ListView<Area>, ListCell<Area>> forListView = CheckBoxListCell.forListView(property);
        
        Map params = new HashMap();
        params.put("userid", SystemPlatformFactory.getPlatform().getSystem().getUserID());
        
        AreaService areaSvc = new AreaService();
        List<Map> result = areaSvc.getMyAssignedAreas(params);
        if(!areaSvc.ERROR.isEmpty()){
            Dialog.showError(areaSvc.ERROR);
        }
        
        ObservableList<Area> data = FXCollections.observableArrayList();
        for(Map m : result){
            String objid = m.get("objid").toString();
            String name = m.get("name").toString();
            String description = m.get("description").toString();
            data.add(new Area(objid,name,description,true));
        }
        
        listView = new ListView<Area>();
        listView.setStyle("-fx-font-size: 25px;");
        listView.setPrefHeight(Main.HEIGHT*0.5);
        listView.setCellFactory(forListView);
        listView.setItems(data);
        
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
                List areas = new ArrayList();
                for(Area a : listView.getItems()){
                    Map map = new HashMap();
                    map.put("objid", a.getObjid());
                    if(a.isSelected()){
                        areas.add(map);
                        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                        if(!db.downloadableArea(a.getObjid())){
                            Dialog.showAlert(a.getName() + " has already been downloaded. You must upload the data first.");
                            return;
                        }
                    }
                }
                DownloadService downSvc = new DownloadService();
                indexlist = downSvc.getDownloadIndexes(areas);
                Main.LOG.debug("INDEXLIST", indexlist.toString());
                downloadsize = indexlist.size();
                if(!downSvc.ERROR.isEmpty()) {
                    Dialog.showError(downSvc.ERROR);
                    return;
                }
                if(downloadsize < 1){
                    Dialog.showError("No data to download");
                    return;
                }
                
                label.setVisible(true);
                progressbar.setVisible(true);
                download.setDisable(true);
                progressbar.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
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
        
        task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                DownloadService downSvc = new DownloadService();
                Iterator<Map> it = indexlist.iterator();
                while(it.hasNext()){
                    Map map = it.next();
                    String objid = map.get("objid") != null ? map.get("objid").toString() : "";
                    Map account = downSvc.getAccount(map);
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    Account result = db.findAccountByID(objid);
                    if(result == null){
                        db.createAccount(account);
                    }else{
                        db.updateAccount(account);
                    }
                    double percent = indexposition/downloadsize;
                    updateProgress(indexposition,downloadsize);
                    indexposition++;
                }
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                label.setText("Download Complete!");
                                download.setDisable(false);
                                download.setText("Done");
                                download.setOnAction(new EventHandler<ActionEvent>(){
                                    @Override
                                    public void handle(ActionEvent event) {
                                        Main.ROOT.setCenter(new Home().getLayout());
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
    
    public Node getLayout(){
        return root;
    }
    
}
