package com.rameses.waterworks.page;

import com.rameses.Main;
import static com.rameses.Main.PRINTER;
import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.printer.OneilPrinterHandler;
import com.rameses.waterworks.printer.ZebraPrinterHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dino
 */
public class Setting {
    
    private VBox root;
    private ListView<String> listview;
    private TextArea textArea;
    String handler = "";
    
    public Setting(){
        Header.TITLE.setText("System Setting");

        listview = new ListView<String>();
        listview.setStyle(Main.HEIGHT > 700 ? "-fx-font-size: 20px;" : "-fx-font-size: 11px;");
        listview.setPrefWidth(Main.WIDTH * 0.5);
        listview.setMaxHeight(Main.HEIGHT > 700 ? 170 : 70);
        listview.setMinHeight(Main.HEIGHT > 700 ? 170 : 70);
        listview.setFocusTraversable(true);
        
        ToggleGroup group = new ToggleGroup();
        
        RadioButton oneil_btn = new RadioButton("Datamax Oneil");
        oneil_btn.setToggleGroup(group);
        oneil_btn.getStyleClass().add("login-label");
        
        RadioButton zebra_btn = new RadioButton("Zebra");
        zebra_btn.setToggleGroup(group);
        zebra_btn.getStyleClass().add("login-label");
        
        if(Main.PRINTERHANDLER != null){
            if(Main.PRINTERHANDLER.getName().equals("ZEBRA")) zebra_btn.setSelected(true);
            if(Main.PRINTERHANDLER.getName().equals("ONEIL")) oneil_btn.setSelected(true);
        }
        
        VBox groupContainer = new VBox(5);
        groupContainer.getChildren().addAll(zebra_btn, oneil_btn);
        
        Button set = new Button("Set Printer");
        set.getStyleClass().add("terminal-button");
        set.setPrefWidth(Main.HEIGHT > 700 ? 200 : 150);
        set.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                set.setDisable(true);
                            }
                        });
                    }
                };
                t.start();
                Main.PRINTERNAME = listview.getSelectionModel().getSelectedItem();
                if(PRINTER != null) PRINTER.closeBT();
                PRINTER = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
                PRINTER.setPrinter(Main.PRINTERNAME);
                PRINTER.openBT();
                
                if(zebra_btn.isSelected()){
                    handler = "ZEBRA";
                    Main.PRINTERHANDLER = new ZebraPrinterHandler();
                }
                if(oneil_btn.isSelected()){
                    handler = "ONEIL";
                    Main.PRINTERHANDLER = new OneilPrinterHandler();
                }
                
                if(handler.isEmpty()){
                    Dialog.showAlert("You must select the handler!");
                    return;
                }
                
                textArea.setText(Main.PRINTERHANDLER.getScriptCode());
                List<com.rameses.waterworks.bean.Setting> settings = new ArrayList<com.rameses.waterworks.bean.Setting>();
                settings.add(new com.rameses.waterworks.bean.Setting("printer",Main.PRINTERNAME));
                settings.add(new com.rameses.waterworks.bean.Setting("handler",handler));
                Iterator<com.rameses.waterworks.bean.Setting> it = settings.iterator();
                while(it.hasNext()){
                    com.rameses.waterworks.bean.Setting s = it.next();
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    if(!db.settingExist(s)){
                        db.createSetting(s);
                    }else{
                        db.updateSetting(s);
                    }
                }
                Dialog.showAlert("Printer is now set to " + Main.PRINTERNAME + ".");
                set.setDisable(false);
            }
        });
        
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(Main.HEIGHT);
        textArea.setPrefWidth(Main.WIDTH);
        if(Main.HEIGHT < 700) textArea.setStyle("-fx-font-size: 11px;");
        if(Main.PRINTERHANDLER != null) textArea.setText(Main.PRINTERHANDLER.getScriptCode());
        
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(20, 20, 20, 20) : new Insets(10, 10, 10, 10));
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        root.getChildren().addAll(createTitle("Printer"), listview, groupContainer, set, createTitle("Report Data"), textArea);
        
        loadPrinterData();
    }
    
    private void loadPrinterData(){
        Thread t = new Thread(){
            @Override
            public void run(){
                BluetoothPort bt = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
                List<String> devices = bt.findDevices();
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        listview.setItems(FXCollections.observableArrayList(devices));
                        for(String device: devices){
                            if(Main.PRINTERNAME.equals(device)){
                                listview.getSelectionModel().select(device);
                            }
                        }
                        Dialog.hide();
                    }
                });
            }
        };
        t.start();
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
