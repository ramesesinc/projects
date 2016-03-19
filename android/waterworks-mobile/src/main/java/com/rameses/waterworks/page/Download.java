package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.ReadingGroup;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.StuboutAccount;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.MobileRuleService;
import com.rameses.waterworks.service.MobileService;
import com.rameses.waterworks.service.ReadingGroupService;
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
    ListView<ReadingGroup> listView;
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
    List<String> downloadedList;
    
    public Download(){
        Header.TITLE.setText("Download");
        
        Callback<ReadingGroup,ObservableValue<Boolean>> property = new Callback<ReadingGroup,ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(ReadingGroup a) {
                return a.selected;
            }
        };
        
        Callback<ListView<ReadingGroup>, ListCell<ReadingGroup>> forListView = CheckBoxListCell.forListView(property);
        
        listView = new ListView<ReadingGroup>();
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
                //GET THE SELECTED AREAS FOR DOWNLOAD
                List<String> groupids = new ArrayList();
                for(ReadingGroup r : listView.getItems()){
                    if(r.isSelected()){
                        groupids.add(r.getObjid());
                    }
                }
                
                if(groupids.size() > 1){
                    Dialog.showAlert("Multiple downloads are not supported!");
                    return;
                }
                
                //INIT DOWNLOAD
                Map params = new HashMap();
                params.put("assigneeid", SystemPlatformFactory.getPlatform().getSystem().getUserID());
                params.put("groupids", groupids);
                MobileService s1  = new MobileService();
                Map initialData = s1.initForDownload(params);
                
                batchid = initialData.get("batchid") != null ? initialData.get("batchid").toString() : "";
                initCount = initialData.get("count") != null ? Integer.parseInt(initialData.get("count").toString()) : 0;
                
                //START DOWNLOAD
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
                
                //STORE THE READING GROUPS, STUBOUTS, STUBOUT_ACCOUNTS
                clearReadingGroups();
                for(ReadingGroup r : listView.getItems()){
                    if(r.isSelected()){
                        saveReadingGroup(r);
                    }
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
                        Map params = new HashMap();
                        params.put("batchid", batchid);
                        params.put("downloadedlist", downloadedList);
                        Main.LOG.error("DOWNLOADED LIST", downloadedList.toString());
                        MobileService svc = new MobileService();
                        svc.cancelDownload(params);
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

                if(counter == initCount){
                    Map params = new HashMap();
                    params.put("batchid", batchid);
                    MobileService svc = new MobileService();
                    status = svc.confirmDownload(params);
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
                        params.put("assigneeid", SystemPlatformFactory.getPlatform().getSystem().getUserID());

                        ReadingGroupService readingGroupSvc = new ReadingGroupService();
                        List<Map> result = readingGroupSvc.getListByAssignee(params);

                        ObservableList<ReadingGroup> data = FXCollections.observableArrayList();
                        for(Map m : result){
                            String objid = m.get("objid").toString();
                            String title = m.get("title").toString();
                            Map assignee = (Map) m.get("assignee");
                            String assigneeid = assignee.get("objid").toString();
                            String assigneename = assignee.get("name").toString();
                            String duedate = m.get("duedate").toString();
                            Object stubout = m.get("stubouts");
                            data.add(new ReadingGroup(objid,title,assigneeid,duedate,false,stubout));
                        }
                        listView.setItems(data);
                        
                        Dialog.hide();
                        if(!readingGroupSvc.ERROR.isEmpty()){
                            Dialog.showError(readingGroupSvc.ERROR);
                        }
                    }
                });
            }
        };
        t2.start();
    }
    
    private void saveReadingGroup(ReadingGroup r){
        DatabasePlatformFactory.getPlatform().getDatabase().createReadingGroup(r);
        List<Map> stubouts = (List<Map>) r.getStubout();
        Iterator<Map> i = stubouts.iterator();
        while(i.hasNext()){
            Map m = i.next();
            String objid = m.get("objid")!=null ? m.get("objid").toString() : "";
            String title = m.get("title")!=null ? m.get("title").toString() : "";
            String description = m.get("description")!=null ? m.get("description").toString() : "";
            Map reading = (Map) m.get("readinggroup");
            String readinggroupid = reading!=null ? reading.get("objid").toString() : "";
            List<Map> accounts = m.get("accounts")!=null ? (List<Map>)m.get("accounts") : new ArrayList();
            
            Stubout stubout = new Stubout(objid,title,description,readinggroupid,accounts);
            DatabasePlatformFactory.getPlatform().getDatabase().createStubout(stubout);
            
            Iterator<Map> it = accounts.iterator();
            while(it.hasNext()){
                Map mm = it.next();
                String acct_objid = mm.get("objid")!=null ? mm.get("objid").toString() : "";
                String acct_parentid = mm.get("parentid")!=null ? mm.get("parentid").toString() : "";
                String acct_acctid = mm.get("acctid")!=null ? mm.get("acctid").toString() : "";
                int acct_sortorder = mm.get("sortorder")!=null ? Integer.parseInt(mm.get("sortorder").toString()) : 0;
                StuboutAccount sa = new StuboutAccount(acct_objid, acct_parentid, acct_acctid, acct_sortorder);
                DatabasePlatformFactory.getPlatform().getDatabase().createStuboutAccount(sa);
            }
        }
    }
    
    private void clearReadingGroups(){
        DatabasePlatformFactory.getPlatform().getDatabase().clearStuboutAccount();
        DatabasePlatformFactory.getPlatform().getDatabase().clearStubout();
        DatabasePlatformFactory.getPlatform().getDatabase().clearReadingGroup();
    }
    
    public Node getLayout(){
        return root;
    }
    
}
