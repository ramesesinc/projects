package com.rameses.waterworks.dialog;

import com.rameses.Main;
import com.rameses.waterworks.page.Home;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dino
 */
public class Dialog {

    public static boolean isOpen = false;
    private static StackPane black;
    
    public static void show(String title,Node child){
        isOpen = true;
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        black.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Dialog.hide();
                }
            }
        });

        if(!Main.PAGE.getChildren().contains(black)){
            Main.PAGE.getChildren().add(black);
        }

        Group container = new Group();

        VBox box = new VBox();

        if(!title.isEmpty()){
            box.getChildren().add(createTitle(title));
        }
        box.getChildren().add(child);
        container.getChildren().add(box);

        Main.PAGE.getChildren().add(container);
    }
    
    public static void showAlert(String message){
        isOpen = true;
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        black.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Dialog.hide();
                }
            }
        });

        Label label = new Label(message);
        label.getStyleClass().add("dialog-label");
        label.setWrapText(true);

        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("terminal-button");
        okBtn.setPrefWidth(Main.HEIGHT > 700 ? 100 : 70);
        okBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog.hide();
            }
        });

        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(okBtn);

        VBox root = new VBox(Main.HEIGHT > 700 ? 30 : 10);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(Main.HEIGHT > 700 ? new Insets(20) : new Insets(10));
        root.setPrefWidth(Main.HEIGHT > 700 ? Main.WIDTH - 200 : Main.WIDTH - 30);
        root.getChildren().addAll(label,btnContainer);

        Group container = new Group();
        container.getChildren().add(root);

        Main.PAGE.getChildren().add(black);
        Main.PAGE.getChildren().add(container);
    }
    
    public static void showError(String message){
        isOpen = true;
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        black.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Dialog.hide();
                }
            }
        });

        Label label = new Label(message);
        label.getStyleClass().add("dialog-label");
        label.setWrapText(true);

        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("terminal-button");
        okBtn.setPrefWidth(Main.HEIGHT > 700 ? 100 : 70);
        okBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog.hide();
            }
        });

        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(okBtn);

        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(Main.HEIGHT > 700 ? new Insets(20) : new Insets(10));
        root.setPrefWidth(Main.HEIGHT > 700 ? Main.WIDTH - 200 : Main.WIDTH - 30);
        root.getChildren().addAll(label,btnContainer);

        Group container = new Group();
        container.getChildren().add(root);

        Main.PAGE.getChildren().add(black);
        Main.PAGE.getChildren().add(container);
    }
    
    public static void wait(String title){
        isOpen = true;
        ProgressIndicator progress = new ProgressIndicator(-1);
        
        Label label = new Label(title);
        label.getStyleClass().add("login-label");
        
        HBox container = new HBox(Main.HEIGHT > 700 ? 20 : 10);
        container.setStyle("-fx-background-color: white;");
        container.setAlignment(Pos.CENTER);
        container.setPadding(Main.HEIGHT > 700 ? new Insets(20,30,20,30) : new Insets(10,15,10,15));
        container.getChildren().addAll(progress,label);
        
        show("", container);
    }
    
    private static HBox createTitle(String s){
        Label title = new Label(s);
        title.getStyleClass().add("dialog-label");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        HBox container = new HBox();
        container.setStyle("-fx-background-color: #3897ee;");
        container.setPadding(Main.HEIGHT > 700 ? new Insets(15) : new Insets(10));
        container.getChildren().add(title);
        container.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 1) Dialog.hide();
            }
        });
        
        return container;
    }
    
    public static void hide(){
        isOpen = false;
        Main.PAGE.getChildren().removeAll(Main.PAGE.getChildren());
        Main.PAGE.getChildren().add(Main.ROOT);
    }
    
}
