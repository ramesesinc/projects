package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class AccountList {
   
    private TextField search;
    private VBox listContainer;
    private Database db;
    private ListView<Account> accountList;
    private List<Account> data;
    private int datasize = 0;
    private Label recordsize;
    private Label pagesize;
    private int psize;
    private int position = 0;
    
    public AccountList(){
        Header.TITLE.setText("Accounts");
        
        db = DatabasePlatformFactory.getPlatform().getDatabase();
        data = db.getSearchResult("");
        ObservableList<Account> dataitems = FXCollections.observableArrayList(data);
        datasize = data.size();
        psize = (int) datasize / 20;
        int remainder = datasize % 20;
        if(remainder > 0){
            psize = psize + 1;
        }
        
        if(!db.getError().isEmpty()) Dialog.showError(db.getError());
        
        search = new TextField();
        search.setId("search-account");
        search.setPromptText("Search Account");
        search.setFocusTraversable(false);
        search.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String so, String value) {
                data = db.getSearchResult(value);
                if(!db.getError().isEmpty()) Dialog.showError(db.getError());
                ObservableList<Account> dataitems = FXCollections.observableArrayList(data);
                datasize = data.size();
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
                    Node child = new AccountDetail(account).getLayout();
                    Dialog.show("Account Information", child);
                }
            }
        });
        loadFirstPage();
        
        ImageView firstPageImg = new ImageView(new Image("icon/firstpage.png"));
        firstPageImg.setFitWidth(50);
        firstPageImg.setFitHeight(30);
        
        Button firstPage = new Button();
        firstPage.setGraphic(firstPageImg);
        firstPage.getStyleClass().add("nav-button");
        firstPage.setPrefWidth(80);
        firstPage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadFirstPage();
            }
        });
        
        ImageView prevImg = new ImageView(new Image("icon/previous.png"));
        prevImg.setFitWidth(50);
        prevImg.setFitHeight(30);
        
        Button prev = new Button();
        prev.setGraphic(prevImg);
        prev.getStyleClass().add("nav-button");
        prev.setPrefWidth(80);
        prev.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadPrevious();
            }
        });
        
        ImageView nextImg = new ImageView(new Image("icon/next.png"));
        nextImg.setFitWidth(50);
        nextImg.setFitHeight(30);
        
        Button next = new Button();
        next.setGraphic(nextImg);
        next.getStyleClass().add("nav-button");
        next.setPrefWidth(80);
        next.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadNext();
            }
        });
        
        ImageView lastPageImg = new ImageView(new Image("icon/lastpage.png"));
        lastPageImg.setFitWidth(50);
        lastPageImg.setFitHeight(30);
        
        Button lastPage = new Button();
        lastPage.setGraphic(lastPageImg);
        lastPage.getStyleClass().add("nav-button");
        lastPage.setPrefWidth(80);
        lastPage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                loadLastPage();
            }
        });
        
        HBox navContainer = new HBox(8);
        navContainer.setAlignment(Pos.CENTER_LEFT);
        navContainer.getChildren().addAll(firstPage,prev,next,lastPage,recordsize,pagesize);
        
        listContainer = new VBox();
        if(Main.HEIGHT > 800){
            listContainer.setSpacing(10);
            listContainer.setPadding(new Insets(20));
        }else{
            listContainer.setSpacing(5);
            listContainer.setPadding(new Insets(10));
        }
        listContainer.getChildren().addAll(search,accountList,navContainer);
        listContainer.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
    }
    
    private void load(int pos){
        if(datasize > 0){
            int start = pos * 20;
            int end = start + 20;
            if(end > datasize){
                end = datasize;
            }
            List<Account> list = data.subList(start, end);
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
    
    class AccountCell extends ListCell<Account>{
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
                
                Label acctno = new Label(account.getAcctNo());
                
                Label name = new Label(account.getAcctName()+" ( "+account.getSerialNo()+" )");
                
                String addr = account.getAddress().replaceAll("\n", "");
                Label address = new Label(addr);
                
                if(Main.HEIGHT > 800){
                    acctno.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
                    name.setStyle("-fx-font-size: 22px;");
                    address.setStyle("-fx-font-size: 24px; -fx-font-style: italic;");
                }else{
                    image.setFitWidth(image.getFitWidth()*0.75);
                    image.setFitHeight(image.getFitHeight()*0.75);
                    acctno.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
                    name.setStyle("-fx-font-size: 18px;");
                    address.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
                }
                
                VBox detail = new VBox(3);
                detail.setAlignment(Pos.CENTER_LEFT);
                detail.getChildren().addAll(acctno,name,address);
                
                HBox root = new HBox(5);
                root.getChildren().addAll(imgContainer,detail);
                
                setGraphic(root);
            }else{
                setGraphic(null);
            }
        }
    }
    
    public Node getLayout(){
        return listContainer;
    }
    
}
