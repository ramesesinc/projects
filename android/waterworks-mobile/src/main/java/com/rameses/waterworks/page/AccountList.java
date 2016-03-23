package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.cell.AccountCell;
import com.rameses.waterworks.cell.StuboutCell;
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
    private VBox root;
    private ListView<Account> accountList;
    private ListView<Stubout> stuboutList;
    private Label recordsize;
    private Label pagesize;
    private TabPane tabPane;
    private List<Account> accountData;
    private List<Stubout> stuboutData;
    private int datasize = 0;
    private int psize;
    private int position = 0;
    
    public AccountList(){
        Header.TITLE.setText("Accounts");
        
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
        
        //LAYOUT FOR THE ACCOUNT TAB (START)
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
                if(event.getClickCount()==2){
                    Account account = accountList.getSelectionModel().getSelectedItem();
                    if(account != null){
                        Node child = new AccountDetail(account).getLayout();
                        Dialog.show("Account Information", child);
                    }
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
        firstPage.setPrefWidth(60);
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
        prev.setPrefWidth(60);
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
        next.setPrefWidth(60);
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
        lastPage.setPrefWidth(60);
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
        //LAYOUT FOR THE ACCOUNT TAB (END)
        
        //LAYOUT FOR THE STUBOUT TAB (START)
        search_stubout = new TextField();
        search_stubout.setId("search-account");
        search_stubout.setPromptText("Search Stubout");
        search_stubout.setFocusTraversable(false);
        search_stubout.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                stuboutData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchStuboutResult(value);
                stuboutList.setItems(FXCollections.observableArrayList(stuboutData));
            }
        });

        stuboutData = DatabasePlatformFactory.getPlatform().getDatabase().getSearchStuboutResult("");
        
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
                if(event.getClickCount()==2){
                    Stubout stubout = stuboutList.getSelectionModel().getSelectedItem();
                    if(stubout != null){
                        Node child = new StuboutAccountList(stubout).getLayout();
                        Main.ROOT.setCenter(child);
                    }
                }
            }
        });
        
        VBox stubout_root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        stubout_root.setPadding(new Insets(Main.HEIGHT > 700 ? 20 : 10));
        stubout_root.getChildren().addAll(search_stubout, stuboutList);
        //LAYOUT FOR THE STUBOUT TAB (END)
        
        Tab account_tab = new Tab("Accounts");
        account_tab.getStyleClass().add("login-label");
        account_tab.setClosable(false);
        account_tab.setContent(account_root);
        
        Tab stubout_tab = new Tab("Stubouts");
        stubout_tab.getStyleClass().add("login-label");
        stubout_tab.setClosable(false);
        stubout_tab.setContent(stubout_root);
        
        tabPane = new TabPane();
        tabPane.getTabs().addAll(stubout_tab, account_tab);
        
        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.getChildren().add(tabPane);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
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
