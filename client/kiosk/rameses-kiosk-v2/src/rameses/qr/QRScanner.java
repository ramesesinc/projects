/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.qr;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import rameses.kiosk.interfaces.QRCallback;

/**
 *
 * @author louie
 */
public class QRScanner {
    
    private JDialog dialog;
    private StringBuilder sb = new StringBuilder();
    private QRCallback qrCallback;
    private HBox buttons = new HBox(5);
    private ImageView qr_icon;
    private Label header;
    private BorderPane contentPane = new BorderPane();
    
    private Runnable contentRunnable = new Runnable() {
        
        public void run() {
            
            if (dialog != null) {
                addContent();
                
                Container container = dialog.getContentPane();
                container.revalidate();
                container.repaint();
            }
        }
        
    };
    
    /*
    private final ActionListener countDownListener = new ActionListener() {
        public void actionPerformed( ActionEvent event ) {
            
            println("counter-> " + countDownCounter);
            countDownCounter -= 1000;
            if (countDownLabel != null) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        countDownLabel.setText( getCountDownString() );
                    }
                });
            }
            if (countDownCounter <= 0) {
                println("count down stop");
                stop();
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        println("allow confirm for rescan-> " + allowConfirmForRescan);
//                        if (allowConfirmForRescan == true) {
//                            if (confirmDialog != null) {
//                                showConfirmDialog();
////                                confirmDialog.setVisible( true );
////                                int value = ((Integer)optionPane.getValue()).intValue();
////                                if (value == JOptionPane.YES_OPTION) {
////                                    println("yes");
//////                                    setLabel("Good.");
////                                } else if (value == JOptionPane.NO_OPTION) {
////                                    println("no");
//////                                    setLabel("Try using the window decorations "
//////                                             + "to close the non-auto-closing dialog. "
//////                                             + "You can't!");
////                                }
//                            }
//                            
////                            int res = JOptionPane.showConfirmDialog( dialog, "Do you want to re-scan qr?", "", JOptionPane.YES_NO_OPTION);
////                            println("result-> " + res);
////                            switch ( res ) {
////                                case 0: 
////                                    println("res 0");
////                                    start(); 
////                                    break;
////                                case 1: 
////                                    println("res 1");
////                                    hide(); 
////                                    break;
////                            }
//                        } else {
//                            println("hide");
//                            hide();
//                        }
//                    }
//                });
            }
        }
    };
    */
    
    public QRScanner( QRCallback qrCallback ) {
        this.qrCallback = qrCallback;
        
        /*
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
        */
        
        dialog = new JDialog();
        dialog.setTitle("Scan QR");
        dialog.setAlwaysOnTop( true );
        dialog.setModalityType( Dialog.ModalityType.MODELESS );
        dialog.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        dialog.setFocusableWindowState( false );
        dialog.setFocusable( false );
        dialog.setFocusTraversalKeysEnabled( false );
        dialog.setResizable( false );
        dialog.setUndecorated( true );
                
        GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = d.getDefaultConfiguration();
        Rectangle r = gc.getBounds();
        
        dialog.setSize( r.width, r.height );
//        dialog.setSize( 300, 450 );
        
        
        JApplet applet = new JApplet();
        dialog.setContentPane( applet.getContentPane() );
        
        JFXPanel panel = new JFXPanel();
        applet.add( panel );
        
        contentPane = createQrScanner();
        Scene scene = new Scene( contentPane );
        scene.setFill( Color.WHITE );
        /*
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                println("key released " + t.getCode());
                String val = t.getText();
                if (val != null) {  
                    if (t.getCode().equals(KeyCode.ENTER)) {
                        try {
                            process( sb.toString() );
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                        sb = new StringBuilder();
                    } else {
                        sb.append( val );
                    }
                }
                
                t.consume();
            }
        });
        */
        panel.setScene(scene);

        /*
        createConfirmDialog();
        countDownTimer = new Timer( 1000, countDownListener );
        */
        
        /*
        BorderPane timerPanel = new BorderPane();
        HBox timer = new HBox( 5 );
        timer.setMinWidth( 90 );
        timer.setPadding( new Insets(5, 10, 5, 5) );
        timer.setStyle("-fx-font-size: 18px; -fx-border-color: #000;");
        
        timer.getChildren().add( new Label("Timer: ") );
        
        countDownLabel = new Label( getCountDownString() );
        countDownLabel.setMinWidth( 30 );
        countDownLabel.setAlignment(Pos.CENTER_RIGHT);
        timer.getChildren().add( countDownLabel );
        
        timerPanel.setRight( timer );
        
        container.setTop( timerPanel );
        */
        
    } 
    
    private void addContent() {
        if (contentPane != null) {
            
            VBox content = new VBox(5);
            content.setPadding( new Insets(5) );
            content.setAlignment( Pos.CENTER );
            content.setStyle( "-fx-background-color: #fff;" );

            header = new Label("Scan QR Code");
            header.setStyle("-fx-font-size: 30;");

            qr_icon = new ImageView(new Image(getClass().getClassLoader().getResource("resources/images/qr-scan.gif").toExternalForm()));

            qr_icon.setFitHeight( 400 );
            qr_icon.setFitWidth( 250 );

    //        container.setCenter(qr_icon);        

            buttons = new HBox(5);
            buttons.setPadding( new Insets(10) );
            buttons.setAlignment( Pos.CENTER );

            Button btnCancel = new Button("Cancel");
            btnCancel.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                public void handle(javafx.event.ActionEvent event) {
                    hideAndBackToMain();
                }
            });
            buttons.getChildren().add( btnCancel );

            content.getChildren().addAll( qr_icon, buttons );
            contentPane.setCenter( content );
        }
    }
    
    private BorderPane createQrScanner() {
    
        BorderPane container = new BorderPane();
        container.setStyle( "-fx-background-color: #fff;" );
        
        return container;
    }
       
    public synchronized void show() {
        if (dialog == null) return;
        if (dialog.isVisible()) return;

        if (qrCallback != null) {
            qrCallback.show();
        }
        
        int x = 0, y = 0;
//        if (dialog != null ) {
//            GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//            GraphicsConfiguration gc = d.getDefaultConfiguration();
//            Rectangle r = gc.getBounds();
//            x = (r.width - dialog.getWidth()) / 2; 
//            y = (r.height - dialog.getHeight()) / 2;
//        }
        
        dialog.setLocation(x, y);
        
        dialog.addComponentListener(new ComponentAdapter() {
            
            public void componentShown(ComponentEvent evt) {
                Platform.runLater( contentRunnable );
            }

            public void componentHidden(ComponentEvent evt) {
            }

            public void componentMoved(ComponentEvent evt) {
            }

            public void componentResized(ComponentEvent evt) {
            }
        });
        
        
//        dialog.setModal( true );
        dialog.setVisible( true );
    }
    
    public synchronized void hide() {
        if (dialog == null) return;
        if (!dialog.isVisible()) return;
        
        if (qrCallback != null) {
            qrCallback.hide();
        }
        dialog.setVisible( false );
    }
    
    public void process( String qrcode ) {
//        qrcode = "rO0ABXNyABdqYXZhLnV0aWwuTGlua2VkSGFzaE1hcDTATlwQbMD7AgABWgALYWNjZXNzT3JkZXJ4cgARamF2YS51dGlsLkhhc2hNYXAFB9rBwxZg0QMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4cD9AAAAAAAAGdwgAAAAIAAAAA3QACGxhc3RuYW1ldAAHUEVSTklUT3QACWZpcnN0bmFtZXQACkNBUkwgTE9VSUV0AAptaWRkbGVuYW1ldAAHSlVBQkxBUngA";
        
        if (qrCallback != null) {
            qrCallback.process( qrcode );
        }
        hide();
    }
    
    public void hideAndBackToMain() {
        if (dialog == null) return;
        if (!dialog.isVisible()) return;
        
        if (qrCallback != null) {
            qrCallback.hide();
            qrCallback.backToMain();
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
    
    private void println( Object msg ) {
        System.out.println( msg.toString() );
    }
}
