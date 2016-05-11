package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.cell.ZoneCell;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.task.ZoneTask;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ZoneList {
    
    private TextField search_zone;
    private ListView<Zone> zoneList;
    private List<Zone> zoneData;
    private VBox root;
    
    public ZoneList(){
        search_zone = new TextField();
        search_zone.setId("search-account");
        search_zone.setPromptText("Search Zone");
        search_zone.setFocusTraversable(false);
        search_zone.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                zoneData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchZoneResult(value);
                zoneList.setItems(FXCollections.observableArrayList(zoneData));
            }
        });

        zoneData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchZoneResult("");
        
        zoneList = new ListView();
        zoneList.setPrefHeight(Main.HEIGHT);
        zoneList.setItems(FXCollections.observableArrayList(zoneData));
        zoneList.setFocusTraversable(true);
        zoneList.setCellFactory(new Callback<ListView<Zone>, ListCell<Zone>>() {
            @Override
            public ListCell<Zone> call(ListView<Zone> param) {
                return new ZoneCell();
            }
        });
        zoneList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>(){
            @Override
            public void changed(ObservableValue<? extends Zone> observable, Zone oldValue, Zone zone) {
                new Thread(new ZoneTask(zone)).start();
            }
        });
        
        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        root.getChildren().addAll(search_zone, zoneList);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new AccountList().getLayout());
                }
            }
        });
    }
    
    public Node getLayout(){
        return root;
    }
    
}
