package com.rameses.waterworks.cell;

import com.rameses.Main;
import com.rameses.waterworks.bean.Zone;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Rameses
 */
public class ZoneCell extends ListCell<Zone>{
    @Override
    protected void updateItem(Zone zone, boolean empty){
        super.updateItem(zone, empty);
        if(!empty){
            ImageView image = new ImageView(new Image("icon/stubout.png"));

            StackPane imgContainer = new StackPane();
            imgContainer.setAlignment(Pos.CENTER);
            imgContainer.getChildren().add(image);
            
            Label title = new Label(zone.getCode() + " - " + zone.getDesc());
            Label description = new Label("Sector " + zone.getSector());

            if(Main.HEIGHT < 700){
                image.setFitWidth(80);
                image.setFitHeight(80);
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                description.setStyle("-fx-font-size: 14px;");
            }else if(Main.HEIGHT < 1200){
                image.setFitWidth(110);
                image.setFitHeight(110);
                title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                description.setStyle("-fx-font-size: 17px;");
            }else{
                title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
                description.setStyle("-fx-font-size: 22px;");
            }

            VBox detail = new VBox(3);
            detail.setAlignment(Pos.CENTER_LEFT);
            detail.getChildren().addAll(title,description);

            HBox root = new HBox(5);
            root.getChildren().addAll(imgContainer,detail);

            setGraphic(root);
        }else{
            setGraphic(null);
        }
    }
}
