package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.MobileService;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author Dino
 */
public class Upload {
    
    private VBox root;
    Label label;
    ProgressBar progressbar;
    TextArea textArea;
    List<Reading> indexlist;
    int indexposition = 1;
    int uploadsize;
    Button upload;
    boolean continueUpload = true;
    
    public Upload(){
        Header.TITLE.setText("Upload Data");
        
        root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        
        Text size_text = new Text(String.valueOf(db.getNoOfTotalReadRecords()));
        size_text.setId("size-text");
        
        Text record_text = new Text("RECORDS");
        record_text.setId("record-text");
        
        VBox textContainer = new VBox();
        textContainer.setAlignment(Pos.CENTER);
        textContainer.getChildren().addAll(size_text,record_text,new Separator());
        
        label = new Label("Uploading... Please wait...");
        label.setStyle("-fx-font-size: 28px; -fx-padding: 10 0 0 0;");
        label.setVisible(false);
        
        progressbar = new ProgressBar();
        progressbar.setStyle("-fx-accent: #5cb1e1;");
        progressbar.setPrefWidth(Main.WIDTH);
        progressbar.setPrefHeight(20);
        progressbar.setVisible(false);
        
        Database adb = DatabasePlatformFactory.getPlatform().getDatabase();
        indexlist = adb.getReadingByUser();
        uploadsize = indexlist.size();
        
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                Iterator<Reading> it = indexlist.iterator();
                String error = "";
                while(it.hasNext()){
                    if(!continueUpload){
                        return null;
                    }
                    Reading r = it.next();
                    
                    Map map = new HashMap();
                    map.put("objid", r.getObjid());
                    map.put("acctid", r.getAcctId());
                    map.put("reading", Integer.parseInt(r.getReading()));
                    map.put("dtreading", r.getReadingDate());
                    map.put("userid", SystemPlatformFactory.getPlatform().getSystem().getUserID());
                    map.put("name", SystemPlatformFactory.getPlatform().getSystem().getFullName());
                    map.put("amount", r.getAmtDue());
                    map.put("batchid", r.getBatchId());
                    
                    MobileService service = new MobileService();
                    Map result = service.upload(map);
                    if(!service.ERROR.isEmpty()){
                        error = service.ERROR;
                        textArea.setVisible(true);
                        textArea.appendText(error + "\n");
                    }
                    
                    if(!result.isEmpty()){
                        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                        db.deleteReadingByMeter(r.getAcctId());
                        db.deleteAccountById(r.getAcctId());
                    }
                    updateProgress(indexposition,uploadsize);
                    indexposition++;
                }
                if(!error.isEmpty()){
                    Dialog.showError(error);
                    upload.setText("Back");
                    upload.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            Main.ROOT.setCenter(new Home().getLayout());
                        }
                    });
                }
                
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                label.setText("Upload Complete!");
                                upload.setDisable(false);
                                upload.setText("Done");
                                upload.setOnAction(new EventHandler<ActionEvent>(){
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
        
        upload = new Button("Upload");
        upload.getStyleClass().add("terminal-button");
        upload.setStyle("-fx-font-size: 26px;");
        upload.setPrefWidth(180);
        if(uploadsize < 1){
            Dialog.showAlert("No data to upload!");
            upload.setText("Back");
            upload.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            });
        }else{
            upload.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    label.setVisible(true);
                    progressbar.setVisible(true);
                    progressbar.progressProperty().bind(task.progressProperty());
                    new Thread(task).start();
                    upload.setText("Cancel");
                    upload.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            continueUpload = false;
                            label.setVisible(false);
                            progressbar.setVisible(false);
                            textArea.setVisible(false);
                            upload.setText("Back");
                            upload.setOnAction(new EventHandler<ActionEvent>(){
                                @Override
                                public void handle(ActionEvent event) {
                                    Main.ROOT.setCenter(new Home().getLayout());
                                }
                            });
                        }
                    });
                }
            });
        }
        
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(10, 0, 0, 0));
        buttonContainer.getChildren().add(upload);
        
        textArea = new TextArea();
        textArea.getStyleClass().add("login-label");
        textArea.setStyle("-fx-text-fill: red;");
        textArea.setPrefHeight(Main.HEIGHT * 0.5);
        textArea.setPrefWidth(Main.WIDTH);
        textArea.setVisible(false);
        textArea.setEditable(false);
        
        root.getChildren().addAll(textContainer, buttonContainer, label, progressbar, textArea);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
    }
    
    public Node getLayout(){
        return root;
    }
    
}
