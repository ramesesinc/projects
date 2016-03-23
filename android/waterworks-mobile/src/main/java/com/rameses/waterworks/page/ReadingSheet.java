package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.ItemAccount;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.calc.BillCalculator;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class ReadingSheet {
    
    private VBox root;
    private TextField field_serialno, field_name, field_class, field_prev;
    private Button save;
    private FlowPane button_container1;
    private Account account;
    private RButton[] rb;
    private int capacity = 6;
    private Reading reading;
    boolean goBackToList = false;
    
    public ReadingSheet(Account a){
        Header.TITLE.setText("Reading Sheet");
        
        Button search = new Button();
        search.setId("sheet-search");
        search.setMinWidth(Main.HEIGHT > 700 ? 100 : 50);
        search.setFocusTraversable(true);
        search.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                reading = null;
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                List<Account> result = db.getResultBySerialno(field_serialno.getText());
                if(!db.getError().isEmpty()) Dialog.showError(db.getError());
                account = null;
                if(result.size() > 0){
                    account = result.get(0);
                    if(account != null){
                        loadAccountData();
                    }
                }else{
                    Dialog.showAlert("No record found!");
                }
            }
        });
        
        Label label_serialno = new Label("Serial No.");
        label_serialno.getStyleClass().add("account-field-name");
        label_serialno.setMinWidth(Main.HEIGHT > 700 ? 200 : 100);
        
        field_serialno = new TextField();
        field_serialno.getStyleClass().add("account-field-value");
        field_serialno.setStyle("-fx-background-radius: 0px 0px 0px 0px; -fx-border-radius: 0px 0px 0px 0px;");
        field_serialno.setPrefWidth(Main.WIDTH);
        field_serialno.setFocusTraversable(false);
        
        Label label_name = new Label("Name");
        label_name.getStyleClass().add("account-field-name");
        label_name.setMinWidth(Main.HEIGHT > 700 ? 200 : 100);
        
        field_name = new TextField();
        field_name.getStyleClass().add("account-field-value");
        field_name.setPrefWidth(Main.WIDTH);
        field_name.setEditable(false);
        field_name.setFocusTraversable(false);
        
        Label label_class = new Label("Classification");
        label_class.getStyleClass().add("account-field-name");
        label_class.setMinWidth(Main.HEIGHT > 700 ? 200 : 100);
        
        field_class = new TextField();
        field_class.getStyleClass().add("account-field-value");
        field_class.setPrefWidth(Main.WIDTH);
        field_class.setEditable(false);
        field_class.setFocusTraversable(false);
        
        Label label_prev = new Label("Prev. Reading");
        label_prev.getStyleClass().add("account-field-name");
        label_prev.setMinWidth(Main.HEIGHT > 700 ? 200 : 100);
        
        field_prev = new TextField();
        field_prev.getStyleClass().add("account-field-value");
        field_prev.setPrefWidth(Main.WIDTH);
        field_prev.setEditable(false);
        field_prev.setFocusTraversable(false);
        
        GridPane grid = new GridPane();
        grid.setPadding(Main.HEIGHT > 700 ? new Insets(10,0,0,0) : new Insets(5,0,0,0));
        grid.setVgap(Main.HEIGHT > 700 ? 10 : 3);
        
        grid.add(label_serialno, 0, 0);
        grid.add(label_name, 0, 1);
        grid.add(label_class, 0, 2);
        grid.add(label_prev, 0, 3);
        grid.add(field_serialno, 1, 0);
        grid.add(field_name, 1, 1, 2, 1);
        grid.add(field_class, 1, 2, 2, 1);
        grid.add(field_prev, 1, 3, 2, 1);
        grid.add(search, 2, 0);
        
        save = new Button("Save");
        save.getStyleClass().add("terminal-button");
        save.setStyle(Main.HEIGHT > 700 ? "-fx-font-size: 30px;" : "-fx-font-size: 17px;");
        save.setPrefWidth(Main.HEIGHT > 700 ? 180 : 100);
        save.setGraphicTextGap(Main.HEIGHT > 700 ? 10 : 3);
        save.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(account != null) saveReading();
            }
        });
        
        button_container1 = new FlowPane();
        button_container1.setAlignment(Pos.CENTER_RIGHT);
        button_container1.setVgap(Main.HEIGHT > 700 ? 10 : 5);
        button_container1.setHgap(Main.HEIGHT > 700 ? 10 : 5);
        button_container1.setPadding(Main.HEIGHT > 700 ? new Insets(10,0,10,0) : new Insets(5,0,5,0));
        button_container1.getChildren().addAll(save);
        
        root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(25) : new Insets(10));
        root.getChildren().add(grid);
        root.getChildren().add(createReadingLayout());
        root.getChildren().add(button_container1);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(goBackToList){
                        Main.ROOT.setCenter(Main.prevScreen);
                        Header.TITLE.setText("Accounts");
                    }else{
                        Main.ROOT.setCenter(new Home().getLayout());
                    }
                }
            }
        });
        
        if(a != null){
            account = a;
            loadAccountData();
            goBackToList = true;
        }
    }
    
    private void saveReading(){
        int prev = Integer.parseInt(account.getLastReading());
        int curr = Integer.parseInt(getMeterReadingValue());
        if(prev >= curr){
            Dialog.showAlert("The latest reading must be greater than the previous reading!");
            return;
        }
        Database sdb = DatabasePlatformFactory.getPlatform().getDatabase();
        Reading r = sdb.findReadingByAccount(account.getObjid());
        
        String objid = UUID.randomUUID().toString();
        String acctid = account.getObjid();
        String value = getMeterReadingValue();
        account.setPresReading(Integer.valueOf(value));
        
        //CHECK FOR WATER-RATES
        Database db2 = DatabasePlatformFactory.getPlatform().getDatabase();
        List<Rule> rlist = Main.RULES;
        if(rlist.isEmpty()){
            Dialog.showError("No rules found!");
            return;
        }
        
        //COMPUTE THE BILL
        int consumption = 0;
        double charge = 0.00;
        DecimalFormat df = new DecimalFormat("####0.00");
        try{
            consumption = Integer.valueOf(account.getPresReading()) -  Integer.valueOf(account.getLastReading());
        }catch(Exception e){
            e.printStackTrace();
            consumption = Integer.valueOf(account.getPresReading());
            System.err.println(e);
        }
        try{
            BillCalculator calc = new BillCalculator();
            charge = calc.compute(account.getInfo(), consumption);
            if(!calc.getError().isEmpty()){
                Dialog.showError(calc.getError());
                return;
            }
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e);
        }
        //GET THE ACCOUNT'S PAYMENTS
        double subtotal = 0.00;
        for(ItemAccount a: account.getItemList()){
            subtotal += a.getAmount();
        }
        double total = subtotal + charge;
        
        account.setConsumption(String.valueOf(consumption));
        account.setAmtDue(df.format(charge));
        account.setTotalDue(df.format(total));
        
        //STORE DATA TO THE DATABASE
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        String datetime = SystemPlatformFactory.getPlatform().getSystem().getDate() + " " + SystemPlatformFactory.getPlatform().getSystem().getTime();
        if(r == null){
            Reading reading = new Reading(objid,acctid,value,account.getConsumption(),account.getAmtDue(),account.getTotalDue(),"OPEN",datetime,account.getBatchId());
            db.createReading(reading);
        }else{
            Reading reading = new Reading(objid,acctid,value,account.getConsumption(),account.getAmtDue(),account.getTotalDue(),"OPEN",datetime,account.getBatchId());
            db.updateMeterReading(reading);
        }
        
        if(!db.getError().isEmpty()) Dialog.showError(db.getError());
        if(db.getError().isEmpty()){
            Dialog.show("Account Information", new AccountDetail(account).getLayout());
            createReading();
        }
    }
    
    private void createReading(){
        account = null;
        reading = null;
        field_serialno.clear();
        field_name.clear();
        field_class.clear();
        field_prev.clear();
        for(RButton r: rb){
            r.setValue(0);
        }
    }
    
    private VBox createReadingLayout(){        
        rb = new RButton[capacity];
        for(int i = 0; i < rb.length; i++){
            rb[i] = new RButton();
        }
        HBox buttons = new HBox(Main.HEIGHT > 700 ? 10 : 5);
        buttons.setId("sheet-buttons");
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(Main.HEIGHT > 700 ? new Insets(20,5,20,5) : new Insets(10,2,10,2));
        for(RButton r: rb){
            buttons.getChildren().add(r.getLayout());
        }
        
        Label label = new Label("Capture the new meter reading by turning the dices above. Swipe-Up the dice to increment its value and Swipe-Down the dice to decrement its value.");
        label.setWrapText(true);
        label.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
        label.setStyle(Main.HEIGHT > 700 ? "-fx-font-size: 18px;" : "-fx-font-size: 12px;");
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        
        VBox root = new VBox(Main.HEIGHT > 700 ? 5 : 2);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(buttons,label);
        return root;
    }
    
    private void initMeterReadingValue(String value){
        String prefix = "";
        for(int i = 0; i < capacity - value.length(); i++ ){
            prefix += "0";
        }
        value = prefix + value;
        for(int i = 0; i < value.length(); i++){
            String chr = String.valueOf(value.charAt(i));
            if(i <= capacity) rb[i].setValue(chr.isEmpty() ? 0 : Integer.parseInt(chr));
        }
    }
    
    private String getMeterReadingValue(){
        String value = "";
        for(int i = 0; i < rb.length; i++){
            value += rb[i].getValue();
        }
        return value;
    }
    
    private void loadAccountData(){
        field_serialno.setText(account.getSerialNo());
        field_name.setText(account.getAcctName());
        field_class.setText(account.getClassificationId());
        field_prev.setText(account.getLastReading());

        Database db2 = DatabasePlatformFactory.getPlatform().getDatabase();
        reading = db2.findReadingByAccount(account.getObjid());
        if(!db2.getError().isEmpty()) Dialog.showError(db2.getError());
        if(reading == null){
            initMeterReadingValue(account.getLastReading());
        }else{
            initMeterReadingValue(reading.getReading());
        }
    }
    
    private VBox createMeterListView(List<Account> accounts){
        ObservableList<Account> data = FXCollections.observableList(accounts);
        ListView<Account> listView = new ListView<Account>();
        listView.setItems(data);
        listView.setStyle("-fx-font-size: 22px;");
        listView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> param) {
                return new MeterCell();
            }
        });
        
        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("terminal-button");
        okBtn.setPrefWidth(Main.HEIGHT > 700 ? 145 : 90);
        okBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                account = listView.getSelectionModel().getSelectedItem();
                if(account != null){
                    field_serialno.setText(account.getSerialNo());
                    field_name.setText(account.getAcctName());
                    field_class.setText(account.getAddress());
                    field_prev.setText(account.getLastReading());
                    
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    reading = db.findReadingByAccount(account.getObjid());
                    if(!db.getError().isEmpty()) Dialog.showError(db.getError());
                    if(reading == null){
                        initMeterReadingValue(account.getLastReading());
                    }else{
                        initMeterReadingValue(reading.getReading());
                    }
                    Dialog.hide();
                }
            }
        });
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("terminal-button");
        cancelBtn.setPrefWidth(Main.HEIGHT > 700 ? 145 : 90);
        cancelBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog.hide();
            }
        });
        
        HBox btnContainer = new HBox(Main.HEIGHT > 700 ? 5 : 2);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.getChildren().addAll(okBtn,cancelBtn);
        
        VBox root = new VBox(Main.HEIGHT > 700 ? 10 : 5);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(20) : new Insets(10));
        root.setStyle("-fx-background-color: white;");
        root.setMinWidth(Main.HEIGHT > 700 ? Main.WIDTH-150 : Main.WIDTH-50);
        root.getChildren().addAll(listView,btnContainer);
        
        return root;
    }
    
    class MeterCell extends ListCell<Account>{
        @Override
        protected void updateItem(Account account, boolean empty){
            super.updateItem(account, empty);
            if(!empty) setText(account.getSerialNo());
        }
    }
    
    public Node getLayout(){
        return root;
    }
    
    private class RButton{
        
        int value = 0;
        private StackPane root;
        private Text text;
        
        public RButton(){
            text = new Text(String.valueOf(value));
            text.getStyleClass().add("rbutton-text");
            
            root = new StackPane();
            root.getStyleClass().add("rbutton-container");
            root.setAlignment(Pos.CENTER);
            root.getChildren().add(text);
            if(Main.HEIGHT > 700){
                root.setMaxSize(100, 150);
                root.setMinSize(100, 150);
            }else{
                root.setMaxSize(40, 65);
                root.setMinSize(40, 65);
            }
            root.setOnSwipeUp(new EventHandler<SwipeEvent>(){
                @Override
                public void handle(SwipeEvent event) {
                    ++value;
                    if(value == 10) value = 0;
                    text.setText(String.valueOf(value));
                }
            });
            
            root.setOnSwipeDown(new EventHandler<SwipeEvent>(){
                @Override
                public void handle(SwipeEvent event) {
                    --value;
                    if(value == -1) value = 9;
                    text.setText(String.valueOf(value));
                }
            });
        }
        
        public Node getLayout(){
            return root;
        }
        
        public void setValue(int value){
            this.value = value;
            text.setText(String.valueOf(value));
        }
        
        public String getValue(){
            return String.valueOf(value);
        }
        
    }
    
}
