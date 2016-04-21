package com.rameses.waterworks.cell;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
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
public class AccountCell extends ListCell<Account>{
    @Override
    protected void updateItem(Account account, boolean empty){
        super.updateItem(account, empty);
        if(!empty){
            Database db = DatabasePlatformFactory.getPlatform().getDatabase();
            Reading reading = db.findReadingByAccount(account.getObjid());

            ImageView image = new ImageView(new Image("icon/useraccount.png"));
            ImageView image2 = new ImageView(new Image("icon/useraccount-check.png"));

            StackPane imgContainer = new StackPane();
            imgContainer.setAlignment(Pos.CENTER);
            if(reading == null){
                imgContainer.getChildren().add(image);
            }else{
                imgContainer.getChildren().add(image2);
            }

            Label serialno = new Label(account.getSerialNo());

            Label name = new Label(account.getAcctName());

            String addr = account.getAddress().replaceAll("\n", "");
            Label address = new Label(addr);
            
            if(Main.HEIGHT < 700){
                image.setFitWidth(80);
                image.setFitHeight(80);
                image2.setFitWidth(80);
                image2.setFitHeight(80);
                
                serialno.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                name.setStyle("-fx-font-size: 14px;");
                address.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
            }else if(Main.HEIGHT < 1200){
                image.setFitWidth(110);
                image.setFitHeight(110);
                image2.setFitWidth(110);
                image2.setFitHeight(110);
                
                serialno.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                name.setStyle("-fx-font-size: 17px;");
                address.setStyle("-fx-font-size: 15px; -fx-font-style: italic;");
            }else{
                serialno.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
                name.setStyle("-fx-font-size: 22px;");
                address.setStyle("-fx-font-size: 24px; -fx-font-style: italic;");
            }

            VBox detail = new VBox(3);
            detail.setAlignment(Pos.CENTER_LEFT);
            detail.getChildren().addAll(serialno,name,address);

            HBox root = new HBox(5);
            root.getChildren().addAll(imgContainer,detail);

            setGraphic(root);
        }else{
            setGraphic(null);
        }
    }
}
