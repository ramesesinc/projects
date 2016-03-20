package com.rameses.waterworks.page;

import com.rameses.Main;
import static com.rameses.Main.PRINTER;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Dino
 */
public class AccountDetail {
    
    private VBox root;
    String present="-",consumption="-";
    
    public AccountDetail(Account account){
        Button close = new Button("Close");
        close.getStyleClass().add("terminal-button");
        close.setFocusTraversable(true);
        close.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog.hide();
            }
        });
        
        Button capture = new Button("Capture");
        capture.getStyleClass().add("terminal-button");
        capture.setFocusTraversable(true);
        capture.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog.hide();
                Main.LOG.error("Account Data", account.toString());
                Main.ROOT.setCenter(new ReadingSheet(account).getLayout());
            }
        });
        
        Button print = new Button("Print");
        print.getStyleClass().add("terminal-button");
        print.setFocusTraversable(true);
        print.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(Main.PRINTERNAME.isEmpty()){
                    Dialog.showAlert("No printer selected!");
                    return;
                }
                int i = 0;
                try{
                    i = Integer.valueOf(present);
                }catch(Exception e){ 
                    Dialog.showAlert("Reading data is not yet available!");
                    return;
                }
                account.setPresReading(i);
                if(!Main.PRINTERHANDLER.getError().isEmpty()){
                    Dialog.showError(Main.PRINTERHANDLER.getError());
                    return;
                }
                
                if(Main.PRINTER ==  null){
                    PRINTER = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
                    PRINTER.setPrinter(Main.PRINTERNAME);
                    PRINTER.openBT();
                }
                PRINTER.setError("");
                PRINTER.print(Main.PRINTERHANDLER.getData(account));
                if(!PRINTER.getError().isEmpty()){
                    Dialog.showError(PRINTER.getError());
                    PRINTER.closeBT();
                    PRINTER = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
                    PRINTER.setPrinter(Main.PRINTERNAME);
                    PRINTER.openBT();
                }else{
                    Dialog.hide();
                }
            }
        });
        
        HBox btnContainer = new HBox(Main.HEIGHT > 700 ? 10 : 5);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.setPadding(Main.HEIGHT > 700 ? new Insets(15, 0, 0, 0) : new Insets(5, 0, 0, 0));
        btnContainer.getChildren().addAll(print,capture,close);
        
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        Reading reading = db.findReadingByAccount(account.getObjid());
        if(reading != null){
            present = String.valueOf(Integer.parseInt(reading.getReading()));
            consumption = String.valueOf(Integer.parseInt(present)-Integer.parseInt(account.getLastReading()));
            account.setConsumption(reading.getConsumption());
            account.setAmtDue(reading.getAmtDue());
            account.setTotalDue(reading.getTotalDue());
        }
        
        HBox readingContainer = new HBox(Main.HEIGHT > 700 ? 10 : 5);
        readingContainer.setPadding(Main.HEIGHT > 700 ? new Insets(20, 10, 20, 10) : new Insets(10, 5, 10, 5));
            
        readingContainer.setStyle("-fx-skin: \"com.sun.javafx.scene.control.skin.ButtonSkin\"; -fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        readingContainer.setAlignment(Pos.CENTER);
        readingContainer.getChildren().addAll(
            createColumnField("Previous",account.getLastReading()),
            createColumnField("Present",present),
            createColumnField("Consumption",consumption)
        );
        
        String amtdue = account.getAmtDue()!=null ? account.getAmtDue() : "?";
        String totaldue = account.getTotalDue()!=null ? account.getTotalDue() : "?";
        
        account.setConsumption(consumption);
        account.setAmtDue(amtdue);
        account.setTotalDue(totaldue);
        
        root = new VBox(Main.HEIGHT > 700 ? 15 : 2);
        root.setStyle("-fx-background-color: white;");
        root.setMinWidth(Main.HEIGHT > 700 ? Main.WIDTH-150 : Main.WIDTH-40);
        root.setMaxWidth(Main.HEIGHT > 700 ? Main.WIDTH-150 : Main.WIDTH-8);
        root.setAlignment(Pos.CENTER);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(20) : new Insets(10));
        root.getChildren().add(createDetail("Account No",account.getAcctNo()));
        root.getChildren().add(createDetail("Name",account.getAcctName()));
        root.getChildren().add(createDetail("Address",account.getAddress()));
        root.getChildren().add(createDetail("Mobile No",account.getMobileNo()));
        root.getChildren().add(createDetail("Phone No",account.getPhoneNo()));
        root.getChildren().add(createDetail("Email",account.getEmail()));
        root.getChildren().add(createDetail("Serial No",account.getSerialNo()));
        root.getChildren().add(createDetail("Classification",account.getClassificationId()));
        root.getChildren().add(createDetail("Balance",account.getBalance()));
        root.getChildren().add(createDetail("Penalty",account.getPenalty()));
        root.getChildren().add(createDetail("Other Charge",account.getOtherCharge()));
        root.getChildren().add(createDetail("Amount Due",amtdue));
        root.getChildren().add(createDetail("TOTAL DUE",totaldue));
        root.getChildren().add(readingContainer);
        root.getChildren().add(btnContainer);
        
    }
    
    private HBox createDetail(String key,String value){
        Label label1 = new Label(key);
        label1.setMinWidth(Main.HEIGHT > 700 ? 180 : 90);
        label1.setTextAlignment(TextAlignment.RIGHT);
        
        Label label2 = new Label(":");
        
        label2.setPrefWidth(Main.HEIGHT > 700 ? 30 : 10);
        
        Label label3 = new Label(value);

        if(Main.HEIGHT > 700){
            label1.setStyle("-fx-font-size: 25px;");
            label2.setStyle("-fx-font-size: 25px;");
            label3.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        }else{
            label1.setStyle("-fx-font-size: 14px;");
            label2.setStyle("-fx-font-size: 14px;");
            label3.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        }
        
        HBox container = new HBox(Main.HEIGHT > 700 ? 10 : 5);
        container.getChildren().addAll(label1,label2,label3);
        
        return container;
    }
    
    private VBox createColumnField(String key,String value){
        Label label1 = new Label(key);
        label1.getStyleClass().add("account-field-name");
        label1.getStyleClass().add("account-columnar-name");
        label1.setPrefWidth(Main.HEIGHT > 700 ? 180 : 90);
        
        Label label2 = new Label(value);
        label2.getStyleClass().add("account-field-value");
        label2.getStyleClass().add("account-columnar-value");
        label2.setStyle("-fx-background-color: white; -fx-font-weight: bold;");
        label2.setStyle(Main.HEIGHT > 700 ? "-fx-font-size: 28px;" : "-fx-font-size: 16px;");
        label2.setPrefWidth(Main.HEIGHT > 700 ? 180 : 90);
        
        VBox container = new VBox();
        container.setStyle("-fx-border-width: 1px; -fx-border-color: #7dbce8;");
        container.getChildren().addAll(label1,label2);
        return container;
    }
    
    public Node getLayout(){
        return root;
    }
    
}
