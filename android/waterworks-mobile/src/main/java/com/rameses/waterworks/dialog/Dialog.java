package com.rameses.waterworks.dialog;

import com.rameses.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dino
 */
public class Dialog {

    private static StackPane black;
    
    public static void show(String title,Node child){
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        
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
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 22px;");
        label.setWrapText(true);
        
        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("terminal-button");
        okBtn.setPrefWidth(100);
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
        root.setPadding(new Insets(20));
        root.setPrefWidth(Main.WIDTH - 200);
        root.getChildren().addAll(label,btnContainer);
        
        Group container = new Group();
        container.getChildren().add(root);
        
        Main.PAGE.getChildren().add(black);
        Main.PAGE.getChildren().add(container);
    }
    
    private static int status;
    public static int showConfirm(String message){
        status = 0;
        
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 22px;");
        label.setWrapText(true);
        
        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("terminal-button");
        okBtn.setPrefWidth(120);
        okBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                status = 1;
                Dialog.hide();
            }
        });
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("terminal-button");
        cancelBtn.setPrefWidth(120);
        cancelBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                status = -1;
                Dialog.hide();
            }
        });
        
        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.getChildren().addAll(okBtn,cancelBtn);
        
        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(20));
        root.setPrefWidth(Main.WIDTH - 200);
        root.getChildren().addAll(label,btnContainer);
        
        Group container = new Group();
        container.getChildren().add(root);
        
        Main.PAGE.getChildren().add(black);
        Main.PAGE.getChildren().add(container);
        
        return status;
    }
    
    public static void showError(String message){
        black = new StackPane();
        black.setStyle("-fx-background-color: black; -fx-opacity: 0.7;");
        black.setPrefSize(Main.WIDTH, Main.HEIGHT);
        
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 22px;");
        label.setWrapText(true);
        
        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("terminal-button");
        okBtn.setPrefWidth(100);
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
        root.setPadding(new Insets(20));
        root.setPrefWidth(Main.WIDTH - 200);
        root.getChildren().addAll(label,btnContainer);
        
        Group container = new Group();
        container.getChildren().add(root);
        
        Main.PAGE.getChildren().add(black);
        Main.PAGE.getChildren().add(container);
    }
    
    private static HBox createTitle(String s){
        Label title = new Label(s);
        title.setStyle("-fx-text-fill: white; -fx-font-size: 25px; -fx-font-weight: bold;");
        
        HBox container = new HBox();
        container.setStyle("-fx-background-color: #3897ee;");
        container.setPadding(new Insets(15));
        container.getChildren().add(title);
        
        return container;
    }
    
    public static void hide(){
        Main.PAGE.getChildren().removeAll(Main.PAGE.getChildren());
        Main.PAGE.getChildren().add(Main.ROOT);
    }
    
}
