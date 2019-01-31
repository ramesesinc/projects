/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk.virtualkeyboard;

import java.awt.Dialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import netscape.javascript.JSObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTextAreaElement;
import rameses.kiosk.RamesesKiosk;
import rameses.kiosk.interfaces.VirtualKeyboardCallback;
import rameses.kiosk.listener.InactivityListener;

/**
 *
 * @author louie
 */
public class VirtualKeyboard {
    
    private RamesesKiosk kiosk;
    private AnchorPane root;
    private Label feedback;
    private HTMLElement target;
    private JDialog dialog;
    private VirtualKeyboardCallback vkCallback;
    
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
    
    public void setTarget(HTMLElement target) {
        this.target = target;
    }
    
    public HTMLElement getTarget() {
        return this.target;
    }
    
    public RamesesKiosk getKiosk() {
        return this.kiosk;
    }
    
    public VirtualKeyboard( RamesesKiosk kiosk, VirtualKeyboardCallback vkCallback ) {
//        this.root = new StackPane();
        this.kiosk = kiosk;
        this.vkCallback = vkCallback;
        
        dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setFocusableWindowState(false);
        dialog.setFocusable(false);
        dialog.setFocusTraversalKeysEnabled(false);
        dialog.setResizable(false);
        dialog.setSize(900, 600);
        
        JApplet applet = new JApplet();
        dialog.setContentPane( applet.getContentPane() );
        
        JFXPanel panel = new JFXPanel();
        applet.add( panel );
        
        root = createKeyboard();
        Scene scene = new Scene( root );
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/keyboard-theme.css").toExternalForm());
        
        panel.setScene(scene);
    }
    
    private AnchorPane createKeyboard() {

       AnchorPane container = new AnchorPane();
       container.setId("root");
       container.setPadding(new Insets(10));

       feedback = createFeedback();

       VBox vbox = new VBox(5);
       AnchorPane.setBottomAnchor(vbox, 0.0);
       AnchorPane.setLeftAnchor(vbox, 0.0);
       AnchorPane.setRightAnchor(vbox, 0.0);
       AnchorPane.setTopAnchor(vbox, 0.0);
       container.getChildren().addAll(vbox, feedback);

       vbox.setAlignment(Pos.CENTER);

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

       return container;
    }
    
    private Label createFeedback() {
        Label label = new Label();
        
//        feedback = new Label();
        label.setAlignment(Pos.CENTER);
        label.setMinSize(80, 80);
//        label.setMinWidth(80);
//        label.setMinHeight(80);
        label.relocate(label.getMinWidth() * -1, label.getMinHeight() * -1);
        label.getStyleClass().add("feedback");
//        label.setVisible( false );
        
        return label;
    }
    
    private void println(Object msg) {
        System.out.println(msg);
    }
    
    // Creates a button with mutable text, and registers listener with it
    private Button createButton(final String text, final KeyCode code) {
        StringProperty textProperty = new SimpleStringProperty(code.getName());
        
        final int height = 70;
        final int width = 70;
        
        final Button button = new Button(text);
        button.setMinSize(70, 70);
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
        
        button.setFocusTraversable(false);
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
            root.getChildren().add(feedback);
        }
    }
    
    private void removeFeedback() {
        if (root != null && feedback != null && root.getChildren().contains(feedback)) {
            root.getChildren().remove(feedback);
        }
    }
    
    public synchronized void show() {
//        show(0, 0);
        
        
        GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = d.getDefaultConfiguration();
        Rectangle r = gc.getBounds();

        int x = 0, y = 0;        
        if (dialog != null) {
            x = (r.width - dialog.getWidth()) / 2;
            y = r.height - dialog.getHeight() - 30;
        } 
        
        show( x, y );
    }
    
    public synchronized void show( int x, int y ) {
        if (dialog == null) return;
        if (dialog.isVisible()) return;
        
        if (vkCallback != null) {
            vkCallback.show();
        }
        
        dialog.setLocation( x, y );
        dialog.setVisible( true );
    }
    
    public synchronized void hide() {
        if (dialog == null) return;
        if (!dialog.isVisible()) return;
        
        if (vkCallback != null) {
            vkCallback.hide();
        }
        
        dialog.setVisible( false );
    }
    
    public int getWidth() {
        if (dialog != null) {
            return dialog.getWidth();
        }
        return 0;
    }
    
    public int getHeight() {
        if (dialog != null) {
            return dialog.getHeight();
        }
        return 0;
    }
  
    private class KeyboardButtonEventListener implements EventHandler {

        private VirtualKeyboard root = VirtualKeyboard.this;
        private Button button;
        private KeyCode code;
        private StringProperty textProperty;

        public KeyboardButtonEventListener(Button button, KeyCode code, StringProperty textProperty) {
            this.button = button;
            this.code = code;
            this.textProperty = textProperty;
        }

        @Override
        public void handle(Event t) {
    //            println("action");
    //            println("bounds " + button.getBoundsInParent());
    //            HTMLInputElement target = root.getTarget();

            InactivityListener inactivityListener = kiosk.getInactivityListener();
            if (inactivityListener != null) {
                ActionEvent ae = new ActionEvent(inactivityListener.getWindow(), ActionEvent.ACTION_PERFORMED, "");
                inactivityListener.eventDispatched( ae );
            }
            HTMLElement target = root.getTarget();
            if (target != null) {
                WebEngine webEngine = root.getKiosk().getWebEngine();
                NamedNodeMap attribs = target.getAttributes();

                String targetId = "";                
                int selectionStart = 0, selectionEnd = 0;
                
                org.w3c.dom.Node n = attribs.getNamedItem("id");
                if (n != null) {
                    targetId = n.getNodeValue();
                    
                    JSObject result = (JSObject) webEngine.executeScript("WebViewUtil.getSelectionRange('" + targetId + "')");

                    if (result != null) {
                        selectionStart = Integer.valueOf(result.getMember("selectionStart").toString());
                        selectionEnd = Integer.valueOf(result.getMember("selectionEnd").toString());
                    }
                }
                /*
                org.w3c.dom.Node n = attribs.getNamedItem("allowjavafxcall");

                int selectionStart = 0, selectionEnd = 0;

                String targetId = ""; 
                if (n != null) {
                    targetId = attribs.getNamedItem("id").getNodeValue();
                    JSObject result = (JSObject) webEngine.executeScript("WebViewUtil.getSelectionRange('" + targetId + "')");

                    if (result != null) {
                        selectionStart = Integer.valueOf(result.getMember("selectionStart").toString());
                        selectionEnd = Integer.valueOf(result.getMember("selectionEnd").toString());
                    }
                }
                */

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

                if (text == null) text = "";

                String textCopy = text;
                int textLength = text.length();
                int selectionLength = selectionEnd - selectionStart;
                if (character != KeyEvent.CHAR_UNDEFINED) {
                    text = textCopy.substring(0, selectionStart);
                    text += character;
                    text += textCopy.substring(selectionEnd, textLength);
                    selectionStart++;

                } else if (character == KeyEvent.CHAR_UNDEFINED) {

                    if (this.code == KeyCode.BACK_SPACE) {

                        if (text.length() > 0 && selectionStart > 0) {

                            if (selectionLength == 0) {
                                if (selectionStart == textLength) {
                                    selectionStart = textLength - 1;
                                    text = text.substring(0, selectionStart);
                                } else {
                                    text = textCopy.substring(0, selectionStart - 1);
                                    text += textCopy.substring(selectionStart, textLength);
                                    selectionStart--;
                                }
                            } else if (selectionLength > 0) {
                                text = textCopy.substring(0, selectionStart);
                                text += textCopy.substring(selectionEnd, textLength);
                            }

                        }
                    } else if (this.code == KeyCode.CLEAR) {
                        text = "";
                        selectionStart = 0;
                    } else {
                        String addText = "";
                        if (this.code == KeyCode.SPACE) {
      //                            text += " ";
                            addText = " ";
                        } else if (this.code == KeyCode.SUBTRACT) {
      //                            text += "-";
                            addText = "-";
                        } else if (this.code == KeyCode.SEMICOLON) {
      //                            text += ":";
                            addText = ":";
                        } else {
                            selectionStart--;
                        }

                        text = textCopy.substring(0, selectionStart);
                        text += addText;
                        text += textCopy.substring(selectionEnd, textLength);

                        selectionStart++;
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

                if (targetId != null && !targetId.equals("")) {
                    webEngine.executeScript("WebViewUtil.setCursorPosition('" + targetId + "'," + selectionStart + ")");
                }
            }
        }

        private void println(String str) {
            System.out.println(str);
        }

    }
}
