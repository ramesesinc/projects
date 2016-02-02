package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dino
 */
public class Setting {
    
    private VBox root;
    
    public Setting(){
        Header.TITLE.setText("System Setting");
        
        BluetoothPort bt = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
        List<String> devices = bt.findDevices();

        ListView<String> listview = new ListView<String>();
        listview.setStyle("-fx-font-size: 20px;");
        listview.setPrefWidth(Main.WIDTH * 0.5);
        listview.setMinHeight(170);
        listview.setMaxHeight(170);
        listview.setFocusTraversable(true);
        listview.setItems(FXCollections.observableArrayList(devices));
        for(String device: devices){
            if(Main.PRINTERNAME.equals(device)){
                listview.getSelectionModel().select(device);
            }
        }
        
        Button set = new Button("Set Printer");
        set.getStyleClass().add("terminal-button");
        set.setPrefWidth(200);
        set.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Main.PRINTERNAME = listview.getSelectionModel().getSelectedItem();
                Dialog.showAlert("Printer is now set to " + Main.PRINTERNAME + ".");
                com.rameses.waterworks.bean.Setting printersetting = new com.rameses.waterworks.bean.Setting("printer",Main.PRINTERNAME);
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                if(!db.settingExist(printersetting)){
                    db.createSetting(printersetting);
                }else{
                    db.updateSetting(printersetting);
                }
            }
        });
        
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(Main.HEIGHT);
        textArea.setPrefWidth(Main.WIDTH);
        textArea.setText(SystemPlatformFactory.getPlatform().getSystem().getReportData());
        
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        if(Main.HEIGHT > 800) root.setPadding(new Insets(20, 20, 20, 20));
        if(Main.HEIGHT < 800) root.setPadding(new Insets(10, 10, 10, 10));
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        root.getChildren().addAll(createTitle("Printer"), listview, set, createTitle("Report Data"), textArea);
    }
    
    private VBox createTitle(String title){
        Label label = new Label(title);
        label.getStyleClass().add("setting-text");
        
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(label);
        
        return root;
    }
    
    public Node getLayout(){
        return root;
    }
    
}
