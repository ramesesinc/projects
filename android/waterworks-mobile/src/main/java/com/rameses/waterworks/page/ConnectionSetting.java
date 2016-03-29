package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ConnectionSetting {
    
    private VBox root;
    private Label notice;
    private TextField ip,port,timeout,context,cluster;
    private Button save;
    
    public ConnectionSetting(){
        Header.TITLE.setText("Connection Setting");
        
        String setting_ip = Main.CONNECTION_SETTING.get("ip") != null ? Main.CONNECTION_SETTING.get("ip").toString() : "127.0.0.1";
        String setting_port = Main.CONNECTION_SETTING.get("port") != null ? Main.CONNECTION_SETTING.get("port").toString() : "8070";
        String setting_timeout = Main.CONNECTION_SETTING.get("timeout") != null ? Main.CONNECTION_SETTING.get("timeout").toString() : "30000";
        String setting_context = Main.CONNECTION_SETTING.get("context") != null ? Main.CONNECTION_SETTING.get("context").toString() : "etracs25";
        String setting_cluster = Main.CONNECTION_SETTING.get("cluster") != null ? Main.CONNECTION_SETTING.get("cluster").toString() : "osiris3";
        
        notice = new Label("The information below determines your device's connectivity to the server. Make sure that the entries are correct.");
        notice.setWrapText(true);
        notice.getStyleClass().add("terminal-notice");
        
        Label ip_label = new Label("IP Address");
        ip_label.getStyleClass().add("terminal-label");
        
        Label port_label = new Label("Port Number");
        port_label.getStyleClass().add("terminal-label");
        
        Label timeout_label = new Label("Read Timeout");
        timeout_label.getStyleClass().add("terminal-label");
        
        Label context_label = new Label("Context");
        context_label.getStyleClass().add("terminal-label");
        
        Label cluster_label = new Label("Cluster");
        cluster_label.getStyleClass().add("terminal-label");
        
        ip = new TextField(setting_ip);
        ip.getStyleClass().add("terminal-field");
        ip.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        ip.setFocusTraversable(false);
        
        port = new TextField(setting_port);
        port.getStyleClass().add("terminal-field");
        port.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        port.setFocusTraversable(false);
        
        timeout = new TextField(setting_timeout);
        timeout.getStyleClass().add("terminal-field");
        timeout.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        timeout.setFocusTraversable(false);
        
        context = new TextField(setting_context);
        context.getStyleClass().add("terminal-field");
        context.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        context.setFocusTraversable(false);
        
        cluster = new TextField(setting_cluster);
        cluster.getStyleClass().add("terminal-field");
        cluster.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        cluster.setFocusTraversable(false);
        
        save = new Button("Save");
        save.getStyleClass().add("terminal-button");
        save.setPrefWidth(130);
        save.setFocusTraversable(true);
        save.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(ip.getText().isEmpty() || port.getText().isEmpty() || timeout.getText().isEmpty()) return;
                List<Setting> settings = new ArrayList<Setting>();
                settings.add(new Setting("ip",ip.getText()));
                settings.add(new Setting("port",port.getText()));
                settings.add(new Setting("timeout",timeout.getText()));
                settings.add(new Setting("context",context.getText()));
                settings.add(new Setting("cluster",cluster.getText()));
                Iterator<Setting> it = settings.iterator();
                while(it.hasNext()){
                    Setting s = it.next();
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    if(!db.settingExist(s)){
                        db.createSetting(s);
                    }else{
                        db.updateSetting(s);
                    }
                    if(!db.getError().isEmpty()){
                        Dialog.showError(db.getError());
                        return;
                    }
                }
                Main.CONNECTION_SETTING = new HashMap();
                Main.CONNECTION_SETTING.put("ip", ip.getText());
                Main.CONNECTION_SETTING.put("port", port.getText());
                Main.CONNECTION_SETTING.put("timeout", timeout.getText());
                Main.CONNECTION_SETTING.put("context", context.getText());
                Main.CONNECTION_SETTING.put("cluster", cluster.getText());
                
                Header.TITLE.setText(Main.prevTitle);
                Main.ROOT.setCenter(Main.prevScreen);
            }
        });
        
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.getChildren().add(save);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(Main.HEIGHT > 700 ? 10 : 5);
        grid.setHgap(Main.HEIGHT > 700 ? 20 : 10); 
        
        grid.add(ip_label, 0, 0);
        grid.add(port_label, 0, 1);
        grid.add(context_label, 0, 2);
        grid.add(cluster_label, 0, 3);
        grid.add(timeout_label, 0, 4);
        
        grid.add(ip, 1, 0);
        grid.add(port, 1, 1);
        grid.add(context, 1, 2);
        grid.add(cluster, 1, 3);
        grid.add(timeout, 1, 4);
        
        grid.add(buttonContainer, 1, 6);
        
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(Main.HEIGHT > 700 ? 30 : 15);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(50) : new Insets(20));
        root.getChildren().addAll(notice,grid);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(Main.prevScreen);
                    Header.TITLE.setText(Main.prevTitle);
                }
            }
        });
    }
    
    public Node getLayout(){
        return root;
    }
    
}
