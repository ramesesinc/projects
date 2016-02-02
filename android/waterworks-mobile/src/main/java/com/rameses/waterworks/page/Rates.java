package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Formula;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.MobileCalcService;
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
import javafx.scene.layout.VBox;

public class Rates {
    
    private VBox root;
    
    public Rates(){
        Header.TITLE.setText("Water Rates");
        
        TableColumn classCol = new TableColumn("Classification");
        TableColumn varCol = new TableColumn("Variable");
        TableColumn exprCol = new TableColumn("Expression");
        
        classCol.setStyle( "-fx-alignment: TOP-CENTER;");
        varCol.setStyle( "-fx-alignment: TOP-CENTER;");
        
        classCol.setMinWidth(Main.WIDTH * 0.20);
        varCol.setMinWidth(Main.WIDTH * 0.20);
        exprCol.setMinWidth(Main.WIDTH * 0.60);
        
        classCol.setCellValueFactory(new PropertyValueFactory<Formula, String>("classificationId"));
        varCol.setCellValueFactory(new PropertyValueFactory<Formula, String>("var"));
        exprCol.setCellValueFactory(new PropertyValueFactory<Formula, String>("expr"));
        
        TableView<Formula> tableView = new TableView<Formula>();
        tableView.setPrefHeight(Main.HEIGHT);
        tableView.setFocusTraversable(false);
        tableView.getColumns().addAll(classCol,varCol,exprCol);
        
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        List<Formula> list = db.getFormula();
        tableView.setItems(FXCollections.observableArrayList(list));
        
        Button download = new Button("Sync");
        download.getStyleClass().add("terminal-button");
        download.setPrefWidth(180);
        download.setFocusTraversable(true);
        download.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                MobileCalcService service = new MobileCalcService();
                List<Map> list = service.getFormula();
                if(!service.ERROR.isEmpty()){
                    Dialog.showError(service.ERROR);
                    return;
                }
                for(Map m : list){
                    String classificationid = m.get("classificationid") != null ? m.get("classificationid").toString() : "";
                    String var = m.get("var") != null ? m.get("var").toString() : "";
                    String expr= m.get("expr") != null ? m.get("expr").toString() : "";
                    
                    Formula f = new Formula(classificationid,var,expr);
                    Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                    if(!db.formulaExist(f)){
                        db.createFormula(f);
                    }else{
                        db.updateFormula(f);
                    }
                }
                Dialog.showAlert("Download Finish!");
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                List<Formula> lst = db.getFormula();
                tableView.setItems(FXCollections.observableArrayList(lst));
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
