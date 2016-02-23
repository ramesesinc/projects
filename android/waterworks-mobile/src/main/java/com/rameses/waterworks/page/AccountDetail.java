package com.rameses.waterworks.page;

import com.rameses.Main;
import static com.rameses.Main.PRINTER;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.printer.PrinterHandler;
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
        close.setPrefWidth(120);
        close.setFocusTraversable(true);
        close.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog.hide();
            }
        });
        
        Button print = new Button("Print");
        print.getStyleClass().add("terminal-button");
        print.setPrefWidth(120);
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
                PrinterHandler handler = new PrinterHandler(account);
                
                if(Main.PRINTER ==  null){
                    PRINTER = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
                    PRINTER.setPrinter(Main.PRINTERNAME);
                    PRINTER.openBT();
                }
                PRINTER.setError("");
                PRINTER.print(handler.getData());
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
        
        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.setPadding(new Insets(15, 0, 0, 0));
        btnContainer.getChildren().addAll(print,close);
        
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        Reading reading = db.findReadingByAccount(account.getObjid());
        if(reading != null){
            present = String.valueOf(Integer.parseInt(reading.getReading()));
            consumption = String.valueOf(Integer.parseInt(present)-Integer.parseInt(account.getLastReading()));
            account.setConsumption(reading.getConsumption());
            account.setAmtDue(reading.getAmtDue());
            account.setTotalDue(reading.getTotalDue());
        }
        
        HBox readingContainer = new HBox(10);
        if(Main.HEIGHT > 800){
            readingContainer.setPadding(new Insets(20, 10, 20, 10));
        }else{
            readingContainer.setPadding(new Insets(10, 5, 10, 5));
        }
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
        
        root = new VBox(15);
        root.setStyle("-fx-background-color: white;");
        if(Main.HEIGHT > 800){
            root.setMinWidth(Main.WIDTH-150);
        }else{
            root.setMinWidth(Main.WIDTH-40);
        }
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
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
        label1.setPrefWidth(180);
        label1.setTextAlignment(TextAlignment.RIGHT);
        
        Label label2 = new Label(":");
        
        label2.setPrefWidth(30);
        
        Label label3 = new Label(value);

        if(Main.HEIGHT > 800){
            label1.setStyle("-fx-font-size: 25px;");
            label2.setStyle("-fx-font-size: 25px;");
            label3.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        }else{
            label1.setStyle("-fx-font-size: 19px;");
            label2.setStyle("-fx-font-size: 19px;");
            label3.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        }
        
        HBox container = new HBox(10);
        container.getChildren().addAll(label1,label2,label3);
        
        return container;
    }
    
    private VBox createColumnField(String key,String value){
        Label label1 = new Label(key);
        label1.getStyleClass().add("account-field-name");
        label1.getStyleClass().add("account-columnar-name");
        label1.setPrefWidth(180);
        
        Label label2 = new Label(value);
        label2.getStyleClass().add("account-field-value");
        label2.getStyleClass().add("account-columnar-value");
        label2.setStyle("-fx-background-color: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        label2.setPrefWidth(180);
        
        VBox container = new VBox();
        container.setStyle("-fx-border-width: 1px; -fx-border-color: #7dbce8;");
        container.getChildren().addAll(label1,label2);
        return container;
    }
    
    public Node getLayout(){
        return root;
    }
    
}
