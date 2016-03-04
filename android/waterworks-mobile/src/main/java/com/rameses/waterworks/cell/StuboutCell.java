package com.rameses.waterworks.cell;

import com.rameses.Main;
import com.rameses.waterworks.bean.Stubout;
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
public class StuboutCell extends ListCell<Stubout>{
    @Override
    protected void updateItem(Stubout stubout, boolean empty){
        super.updateItem(stubout, empty);
        if(!empty){
            ImageView image = new ImageView(new Image("icon/stubout.png"));

            StackPane imgContainer = new StackPane();
            imgContainer.setAlignment(Pos.CENTER);
            imgContainer.getChildren().add(image);


            Label title = new Label(stubout.getTitle());
            Label description = new Label(stubout.getDescription());

            if(Main.HEIGHT > 800){
                title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
                description.setStyle("-fx-font-size: 22px;");
            }else{
                image.setFitWidth(image.getFitWidth()*0.75);
                image.setFitHeight(image.getFitHeight()*0.75);
                title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
                description.setStyle("-fx-font-size: 18px;");
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
