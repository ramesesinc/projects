package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.dialog.Dialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class GpsPage {
    
    private VBox root;
    
    public GpsPage(){
        TextField t1 = new TextField();
        t1.getStyleClass().add("login-label");
        
        TextField t2 = new TextField();
        t2.getStyleClass().add("login-label");
        
        Button b = new Button("Get Location");
        b.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                t1.setText(Main.LATITUDE + "");
                t2.setText(Main.LONGITUDE + "");
            }
        });
        
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(t1,t2,b);
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
    
    public Node getLayout(){
        return root;
    }
    
}
