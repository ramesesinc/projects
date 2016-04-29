package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.cell.AccountCell;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.task.AccountTask;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class StuboutAccountList {
    
    private VBox root;
    private ListView<Account> listView;
    private TextField search_account;
    private List<Account> data;
    private Button refresh;
    
    public StuboutAccountList(Stubout stubout,Zone zone){
        Header.TITLE.setText(stubout.getCode());
        
        data = DatabasePlatformFactory.getPlatform().getDatabase().getAccountByStubout(stubout,"");
        
        search_account = new TextField();
        search_account.setId("search-account");
        search_account.setPromptText("Search Account");
        search_account.setFocusTraversable(false);
        search_account.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                data = DatabasePlatformFactory.getPlatform().getDatabase().getAccountByStubout(stubout,value);
                listView.setItems(FXCollections.observableArrayList(data));
            }
        });

        listView = new ListView();
        listView.setItems(FXCollections.observableArrayList(data));
        listView.setFocusTraversable(true);
        listView.setPrefHeight(Main.HEIGHT);
        listView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> param) {
                return new AccountCell();
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>(){
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account account) {
                int position = listView.getSelectionModel().getSelectedIndex();
                new Thread(new AccountTask(account, stubout, position)).start();
            }
        });
        listView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Account account = listView.getSelectionModel().getSelectedItem();
                    if(account != null){
                        int position = listView.getSelectionModel().getSelectedIndex();
                        new Thread(new AccountTask(account, stubout, position)).start();
                    }
                }
            }
        });
        
        refresh = new Button("Refresh");
        refresh.getStyleClass().add("terminal-button");
        refresh.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                data = DatabasePlatformFactory.getPlatform().getDatabase().getAccountByStubout(stubout,search_account.getText());
                listView.setItems(FXCollections.observableArrayList(data));
            }
        });

        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        root.setStyle("-fx-background-color: white;");
        root.getChildren().addAll(search_account, listView, refresh);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new StuboutList(zone).getLayout());
                }
            }
        });
        Main.prevScreen = getLayout();
        }
        
        public Node getLayout(){
            return root;
        }
    
}
