package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.TerminalService;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TerminalRegistration {
    
    private StackPane root;
    private Label notice;
    private TextField key,user;
    private Button submit;
    private Text error;
    
    public TerminalRegistration(){
        Header.TITLE.setText("New Terminal Registration");
        
        notice = new Label("Please fill in the information below. Contact helpdesk for assistance.");
        notice.getStyleClass().add("terminal-notice");
        notice.setWrapText(true);
        
        Label key_lb = new Label("Terminal Key :");
        key_lb.getStyleClass().add("terminal-label");
        
        Label user_lb = new Label("Registered By :");
        user_lb.getStyleClass().add("terminal-label");
        
        key = new TextField();
        key.getStyleClass().add("terminal-field");
        key.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        key.setFocusTraversable(false);
        
        user = new TextField();
        user.getStyleClass().add("terminal-field");
        user.setPrefWidth(Main.HEIGHT > 700 ? 350 : 180);
        user.setFocusTraversable(false);
        
        submit = new Button("Submit");
        submit.getStyleClass().add("terminal-button");
        submit.setPrefWidth(130);
        submit.setFocusTraversable(true);
        submit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                String terminalid = key.getText();
                String registeredby = user.getText();
                String macaddress = SystemPlatformFactory.getPlatform().getSystem().getMacAddress();
                if(terminalid.isEmpty() || registeredby.isEmpty()) return;

                Map params = new HashMap();
                params.put("terminalid",terminalid);
                params.put("registeredby", registeredby);
                params.put("macaddress",macaddress);
                
                TerminalService service = new TerminalService();
                Map result = service.register(params);
                if(!service.ERROR.isEmpty()) Dialog.showError(service.ERROR);
                if(result != null) Main.ROOT.setCenter(new Login().getLayout());
            }
        });
        
        HBox bcontainer = new HBox(5);
        bcontainer.setAlignment(Pos.CENTER_RIGHT);
        bcontainer.getChildren().addAll(submit);
        
        VBox head = new VBox();
        head.setAlignment(Pos.CENTER);
        head.getChildren().add(notice);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(Main.HEIGHT > 700 ? 10 : 5);
        grid.setHgap(Main.HEIGHT > 700 ? 20 : 10); 
        grid.add(key_lb, 0, 0);
        grid.add(key, 1, 0);
        grid.add(user_lb, 0, 1);
        grid.add(user, 1, 1);
        grid.add(bcontainer, 1, 4);
        
        VBox container = new VBox(Main.HEIGHT > 700 ? 30 : 15);
        container.setAlignment(Pos.TOP_CENTER);
        container.getChildren().addAll(head,grid);
        
        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(Main.HEIGHT > 700 ? new Insets(50, 50, 50, 50) : new Insets(15, 15, 15, 15));
        root.getChildren().add(container);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                    Main.ROOT.setCenter(new PreTerminalRegistration().getLayout());
                }
            }
        });
    }
    
    public Node getLayout(){
        return root;
    }

}
