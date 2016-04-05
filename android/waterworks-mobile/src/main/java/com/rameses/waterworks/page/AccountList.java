package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import com.rameses.waterworks.cell.AccountCell;
import com.rameses.waterworks.cell.StuboutCell;
import com.rameses.waterworks.cell.ZoneCell;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TextField search_stubout;
    private TextField search_zone;
    private VBox root;
    private ListView<Account> accountList;
    private ListView<Stubout> stuboutList;
    private ListView<Zone> zoneList;
    private Label recordsize;
    private Label pagesize;
    private TabPane tabPane;
    private List<Account> accountData;
    private List<Stubout> stuboutData;
    private List<Zone> zoneData;
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
        zone_tab.setContent(createZoneLayout());
        
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
            if(list.size() > 0) accountList.getSelectionModel().select(0);
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
        accountList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Account account = accountList.getSelectionModel().getSelectedItem();
                if(account != null){
                    Node child = new AccountDetail(account).getLayout();
                    Dialog.show("Account Information", child);
                }
            }
        });
        loadFirstPage();
        
        ImageView firstPageImg = new ImageView(new Image("icon/firstpage.png"));
        firstPageImg.setFitWidth(Main.HEIGHT > 700 ? 50 : 30);
        firstPageImg.setFitHeight(Main.HEIGHT > 700 ? 30 : 15);
        
        Button firstPage = new Button();
        firstPage.setGraphic(firstPageImg);
        firstPage.getStyleClass().add("nav-button");
        firstPage.setPrefWidth(Main.HEIGHT > 700 ? 60 : 40);
        firstPage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadFirstPage();
            }
        });
        
        ImageView prevImg = new ImageView(new Image("icon/previous.png"));
        prevImg.setFitWidth(Main.HEIGHT > 700 ? 50 : 30);
        prevImg.setFitHeight(Main.HEIGHT > 700 ? 30 : 15);
        
        Button prev = new Button();
        prev.setGraphic(prevImg);
        prev.getStyleClass().add("nav-button");
        prev.setPrefWidth(Main.HEIGHT > 700 ? 60 : 40);
        prev.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadPrevious();
            }
        });
        
        ImageView nextImg = new ImageView(new Image("icon/next.png"));
        nextImg.setFitWidth(Main.HEIGHT > 700 ? 50 : 30);
        nextImg.setFitHeight(Main.HEIGHT > 700 ? 30 : 15);
        
        Button next = new Button();
        next.setGraphic(nextImg);
        next.getStyleClass().add("nav-button");
        next.setPrefWidth(Main.HEIGHT > 700 ? 60 : 40);
        next.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadNext();
            }
        });
        
        ImageView lastPageImg = new ImageView(new Image("icon/lastpage.png"));
        lastPageImg.setFitWidth(Main.HEIGHT > 700 ? 50 : 30);
        lastPageImg.setFitHeight(Main.HEIGHT > 700 ? 30 : 15);
        
        Button lastPage = new Button();
        lastPage.setGraphic(lastPageImg);
        lastPage.getStyleClass().add("nav-button");
        lastPage.setPrefWidth(Main.HEIGHT > 700 ? 60 : 40);
        lastPage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadLastPage();
            }
        });
        
        HBox navContainer = new HBox(8);
        navContainer.setAlignment(Pos.CENTER_LEFT);
        navContainer.getChildren().addAll(firstPage,prev,next,lastPage,recordsize,pagesize);
        
        VBox account_root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        account_root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        account_root.getChildren().addAll(search_account,accountList,navContainer);
        
        return account_root;
    }
    
    private Node createStuboutLayout(Zone zone){
        search_stubout = new TextField();
        search_stubout.setId("search-account");
        search_stubout.setPromptText("Search Stubout");
        search_stubout.setFocusTraversable(false);
        search_stubout.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                stuboutData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchStuboutResult(value,zone);
                stuboutList.setItems(FXCollections.observableArrayList(stuboutData));
            }
        });

        stuboutData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchStuboutResult("",zone);
        
        stuboutList = new ListView();
        stuboutList.setPrefHeight(Main.HEIGHT);
        stuboutList.setItems(FXCollections.observableArrayList(stuboutData));
        stuboutList.setFocusTraversable(true);
        stuboutList.setCellFactory(new Callback<ListView<Stubout>, ListCell<Stubout>>() {
            @Override
            public ListCell<Stubout> call(ListView<Stubout> param) {
                return new StuboutCell();
            }
        });
        stuboutList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Stubout stubout = stuboutList.getSelectionModel().getSelectedItem();
                if(stubout != null){
                    Node child = new StuboutAccountList(stubout).getLayout();
                    Main.ROOT.setCenter(child);
                }
            }
        });
        
        Button back = new Button("Back");
        back.getStyleClass().add("terminal-button");
        back.setStyle(Main.HEIGHT > 700 ? "-fx-font-size: 30px;" : "-fx-font-size: 17px;");
        back.setGraphicTextGap(Main.HEIGHT > 700 ? 10 : 3);
        back.setPrefWidth(Main.HEIGHT > 700 ? 180 : 100);
        back.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(Dialog.isOpen){ Dialog.hide(); return; }
                Main.ROOT.setCenter(new AccountList().getLayout());
            }
        });
        
        VBox stubout_root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        stubout_root.setAlignment(Pos.CENTER_RIGHT);
        stubout_root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        stubout_root.getChildren().addAll(search_stubout, stuboutList, back);
        
        Main.prevScreen = stubout_root;
        return stubout_root;
    }
    
    private Node createZoneLayout(){
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
        zoneList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Zone zone = zoneList.getSelectionModel().getSelectedItem();
                if(zone != null){
                    Main.ROOT.setCenter(createStuboutLayout(zone));
                }
            }
        });
        
        VBox stubout_root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        stubout_root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        stubout_root.getChildren().addAll(search_zone, zoneList);
        
        Main.prevScreen = stubout_root;
        return stubout_root;
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
