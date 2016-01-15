package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PreTerminalRegistration {

    private StackPane root;
    private Label notice;
    private RadioButton register, recover;
    private Button next;

    public PreTerminalRegistration() {
        Header.TITLE.setText("Terminal Registration");

        notice = new Label("This machine is not yet registered. Terminal Registration is required for performing secured transactions. Please choose a type of action below :");
        notice.getStyleClass().add("terminal-notice");
        notice.setWrapText(true);

        ToggleGroup toggle = new ToggleGroup();

        register = new RadioButton("Register new terminal");
        register.setToggleGroup(toggle);
        register.getStyleClass().add("terminal-radio");
        register.setSelected(true);

        recover = new RadioButton("Recover existing terminal");
        recover.setToggleGroup(toggle);
        recover.getStyleClass().add("terminal-radio");

        next = new Button("Next");
        next.getStyleClass().add("terminal-button");
        next.setPrefWidth(130);
        next.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(register.isSelected()){
                    Main.ROOT.setCenter(new TerminalRegistration().getLayout());
                }
                if(recover.isSelected()){
                    Map params = new HashMap();
                    params.put("macaddress", SystemPlatformFactory.getPlatform().getSystem().getMacAddress());
                    
                    TerminalService service = new TerminalService();
                    Map result = service.recover(params);
                    if(!service.ERROR.isEmpty()) Dialog.showError(service.ERROR);
                    if(result != null){
                        Main.ROOT.setCenter(new Login().getLayout());
                        com.rameses.waterworks.bean.Setting setting = new com.rameses.waterworks.bean.Setting("registered","true");
                        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                        if(!db.settingExist(setting)){
                            db.createSetting(setting);
                        }else{
                            db.updateSetting(setting);
                        }
                    }
                }
            }
        });

        HBox bcontainer = new HBox();
        bcontainer.setPadding(new Insets(10, 0, 10, 0));
        bcontainer.getChildren().add(next);

        VBox head = new VBox(20);
        head.setAlignment(Pos.CENTER);
        head.getChildren().addAll(notice);

        VBox body = new VBox(10);
        body.setAlignment(Pos.CENTER_LEFT);
        body.getChildren().addAll(register, recover, bcontainer);

        VBox container = new VBox(20);
        container.getChildren().addAll(head, body);

        root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);
        if(Main.HEIGHT > 800){
            root.setPadding(new Insets(50, 50, 50, 50));
        }else{
            root.setPadding(new Insets(15, 15, 15, 15));
        }
        root.getChildren().add(container);
    }

    public Node getLayout() {
        return root;
    }

}
