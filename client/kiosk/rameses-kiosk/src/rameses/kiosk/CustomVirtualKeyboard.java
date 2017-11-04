/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author louie
 */
public class CustomVirtualKeyboard {
    
//    private BorderPane root;
//    private StackPane root;
    private AnchorPane root;
//    private Canvas overlay;
    private Label feedback;
//    private VBox root;
//    private HTMLInputElement target;
    private HTMLElement target;
    
    final String[][] keys = new String[][] {
        { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "<" },
        { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "-" },
        { "A", "S", "D", "F", "G", "H", "J", "K", "L", ":" },
        { "Z", "X", "C", "V", "B", "N", "M", "CLEAR" },
        { "" }
    };
    
    final KeyCode[][] codes = new KeyCode[][] {
        { KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.DIGIT4, KeyCode.DIGIT5, 
            KeyCode.DIGIT6, KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9, KeyCode.DIGIT0, 
            KeyCode.BACK_SPACE },
        { KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y, KeyCode.U, 
            KeyCode.I, KeyCode.O, KeyCode.P, KeyCode.SUBTRACT },
        { KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H, KeyCode.J, 
            KeyCode.K, KeyCode.L, KeyCode.SEMICOLON },
        { KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N, KeyCode.M, 
            KeyCode.CLEAR },
        { KeyCode.SPACE }
    };
    
//    // Data for regular buttons; split into rows
//    final String[][] unshifted = new String[][] {
//        { "`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=" },
//        { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\" },
//        { "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'" },
//        { "z", "x", "c", "v", "b", "n", "m", ",", ".", "/" } };
//
//    final String[][] shifted = new String[][] {
//        { "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+" },
//        { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "|" },
//        { "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"" },
//        { "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?" } };
//
//    final KeyCode[][] codes = new KeyCode[][] {
//        { KeyCode.BACK_QUOTE, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3,
//            KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7,
//            KeyCode.DIGIT8, KeyCode.DIGIT9, KeyCode.DIGIT0, KeyCode.SUBTRACT,
//            KeyCode.EQUALS },
//        { KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y,
//            KeyCode.U, KeyCode.I, KeyCode.O, KeyCode.P, KeyCode.OPEN_BRACKET,
//            KeyCode.CLOSE_BRACKET, KeyCode.BACK_SLASH },
//        { KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H,
//            KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.SEMICOLON, KeyCode.QUOTE },
//        { KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N,
//            KeyCode.M, KeyCode.COMMA, KeyCode.PERIOD, KeyCode.SLASH } };
//    
//    public void setTarget(HTMLInputElement target) {
//        this.target = target;
//    }
    public void setTarget(HTMLElement target) {
        this.target = target;
    }
    
//    public HTMLInputElement getTarget() {
//        return this.target;
//    }
    public HTMLElement getTarget() {
        return this.target;
    }
    
    public CustomVirtualKeyboard() {
//        this.root = new StackPane();
        root = new AnchorPane();
        root.setId("root");
        root.setPadding(new Insets(10));
//        root.setStyle("-fx-background-color: #bcbdc0");
        
//        overlay = new Canvas(300, 300);
//        overlay.setStyle("-fx-font-family: \"Myriad Pro\"; -fx-font-size: 60;");
//        GraphicsContext gc = overlay.getGraphicsContext2D();
////        gc.setTextAlign(TextAlignment.CENTER);
////        gc.setTextBaseline(VPos.CENTER);
//        gc.fillText(
//            "A", 
//            Math.round(overlay.getWidth()  / 2), 
//            Math.round(overlay.getHeight() / 2)
//        );
        feedback = new Label();
        feedback.setAlignment(Pos.CENTER);
        feedback.setMinWidth(80);
        feedback.setMinHeight(80);
        feedback.relocate(feedback.getMinWidth() * -1, feedback.getMinHeight() * -1);
        feedback.getStyleClass().add("feedback");
//        feedback.setStyle("-fx-font-family: \"Myriad Pro\"; -fx-font-size: 60; -fx-background-color: #bcbdc0;");
        
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: red;");
        
        VBox vbox = new VBox(5);
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);
        AnchorPane.setTopAnchor(vbox, 0.0);
        this.root.getChildren().addAll(vbox, feedback);
        
//        this.root = new BorderPane();
//        GridPane grid = new GridPane();
        vbox.setAlignment(Pos.CENTER);
        
//        this.root.setCenter(this.root);
        
        // build layout
        for (int row = 0; row < keys.length; row++) {
            HBox hbox = new HBox(5);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5);
            vbox.getChildren().add(hbox);

      //      hbox.getChildren().addAll(extraLeftButtons[row]);
            for (int col = 0; col < keys[row].length; col++) {
                hbox.getChildren().add(createButton(keys[row][col], codes[row][col]));
  //            hbox.getChildren().add( createShiftableButton(unshifted[row][k], shifted[row][k], codes[row][k], modifiers, target));
            }
    //      hbox.getChildren().addAll(extraRightButtons[row]);
        }
        
    }
    
    private void println(Object msg) {
        System.out.println(msg);
    }
    
    // Creates a button with mutable text, and registers listener with it
    private Button createButton(final String text, final KeyCode code) {
        StringProperty textProperty = new SimpleStringProperty(code.getName());
        
        final int height = 70;
        final int width = 70;
//        final JDialog feedback = new JDialog();
//        feedback.setUndecorated(true);
//        feedback.setBounds(0, 0, (width + 30), (height + 30));
//        feedback.setAlwaysOnTop(true);
//        
//        JApplet xapplet = new JApplet();
//        feedback.setContentPane(xapplet.getContentPane());
//        
//        JFXPanel panel = new JFXPanel();
//        xapplet.add(panel);
//        
//        BorderPane container = new BorderPane();
//        container.setId("container");
        
//        Label label = new Label(text);
//        label.setStyle("-fx-font-family: \"Myriad Pro\"; -fx-font-size: 50;");
//        container.setCenter(new Label(text));
//        
//        Scene scene = new Scene(container);
//        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/keyboard-feedback.css").toExternalForm());
//        panel.setScene(scene);
        
        final Button button = new Button(text);
        button.setMinSize(width, height);
        button.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
//                println("code " + code);
//                println("mouse pressed " + text + " " + button.getBoundsInParent());
                Bounds b = button.localToScene(button.getBoundsInLocal());
//                println("bounds " + b);
                if (code == KeyCode.SPACE || code == KeyCode.BACK_SPACE || code == KeyCode.CLEAR) {
                    //do nothing
                } else {
                    addFeedback(text, (b.getMinX() - 5), (b.getMinY() - feedback.getHeight() - 1));
                }
//                BigDecimal x = new BigDecimal(b.getMinX() + "").setScale(0, BigDecimal.ROUND_HALF_UP);
//                BigDecimal y = new BigDecimal(b.getMinY() + "").setScale(0, BigDecimal.ROUND_HALF_UP);
                
//                println("x " + x.intValue() + " y " + y.intValue());
                
//                feedback.setLocation((x.intValue() - 15), (y.intValue() - feedback.getHeight() - 1));
//                feedback.setVisible(true);
                
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        button.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
//                println("mouse released " + text);
//                feedback.setVisible(false);
                removeFeedback();
            }
        });
        if (code == KeyCode.SPACE) {
            button.setMinWidth(550);
        }
        
        // Important not to grab the focus from the target:
        button.setFocusTraversable(false);

        // Add a style class for css:
//        button.getStyleClass().add("virtual-keyboard-button");
//        button.setStyle("-fx-font-size: 18px; -fx-font-family: \"Myriad Pro\"");
        button.setOnAction(new KeyboardButtonEventListener(button, code, textProperty));
    
        return button;
    }
    
    private void addFeedback(String text, double x, double y) {
        if (root != null && feedback != null) {
            if (root.getChildren().contains(feedback)) {
                root.getChildren().remove(feedback);
            }
            
            feedback.setText(text);
            feedback.relocate(x, y);
//            feedback.setTranslateX(x);
//            feedback.setTranslateY(y);
            root.getChildren().add(feedback);
        }
    }
    
    private void removeFeedback() {
        if (root != null && feedback != null && root.getChildren().contains(feedback)) {
            root.getChildren().remove(feedback);
        }
    }

//    private void addOverlay(String text) {
//        if (!root.getChildren().contains(overlay)) {
//            Label label = new Label(text);
////            overlay.setCenter(label);
//            root.getChildren().add(overlay);
//        }
//    }
//    
//    private void removeOverlay() {
//        if (root.getChildren().contains(overlay)) {
//            root.getChildren().remove(overlay);
//        }
//    }
    
    public Node view() {
        return root ;
    }
  
  private class KeyboardButtonEventListener implements EventHandler {

        private CustomVirtualKeyboard root = CustomVirtualKeyboard.this;
        private Button button;
        private KeyCode code;
        private StringProperty textProperty;
      
        public KeyboardButtonEventListener(Button button, KeyCode code, StringProperty textProperty) {
            this.button = button;
            this.code = code;
            this.textProperty = textProperty;
        }
        
//        public KeyboardButtonEventListener(String value) {
//            this.value = value;
//        }
        
        @Override
        public void handle(Event t) {
//            println("action");
//            println("bounds " + button.getBoundsInParent());
//            HTMLInputElement target = root.getTarget();
            HTMLElement target = root.getTarget();
            if (target != null) {
                
                String character;
                if (textProperty.get().length() == 1) {
                    character = textProperty.get();
                } else {
                    character = KeyEvent.CHAR_UNDEFINED;
                }
                
                HTMLInputElement input = null;
                HTMLTextAreaElement textarea = null;
                
                String text = "";
                if (target instanceof HTMLInputElement) {
                    input = (HTMLInputElement) target;
                    text = input.getValue();
                } else if (target instanceof HTMLTextAreaElement) {
                    textarea = (HTMLTextAreaElement) target;
                    text = textarea.getValue();
                }
                
//                String text = target.getValue();
                if (text == null) text = "";
                
//                println("character-> " + character);
//                println("undefined-> " + (character != KeyEvent.CHAR_UNDEFINED));
                if (character != KeyEvent.CHAR_UNDEFINED) {
                    text += character;
                    
                } else if (character == KeyEvent.CHAR_UNDEFINED) {
//                    println("code " + this.code + " text " + text);
                    if (this.code == KeyCode.BACK_SPACE) {
//                        println("backspace " + text.length());
                        if (text.length() > 0) {
                            text = text.substring(0, text.length() - 1);
                        }
                    } else if (this.code == KeyCode.CLEAR) {
//                        println("clear");
                        text = "";
                    } else if (this.code == KeyCode.SPACE) {
//                        println("space");
                        text += " ";
                    } else if (this.code == KeyCode.SUBTRACT) {
                        text += "-";
                    } else if (this.code == KeyCode.SEMICOLON) {
                        text += ":";
                    }
                }
                
                if (input != null) {
                    input.setValue(text);
                    input.focus();
                }
                if (textarea != null) {
                    textarea.setValue(text);
                    textarea.focus();
                }
//                target.setValue(text);
//                target.click();
//                println("character " + character);
                
                
//                final KeyEvent keyPressEvent = createKeyEvent(button, target, KeyEvent.KEY_PRESSED, character, code, modifiers);
//                target.fireEvent(keyPressEvent);
//                final KeyEvent keyReleasedEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_RELEASED, character, code, modifiers);
//                target
                
//                println("code " + code);
//                String text = target.getValue();
//                if (text == null) text = "";
//                text += this.value;
//                target.setValue(text);
            }
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private void println(String str) {
            System.out.println(str);
        }

  }
}
