
package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.util.SystemPlatformFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserAccount {
    
    private HBox root;
    
    public UserAccount(){
        ImageView user_img = new ImageView(new Image("icon/user.png"));
        if(Main.HEIGHT < 700){
            user_img.setFitWidth(130);
            user_img.setFitHeight(130);
        }
        
        if(Main.HEIGHT < 1200){
            user_img.setFitWidth(150);
            user_img.setFitHeight(150);
        }
        
        HBox img_container = new HBox();
        img_container.setStyle("-fx-border-color: gray; -fx-border-width: 2px;");
        img_container.getChildren().add(user_img);
        
        Label username = new Label(SystemPlatformFactory.getPlatform().getSystem().getUserName());
        username.getStyleClass().add("terminal-label");
        
        Label fullname = new Label(SystemPlatformFactory.getPlatform().getSystem().getFullName());
        fullname.getStyleClass().add("terminal-label");
        
        Button logout = new Button("Logout");
        logout.getStyleClass().add("terminal-button");
        logout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Main.MYACCOUNT = null;
                Main.ROOT.setCenter(new Login().getLayout());
                Header.useraccount.setVisible(false);
                Dialog.hide();
            }
        });
        
        VBox vbox1 = new VBox(Main.HEIGHT > 700 ? 15 : 10);
        vbox1.setPrefWidth(Main.WIDTH);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.getChildren().addAll(username,fullname,logout);
        
        root = new HBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(Main.HEIGHT > 700 ? new Insets(15) : new Insets(10));
        root.setMaxWidth(Main.HEIGHT > 700 ? Main.WIDTH * 0.90 : Main.WIDTH * 0.95);
        root.getChildren().add(img_container);
        root.getChildren().add(vbox1);
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Dialog.hide();
            }
        });
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Dialog.hide();
                }
            }
        });
        if(Main.HEIGHT < 700){
            username.setStyle("-fx-font-size: 18px; -fx-underline: true;");
            fullname.setStyle("-fx-font-size: 18px; -fx-underline: true;");
            logout.setStyle("-fx-font-size: 16px;");
            logout.setPrefWidth(120);
        }else if(Main.HEIGHT < 1200){
            username.setStyle("-fx-font-size: 25px; -fx-underline: true;");
            fullname.setStyle("-fx-font-size: 18px; -fx-underline: true;");
            logout.setStyle("-fx-font-size: 22px;");
            logout.setPrefWidth(150);
        }else{
            username.setStyle("-fx-font-size: 32px; -fx-underline: true;");
            fullname.setStyle("-fx-font-size: 18px; -fx-underline: true;");
            logout.setStyle("-fx-font-size: 28px;");
            logout.setPrefWidth(180);
        }
    }
    
    public Node getLayout(){
        return root;
    }
    
}
