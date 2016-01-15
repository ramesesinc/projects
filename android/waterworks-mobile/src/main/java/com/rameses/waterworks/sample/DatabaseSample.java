package com.rameses.waterworks.sample;

import com.rameses.Main;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DatabaseSample {
    
    private StackPane root;
    private TextField name,value;
    private Button save,reload;
    private TextArea list;
    
    public DatabaseSample(){
        name = new TextField();
        
        value = new TextField();
        
        save = new Button("Save to Setting");
        save.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                db.createSetting(new Setting(name.getText(),value.getText()));
            }
        });
        
        reload = new Button("Reload Setting");
        reload.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                Main.LOG.debug("WATERWORKS TEST", "BEFORE ENTERING DATABASE...");
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                Main.LOG.debug("WATERWORKS TEST", db.toString());
                List<Setting> slist = db.getAllSettings();
                Iterator<Setting> it = slist.iterator();
                while(it.hasNext()){
                    Setting s = it.next();
                    list.appendText(s.getName() + " : " + s.getValue() + "\n");
                }
            }
        });
        
        list = new TextArea();
        
        VBox box = new VBox(10);
        box.getChildren().addAll(name,value,save,list,reload);
        
        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().add(box);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
