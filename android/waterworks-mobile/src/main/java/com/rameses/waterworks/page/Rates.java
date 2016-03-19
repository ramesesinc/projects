package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.MobileRuleService;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Rates {
    
    private VBox root;
    
    public Rates(){
        Header.TITLE.setText("Water Rates");
        
        TableColumn salienceCol = new TableColumn("Salience");
        TableColumn conditionCol = new TableColumn("Condition");
        TableColumn varCol = new TableColumn("Variable");
        TableColumn actionCol = new TableColumn("Action");
        
        salienceCol.setStyle( "-fx-alignment: TOP-CENTER;");
        varCol.setStyle( "-fx-alignment: TOP-CENTER;");
        
        salienceCol.setMinWidth(Main.WIDTH * 0.10);
        conditionCol.setMinWidth(Main.WIDTH * 0.35);
        varCol.setMinWidth(Main.WIDTH * 0.10);
        actionCol.setMinWidth(Main.WIDTH * 0.45);
        
        salienceCol.setCellValueFactory(new PropertyValueFactory<Rule, Integer>("salience"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<Rule, String>("condition"));
        varCol.setCellValueFactory(new PropertyValueFactory<Rule, String>("var"));
        actionCol.setCellValueFactory(new PropertyValueFactory<Rule, String>("action"));
        
        TableView<Rule> tableView = new TableView<Rule>();
        tableView.setPrefHeight(Main.HEIGHT);
        tableView.setFocusTraversable(false);
        tableView.getColumns().addAll(salienceCol,conditionCol,varCol,actionCol);
        
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        List<Rule> list = db.getRules();
        tableView.setItems(FXCollections.observableArrayList(list));
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Rule r = tableView.getSelectionModel().getSelectedItem();
                    if(r != null){
                        Node node = new RuleDetail(r).getLayout();
                        Dialog.show("Rule Information", node);
                    }
                }
            }
        });
        
        Button download = new Button("Sync");
        download.getStyleClass().add("terminal-button");
        download.setPrefWidth(180);
        download.setFocusTraversable(true);
        download.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                MobileRuleService service = new MobileRuleService();
                List<Map> list = service.getRules();
                if(!service.ERROR.isEmpty()){
                    Dialog.showError(service.ERROR);
                    return;
                }
                if(!list.isEmpty()){
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    db.clearRule();
                }
                for(Map m : list){
                    int salience = m.get("salience") != null ? Integer.parseInt(m.get("salience").toString()) : 0;
                    String condition = m.get("condition") != null ? m.get("condition").toString() : "";
                    String var= m.get("var") != null ? m.get("var").toString() : "";
                    String action= m.get("action") != null ? m.get("action").toString() : "";
                    
                    Rule rule = new Rule(salience,condition,var,action);
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    db.createRule(rule);
                }
                Dialog.showAlert("Sync Finish!");
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                Main.loadRules();
                tableView.setItems(FXCollections.observableArrayList(Main.RULES));
            }
        });
       
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(0,0,10,0));
        root.getChildren().addAll(tableView, download);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
    }
    
    public Node getLayout(){
        return root;
    }
    
}
