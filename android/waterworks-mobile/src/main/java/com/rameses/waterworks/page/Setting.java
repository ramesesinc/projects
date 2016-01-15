package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.util.SystemPlatformFactory;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dino
 */
public class Setting {
    
    private VBox root;
    
    public Setting(){
        Header.TITLE.setText("System Setting");
        
        ImageView user_img = new ImageView(new Image("icon/user.png"));
        
        HBox img_container = new HBox();
        img_container.setStyle("-fx-border-color: gray; -fx-border-width: 2px;");
        img_container.getChildren().add(user_img);
        
        Label username = new Label(SystemPlatformFactory.getPlatform().getSystem().getUserName());
        username.getStyleClass().add("terminal-label");
        username.setStyle("-fx-font-size: 38px; -fx-underline: true;");
        
        Label fullname = new Label(SystemPlatformFactory.getPlatform().getSystem().getFullName());
        fullname.getStyleClass().add("terminal-label");
        fullname.setStyle("-fx-font-size: 38px;");
        
        Button logout = new Button("Logout");
        logout.getStyleClass().add("terminal-button");
        logout.setStyle("-fx-font-size: 35px;");
        logout.setPrefWidth(180);
        logout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Main.MYACCOUNT = null;
                Main.ROOT.setCenter(new Login().getLayout());
            }
        });
        
        VBox vbox1 = new VBox(15);
        vbox1.setPrefWidth(Main.WIDTH);
        vbox1.setAlignment(Pos.TOP_CENTER);
        vbox1.getChildren().addAll(username,fullname,logout);
        
        HBox account_box = new HBox(10);
        account_box.getStyleClass().add("setting-container");
        account_box.setPadding(new Insets(15));
        account_box.getChildren().add(img_container);
        account_box.getChildren().add(vbox1);
        
        ImageView printer_img = new ImageView(new Image("icon/printer.png"));
        
        HBox printer_container = new HBox();
        printer_container.setStyle("-fx-border-color: gray; -fx-border-width: 2px;");
        printer_container.getChildren().add(printer_img);
        
        BluetoothPort bt = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
        List<String> devices = bt.findDevices();

        ListView<String> listview = new ListView<String>();
        listview.setStyle("-fx-font-size: 20px;");
        listview.setPrefWidth(Main.WIDTH * 0.5);
        listview.setPrefHeight(170);
        listview.setFocusTraversable(true);
        listview.setItems(FXCollections.observableArrayList(devices));
        for(String device: devices){
            if(Main.PRINTERNAME.equals(device)){
                listview.getSelectionModel().select(device);
            }
        }
        
        Button set = new Button("Set Printer");
        set.getStyleClass().add("terminal-button");
        set.setStyle("-fx-font-size: 35px; -fx-border-radius: 15px;");
        set.setPrefWidth(240);
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
        
        VBox vbox2 = new VBox(15);
        vbox2.setPrefWidth(Main.WIDTH);
        vbox2.setAlignment(Pos.TOP_CENTER);
        vbox2.getChildren().addAll(listview,set);
        
        HBox printer_box = new HBox(10);
        printer_box.getStyleClass().add("setting-container");
        printer_box.setPadding(new Insets(15));
        printer_box.getChildren().add(printer_container);
        printer_box.getChildren().add(vbox2);
       
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        if(Main.HEIGHT > 800){
            root.setPadding(new Insets(50, 50, 50, 50));
        }else{
            root.setPadding(new Insets(15, 15, 15, 15));
        }
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        root.getChildren().addAll(account_box,printer_box);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
