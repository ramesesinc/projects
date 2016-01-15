package com.rameses.waterworks.layout;

import com.rameses.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Footer {

    private BorderPane root;
    private ImageView logo;
    private Text title1, title2;

    public Footer() {
        title1 = new Text("Rameses Systems Inc.");
        title1.getStyleClass().add("footer-title");
        title1.getStyleClass().add("bold");

        title2 = new Text("www.ramesesinc.com");
        title2.getStyleClass().add("footer-title");

        logo = new ImageView(new Image("icon/rameses.png"));
        
        if(Main.HEIGHT < 800){
            logo.setFitWidth(logo.getFitWidth() * 0.75);
            logo.setFitHeight(logo.getFitHeight() * 0.75);
        }

        VBox titleContainer = new VBox(5);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().addAll(title1, title2);

        HBox logoContainer = new HBox();
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.getChildren().add(logo);

        root = new BorderPane();
        root.setId("footer-container");
        root.setPadding(new Insets(8, 8, 8, 8));
        root.setRight(titleContainer);
        root.setLeft(logoContainer);
    }

    public Node getLayout() {
        return root;
    }

}
