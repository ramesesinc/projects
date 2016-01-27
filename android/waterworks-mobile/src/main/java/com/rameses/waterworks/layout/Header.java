package com.rameses.waterworks.layout;

import com.rameses.Main;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.page.ConnectionSetting;
import com.rameses.waterworks.page.UserAccount;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Header {
    
    public static Text TITLE;
    private BorderPane root;
    private static HBox settingContainer;
    private Button setting;
    public static Button useraccount;
    
    public Header(){
        TITLE = new Text();
        TITLE.setId("header-title");
        
        HBox titleContainer = new HBox(5);
        titleContainer.setAlignment(Pos.CENTER_LEFT);
        titleContainer.setPadding(new Insets(12));
        titleContainer.getChildren().addAll(TITLE);
        
        ImageView image = new ImageView(new Image("icon/setting.png"));
        
        if(Main.HEIGHT > 800){
            image.setFitWidth(35);
            image.setFitHeight(35);
        }else{
            image.setFitWidth(25);
            image.setFitHeight(25);
        }
        
        setting = new Button();
        setting.setStyle("-fx-base: white;");
        setting.setGraphic(image);
        setting.setFocusTraversable(false);
        setting.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!Header.TITLE.getText().equals("Connection Setting")){
                    Main.prevScreen = Main.ROOT.getCenter();
                    Main.prevTitle = TITLE.getText();
                    Main.ROOT.setCenter(new ConnectionSetting().getLayout());
                }
            }
        });
        
        ImageView image2 = new ImageView(new Image("icon/user1.png"));
        
        if(Main.HEIGHT > 800){
            image2.setFitWidth(35);
            image2.setFitHeight(35);
        }else{
            image2.setFitWidth(25);
            image2.setFitHeight(25);
        }
        
        useraccount = new Button();
        useraccount.setStyle("-fx-base: white;");
        useraccount.setGraphic(image2);
        useraccount.setFocusTraversable(false);
        useraccount.setVisible(false);
        useraccount.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Node node = new UserAccount().getLayout();
                Dialog.show("User Account", node);
            }
        });
        
        settingContainer = new HBox(8);
        settingContainer.setPadding(new Insets(8));
        settingContainer.setAlignment(Pos.CENTER);
        settingContainer.getChildren().addAll(useraccount, setting);
        
        root = new BorderPane();
        root.setId("header-container");
        root.setLeft(titleContainer);
        root.setRight(settingContainer);
    }
    
    public static void showSetting(boolean b){
        settingContainer.setVisible(b);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
