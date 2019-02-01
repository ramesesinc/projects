package rameses.kiosk;

import java.awt.Dialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class SuspendDialog {
    
    
    private JDialog dialog;
    private boolean isShowing = false;
    private boolean isActive = false;
    private Button btnReturnToMain = new Button("Return to home");
    private Rectangle window;
        
    public SuspendDialog() {
        this(null);
    }
    
    public SuspendDialog( EventHandler<ActionEvent> returnToMainAction ) {
        dialog = new JDialog();
        dialog.setTitle("Suspend ...");
        dialog.setAlwaysOnTop(true);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setFocusableWindowState(false);
        dialog.setFocusable(false);
        dialog.setFocusTraversalKeysEnabled(false);
        dialog.setResizable(false);
        dialog.setSize(400, 210);
        dialog.setLocation(300, 200);
        
        
        GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = d.getDefaultConfiguration();
        window = gc.getBounds();
        
        JApplet applet = new JApplet();
        dialog.setContentPane(applet.getContentPane());
        
        JFXPanel panel = new JFXPanel();
        applet.add(panel);
        
        BorderPane container = new BorderPane();
        
        VBox content = new VBox();
        content.setPadding(new Insets(25,25,10,25));
        Label text = new Label("Session Timeout");
        text.setFont(Font.font("", FontWeight.NORMAL, 18));
//        System.out.println("font " + text.getFont());
        content.getChildren().add(text);
        container.setCenter(content);
        
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setSpacing(10);
        toolbar.minHeight(30);
        container.setBottom(toolbar);
        
//        System.out.println("buttong font " + btnReturnToMain.getFont());
        btnReturnToMain.setPrefSize(150, 40);
        btnReturnToMain.setOnAction(returnToMainAction);
        toolbar.getChildren().add(btnReturnToMain);
//        VBox container = new VBox();
//        
////        GridPane grid = new GridPane();
////        grid.setAlignment(Pos.CENTER);
////        grid.setHgap(10);
////        grid.setVgap(10);
////        grid.setPadding(new Insets(5, 5, 5, 5));
//        
//        Text text = new Text("Session expired.");
//        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//        container.getChildren().add(text);
//        
//        HBox toolbar = new HBox();
//        toolbar.setAlignment(Pos.CENTER);
//        btnReturnToMain.setOnAction(returnToMainAction);
//        toolbar.getChildren().add(btnReturnToMain);
//        container.getChildren().add(toolbar);
//        grid.add(text, 0, 0, 2, 1);

        
        panel.setScene(new Scene(container));
    }
    
    public void show() {
        if (dialog != null && dialog.isVisible() == false) {
            int height = (window.height - dialog.getHeight())/2;
            int width = (window.width - dialog.getWidth())/2;
            
//            dialog.setLocation(300, 200);
            dialog.setLocation(width, height);
            dialog.setVisible(true);
            this.isShowing = true;
        }
    }
    
    public void hide() {
        if (dialog != null && dialog.isVisible() == true) {
            dialog.setVisible(false);
            this.isShowing = false;
        }
    }
    
    public void setActive( boolean isActive ) {
        this.isActive = isActive;
    }
    
    public boolean isShowing() {
        return this.isShowing;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
}
