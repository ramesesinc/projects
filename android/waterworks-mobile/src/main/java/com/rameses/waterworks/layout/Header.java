package com.rameses.waterworks.layout;

import com.rameses.Main;
import com.rameses.waterworks.page.ConnectionSetting;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Header {
    
    public static Text TITLE;
    private BorderPane root;
    private static VBox settingContainer;
    private Button setting;
    
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
        setting.setGraphic(image);
        setting.setFocusTraversable(false);
        setting.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Main.prevScreen = Main.ROOT.getCenter();
                Main.prevTitle = TITLE.getText();
                Main.ROOT.setCenter(new ConnectionSetting().getLayout());
            }
        });
        
        settingContainer = new VBox();
        settingContainer.setPadding(new Insets(8));
        settingContainer.setAlignment(Pos.CENTER);
        settingContainer.getChildren().add(setting);
        
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
