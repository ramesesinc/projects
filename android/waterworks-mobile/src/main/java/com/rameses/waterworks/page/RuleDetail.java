package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.bean.Rule;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Rameses
 */
public class RuleDetail {
    
    private GridPane root;
    
    public RuleDetail(Rule r){
        Label salience_lb = new Label("SALIENCE");
        salience_lb.setStyle("-fx-font-weight: bold;");
        if(Main.HEIGHT < 700) salience_lb.setStyle("-fx-font-size: 11px;");
        
        Label condition_lb = new Label("CONDITION");
        condition_lb.setStyle("-fx-font-weight: bold;");
        if(Main.HEIGHT < 700) condition_lb.setStyle("-fx-font-size: 11px;");
        
        Label var_lb = new Label("VARIABLE");
        var_lb.setStyle("-fx-font-weight: bold;");
        if(Main.HEIGHT < 700) var_lb.setStyle("-fx-font-size: 11px;");
        
        Label action_lb = new Label("ACTION");
        action_lb.setStyle("-fx-font-weight: bold;");
        if(Main.HEIGHT < 700) action_lb.setStyle("-fx-font-size: 11px;");
        
        TextField salience_tf = new TextField(String.valueOf(r.getSalience()));
        salience_tf.setEditable(false);
        if(Main.HEIGHT < 700) salience_tf.setStyle("-fx-font-size: 11px;");
        
        TextArea condition_ta = new TextArea(r.getCondition());
        condition_ta.setEditable(false);
        if(Main.HEIGHT < 700) condition_ta.setStyle("-fx-font-size: 11px;");
        condition_ta.setPrefHeight(Main.HEIGHT > 700 ? 200 : 100);
        
        TextField var_tf = new TextField(r.getVar());
        var_tf.setEditable(false);
        if(Main.HEIGHT < 700) var_tf.setStyle("-fx-font-size: 11px;");
        
        TextArea action_ta = new TextArea(r.getAction());
        action_ta.setEditable(false);
        if(Main.HEIGHT < 700) action_ta.setStyle("-fx-font-size: 11px;");
        
        root = new GridPane();
        root.setStyle("-fx-background-color: white;");
        root.setMaxWidth(Main.HEIGHT > 700 ? Main.WIDTH - 100 : Main.WIDTH - 50);
        root.setPadding(new Insets(Main.HEIGHT > 700 ? 10 : 5));
        root.setAlignment(Pos.TOP_LEFT);
        root.setVgap(5);
        root.setHgap(5);
        
        root.add(salience_lb, 0, 0);
        root.add(salience_tf, 0, 1);
        
        root.add(condition_lb, 0, 4);
        root.add(condition_ta, 0, 5);
        
        root.add(var_lb, 0, 8);
        root.add(var_tf, 0, 9);
        
        root.add(action_lb, 0, 12);
        root.add(action_ta, 0, 13);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
