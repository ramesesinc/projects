package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.cell.StuboutCell;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class StuboutList {
    
    private TextField search_stubout;
    private ListView<Stubout> stuboutList;
    private List<Stubout> stuboutData;
    private VBox root;
    private boolean onScroll = false;
    
    public StuboutList(Zone zone){
        Header.TITLE.setText(zone.getDescription());
        
        search_stubout = new TextField();
        search_stubout.setId("search-account");
        search_stubout.setPromptText("Search Stubout");
        search_stubout.setFocusTraversable(false);
        search_stubout.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                stuboutData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchStuboutResult(value,zone);
                stuboutList.setItems(FXCollections.observableArrayList(stuboutData));
                if(stuboutData.size()>0) stuboutList.getSelectionModel().select(0);
            }
        });

        stuboutData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchStuboutResult("",zone);
        
        stuboutList = new ListView();
        stuboutList.setPrefHeight(Main.HEIGHT);
        stuboutList.setItems(FXCollections.observableArrayList(stuboutData));
        stuboutList.setFocusTraversable(true);
        if(stuboutData.size()>0) stuboutList.getSelectionModel().select(0);
        stuboutList.setCellFactory(new Callback<ListView<Stubout>, ListCell<Stubout>>() {
            @Override
            public ListCell<Stubout> call(ListView<Stubout> param) {
                return new StuboutCell();
            }
        });
        stuboutList.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            Stubout stubout = stuboutList.getSelectionModel().getSelectedItem();
                            if(stubout != null){
                                Node child = new StuboutAccountList(stubout,zone).getLayout();
                                Main.ROOT.setCenter(child);
                            }
                        }
                    });
                }
            }
        });
        
        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setAlignment(Pos.CENTER_RIGHT);
        root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        root.getChildren().addAll(search_stubout, stuboutList);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new AccountList().getLayout());
                }
            }
        });
        Main.prevScreen = getLayout();
    }
    
    public Node getLayout(){
        return root;
    }
    
}
