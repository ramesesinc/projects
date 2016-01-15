package com.rameses.waterworks.sample;

import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PrintSample {
    
    private VBox root;
    public static TextArea textarea;
    BluetoothPort port;
    
    public PrintSample(){
        port = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
        
        textarea = new TextArea();
        textarea.setStyle("-fx-font-size: 20px;");
        
        TextArea textarea2 = new TextArea();
        textarea2.setStyle("-fx-font-size: 20px;");
        
        TextField printer = new TextField();
        printer.setStyle("-fx-font-size: 20px;");
        printer.setPromptText("Printer Name");
        
        Button print = new Button("PRINT");
        print.setStyle("-fx-font-size: 18px;");
        print.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                port.print(textarea.getText());
                textarea2.appendText(port.getPrinter() + "\n");
                textarea2.appendText(port.getError());
            }
        });
        
        Button search = new Button("SEARCH");
        search.setStyle("-fx-font-size: 18px;");
        search.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                textarea.clear();
                List<String> printers = port.findDevices();
                for(String printer : printers){
                    textarea.appendText(printer + "\n");
                }
            }
        });
        
        Button open = new Button("OPEN");
        open.setStyle("-fx-font-size: 18px;");
        open.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                port.setPrinter(printer.getText());
                port.openBT();
            }
        });
        
        root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(25));
        root.getChildren().addAll(textarea, printer ,print ,search ,open, textarea2);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
