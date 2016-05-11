package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.cell.AccountCell;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.task.AccountTask;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class AccountList {
   
    private Database db;
    private TextField search_account;
    private VBox root;
    private ListView<Account> accountList;
    private Label recordsize;
    private Label pagesize;
    private TabPane tabPane;
    private List<Account> accountData;
    private int datasize = 0;
    private int psize;
    private int position = 0;
    
    public AccountList(){
        Header.TITLE.setText("Accounts");
        
        Tab account_tab = new Tab("Accounts");
        account_tab.getStyleClass().add("login-label");
        account_tab.setClosable(false);
        account_tab.setContent(createAccountLayout());
        
        Tab zone_tab = new Tab("Zones");
        zone_tab.getStyleClass().add("login-label");
        zone_tab.setClosable(false);
        zone_tab.setContent(new ZoneList().getLayout());
        
        tabPane = new TabPane();
        tabPane.getTabs().addAll(zone_tab, account_tab);
        
        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.getChildren().add(tabPane);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
        Main.prevScreen = getLayout();
    }
    
    private void load(int pos){
        if(datasize > 0){
            int start = pos * 20;
            int end = start + 20;
            if(end > datasize){
                end = datasize;
            }
            List<Account> list = accountData.subList(start, end);
            accountList.setItems(FXCollections.observableArrayList(list));
            recordsize.setText(list.size() + " records");
            pagesize.setText("Page " + (pos+1) + " of " + psize);
        }
    }
    
    private Node createAccountLayout(){
        //LOAD THE THE FIRST 20 RECORDS
        db = DatabasePlatformFactory.getPlatform().getDatabase();
        accountData = db.getSearchAccountResult("");
        ObservableList<Account> dataitems = FXCollections.observableArrayList(accountData);
        datasize = accountData.size();
        psize = (int) datasize / 20;
        int remainder = datasize % 20;
        if(remainder > 0){
            psize = psize + 1;
        }
        if(!db.getError().isEmpty()) Dialog.showError(db.getError());
        
        search_account = new TextField();
        search_account.setId("search-account");
        search_account.setPromptText("Search Account");
        search_account.setFocusTraversable(false);
        search_account.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                accountData = db.getSearchAccountResult(value);
                if(!db.getError().isEmpty()) Dialog.showError(db.getError());
                ObservableList<Account> dataitems = FXCollections.observableArrayList(accountData);
                datasize = accountData.size();
                psize = (int) datasize / 20;
                int remainder = datasize % 20;
                if(remainder > 0){
                    psize = psize + 1;
                }
                if(datasize == 0){
                    accountList.setItems(dataitems);
                    recordsize.setText("0 records");
                    pagesize.setText("Page 1 of ?");
                }else{
                    loadFirstPage();
                }
            }
        });
        
        recordsize = new Label("0 records");
        recordsize.getStyleClass().add("accountlist-label");
        
        pagesize = new Label("Page 1 of " + (psize > 0 ? psize : "?"));
        pagesize.getStyleClass().add("accountlist-label");
        
        accountList = new ListView();
        accountList.setPrefHeight(Main.HEIGHT);
        accountList.setItems(dataitems);
        accountList.setFocusTraversable(true);
        accountList.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> param) {
                return new AccountCell();
            }
        });
        accountList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>(){
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account account) {
                new Thread(new AccountTask(account,null,-1)).start();
            }
        });
        accountList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Account account = accountList.getSelectionModel().getSelectedItem();
                    if(account != null){
                        int position = accountList.getSelectionModel().getSelectedIndex();
                        new Thread(new AccountTask(account, null, -1)).start();
                    }
                }
            }
        });
        
        loadFirstPage();
        
        ImageView firstPageImg = new ImageView(new Image("icon/firstpage.png"));
        
        Button firstPage = new Button();
        firstPage.setGraphic(firstPageImg);
        firstPage.getStyleClass().add("nav-button");
        firstPage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadFirstPage();
            }
        });
        
        ImageView prevImg = new ImageView(new Image("icon/previous.png"));
        
        Button prev = new Button();
        prev.setGraphic(prevImg);
        prev.getStyleClass().add("nav-button");
        prev.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadPrevious();
            }
        });
        
        ImageView nextImg = new ImageView(new Image("icon/next.png"));
        
        Button next = new Button();
        next.setGraphic(nextImg);
        next.getStyleClass().add("nav-button");
        next.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadNext();
            }
        });
        
        ImageView lastPageImg = new ImageView(new Image("icon/lastpage.png"));
        
        Button lastPage = new Button();
        lastPage.setGraphic(lastPageImg);
        lastPage.getStyleClass().add("nav-button");
        lastPage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadLastPage();
            }
        });
        
        if(Main.HEIGHT < 700){
            firstPageImg.setFitWidth(30);
            firstPageImg.setFitHeight(15);
            prevImg.setFitWidth(30);
            prevImg.setFitHeight(15);
            nextImg.setFitWidth(30);
            nextImg.setFitHeight(15);
            lastPageImg.setFitWidth(30);
            lastPageImg.setFitHeight(15);
            
            firstPage.setPrefWidth(30);
            prev.setPrefWidth(30);
            next.setPrefWidth(30);
            lastPage.setPrefWidth(30);
        }else if(Main.HEIGHT < 1200){
            firstPageImg.setFitWidth(40);
            firstPageImg.setFitHeight(20);
            prevImg.setFitWidth(40);
            prevImg.setFitHeight(20);
            nextImg.setFitWidth(40);
            nextImg.setFitHeight(20);
            lastPageImg.setFitWidth(40);
            lastPageImg.setFitHeight(20);
            
            firstPage.setPrefWidth(40);
            prev.setPrefWidth(40);
            next.setPrefWidth(40);
            lastPage.setPrefWidth(40);
        }else{
            firstPageImg.setFitWidth(50);
            firstPageImg.setFitHeight(30);
            prevImg.setFitWidth(50);
            prevImg.setFitHeight(30);
            nextImg.setFitWidth(50);
            nextImg.setFitHeight(30);
            lastPageImg.setFitWidth(50);
            lastPageImg.setFitHeight(30);
            
            firstPage.setPrefWidth(60);
            prev.setPrefWidth(60);
            next.setPrefWidth(60);
            lastPage.setPrefWidth(60);
        }
        
        HBox navContainer = new HBox(8);
        navContainer.setAlignment(Pos.CENTER_LEFT);
        navContainer.getChildren().addAll(firstPage,prev,next,lastPage,recordsize,pagesize);
        
        VBox account_root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        account_root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        account_root.getChildren().addAll(search_account,accountList,navContainer);
        
        return account_root;
    }
    
    private void loadFirstPage(){
        position = 0;
        load(position);
    }
    
    private void loadPrevious(){
        if(position > 0){
            position--;
        }
        load(position);
    }
    
    private void loadNext(){
        if(position < (psize-1) ){
            position++;
        }
        load(position);
    }
    
    private void loadLastPage(){
        position = psize-1;
        load(position);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
