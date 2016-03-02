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
        Label salience_lb = new Label("Salience");
        
        Label condition_lb = new Label("Condition");
        
        Label var_lb = new Label("Variable");
        
        Label action_lb = new Label("Action");
        
        TextField salience_tf = new TextField(String.valueOf(r.getSalience()));
        salience_tf.setEditable(false);
        
        TextArea condition_ta = new TextArea(r.getCondition());
        condition_ta.setEditable(false);
        
        TextField var_tf = new TextField(r.getVar());
        var_tf.setEditable(false);
        
        TextArea action_ta = new TextArea(r.getAction());
        action_ta.setEditable(false);
        
        root = new GridPane();
        root.setStyle("-fx-background-color: white;");
        root.setMaxWidth(Main.HEIGHT > 800 ? Main.WIDTH - 100 : Main.WIDTH - 50);
        root.setPadding(new Insets(Main.HEIGHT > 800 ? 10 : 5));
        root.setAlignment(Pos.TOP_LEFT);
        root.setVgap(10);
        root.setHgap(10);
        
        root.add(salience_lb, 0, 0);
        root.add(condition_lb, 0, 1);
        root.add(var_lb, 0, 2);
        root.add(action_lb, 0, 3);
        
        root.add(salience_tf, 1, 0);
        root.add(condition_ta, 1, 1);
        root.add(var_tf, 1, 2);
        root.add(action_ta, 1, 3);
    }
    
    public Node getLayout(){
        return root;
    }
    
}
