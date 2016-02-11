package com.rameses.waterworks.dialog;

import com.rameses.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
    
    public static void wait(String title){
        ProgressIndicator progress = new ProgressIndicator(-1);
        
        Label label = new Label(title);
        label.getStyleClass().add("login-label");
        
        HBox container = new HBox(20);
        container.setStyle("-fx-background-color: white;");
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20,30,20,30));
        container.getChildren().addAll(progress,label);
        
        show("", container);
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
