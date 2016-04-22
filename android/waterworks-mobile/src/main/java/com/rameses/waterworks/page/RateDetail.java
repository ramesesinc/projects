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
public class RateDetail {
    
    private GridPane root;
    
    public RateDetail(Rule r){
        Label salience_lb = new Label("SALIENCE");
        salience_lb.getStyleClass().add("login-label");
        salience_lb.setStyle("-fx-font-weight: bold;");
        
        Label condition_lb = new Label("CONDITION");
        condition_lb.getStyleClass().add("login-label");
        condition_lb.setStyle("-fx-font-weight: bold;");
        
        Label var_lb = new Label("VARIABLE");
        var_lb.getStyleClass().add("login-label");
        var_lb.setStyle("-fx-font-weight: bold;");
        
        Label action_lb = new Label("ACTION");
        action_lb.getStyleClass().add("login-label");
        action_lb.setStyle("-fx-font-weight: bold;");
        
        TextField salience_tf = new TextField(String.valueOf(r.getSalience()));
        salience_tf.getStyleClass().add("login-label");
        salience_tf.setPrefWidth(Main.WIDTH);
        salience_tf.setEditable(false);
        
        TextField condition_ta = new TextField(r.getCondition());
        condition_ta.getStyleClass().add("login-label");
        condition_ta.setPrefWidth(Main.WIDTH);
        condition_ta.setEditable(false);
        
        TextField var_tf = new TextField(r.getVar());
        var_tf.getStyleClass().add("login-label");
        var_tf.setPrefWidth(Main.WIDTH);
        var_tf.setEditable(false);
        
        TextArea action_ta = new TextArea(r.getAction());
        action_ta.getStyleClass().add("login-label");
        action_ta.setPrefWidth(Main.WIDTH);
        action_ta.setEditable(false);
        
        root = new GridPane();
        root.setStyle("-fx-background-color: white;");
        root.setMaxWidth(Main.HEIGHT > 700 ? Main.WIDTH - 100 : Main.WIDTH - 20);
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
