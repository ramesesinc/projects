/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk;


import com.rameses.osiris2.reports.ReportDataSource;
import com.rameses.util.Base64Cipher;
//import com.sun.javafx.scene.layout.region.Border;
import device.Device;
import device.DeviceListener;
import device.DeviceManager;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTextAreaElement;
import rameses.kiosk.interfaces.QRCallback;
import rameses.kiosk.interfaces.VirtualKeyboardCallback;
import rameses.kiosk.listener.InactivityListener;
import rameses.kiosk.util.KioskUtil;
import rameses.kiosk.virtualkeyboard.VirtualKeyboard;
import rameses.qr.QRScanner;

/**
 *
 * @author louie
 */
public class RamesesKiosk extends JApplet {
    
//    private RamesesKiosk kioskRoot = this;
    private static Properties props = new Properties();
    private static JFXPanel fxContainer;
    private WebEngine webEngine;
    private WebHistory history;
    private Button btnBack;
    private BridgeHandler handler = new BridgeHandler();
    private StackPane root;
//    private AnchorPane glassOverlay;
    private BorderPane progressOverlay;
    private Pane topGlassOverlay, bottomGlassOverlay, contentGlassOverlay, suspendGlassOverlay;
    private ProgressIndicator progress;
    private boolean hasAutofocus = false;
//    private ProgressBar progress;
//    private Button btnCancelProgress;
//    private Map autofocusData = new HashMap();
//    private AnchorPane root = new AnchorPane();
//    private BorderPane root = new BorderPane();
    
    private InactivityListener inactivityListener;
//    private SuspendDialog suspendDialog;
//    private StringBuffer sb = new StringBuffer();
    
    private VirtualKeyboard virtualKeyboard;
    private QRScanner qrScanner;
    
    
//    private JDialog virtualKeyboard;
//    private CustomVirtualKeyboard customVkb;
    private HTMLElement currentTarget;
    private WindowListener customWindowListener = new WindowListener() {

        public void windowOpened(WindowEvent e) {}
        public void windowClosing(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        
        public void windowActivated(WindowEvent e) { 
            Platform.runLater(new Runnable() {
                public void run() {
                    boolean showVk = true;
                    HTMLElement target = getCurrentTarget();
//                    println("window activated: target-> " + target);
                    
                    Map user = Session.getSession().getUser();
                    if (user == null && handler.getValidateQrCode() == true) {
                        showQRScanner();
                        showVk = false;
                    }
                    
                    if (showVk == true) {
                        setFocus( target );
//                        showVirtualKeyboard( target );
                    }
                    
                    /*
                    if (suspendDialog != null && suspendDialog.isActive()) {
                        showSuspendDialog();
                    }
                    */
                }
            });
        }

        public void windowDeactivated(WindowEvent e) {
            Platform.runLater(new Runnable() {
                public void run() {
                    hideQRScanner();
                    hideVirtualKeyboard();
//                    hideSuspendDialog();
//                    hideQRScanner();
                }
            });
        }
    };
    
    private int returnToIndex = 0;
    
    public void setReturnToIndex( int returnToIndex ) {
        if (returnToIndex < 0) returnToIndex = 0;
        this.returnToIndex = returnToIndex;
    }
    
    public int getReturnToIndex() {
        return this.returnToIndex;
    }
    
    public void setCurrentTarget( HTMLElement currentTarget ) {
        this.currentTarget = currentTarget;
    } 
    
    public HTMLElement getCurrentTarget() {
        return this.currentTarget;
    }
    
    public WindowListener getCustomWindowListener() {
        return this.customWindowListener;
    }
    
    public InactivityListener getInactivityListener() {
        return this.inactivityListener;
    }
    
    static {
//        println("print static ");
        try {
            File file = new File("kiosk.conf");
//            println("ap " + file.getAbsolutePath());
            props.load(new FileInputStream(file));
            
        } catch (Throwable t) {
            println("error " + t.getMessage());
        }
        
        TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() {     
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                    return null;
                } 
                public void checkClientTrusted( 
                    java.security.cert.X509Certificate[] certs, String authType) {
                        println("client trusted");
                    } 
                public void checkServerTrusted( 
                    java.security.cert.X509Certificate[] certs, String authType) {
                        println("server trusted");
                }
            } 
        }; 

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL"); 
            sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (GeneralSecurityException e) {}
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                DeviceManager.getInstance().stop();
            }
        });
    }
    
    public WebEngine getWebEngine() {
        return this.webEngine;
    }
    /*
    private final Action suspendAction = new AbstractAction() {

        public void actionPerformed(java.awt.event.ActionEvent e) {

            Platform.runLater(new Runnable() {
                public void run() {
                    inactivityListener.stop();
                    Session.getSession().setUser( null );
//                    suspendDialog.setActive(true);
//                    showSuspendDialog();
                    
//                    inactivityListener.start();
                }
            });
        }
    };
    */
    /*
    private final Action showQRAction = new AbstractAction() {
      
        public void actionPerformed( java.awt.event.ActionEvent e ) {
            Platform.runLater( new Runnable() {
                public void run() {
                    
                    inactivityListener.stop();
                    showQRScanner();
                }
            } );
        }
        
    };
    */
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setUndecorated( true );
                frame.setResizable( false );
                
                RamesesKiosk applet = new RamesesKiosk();
                applet.init( frame );
//                applet.initQRScanner( frame );
                applet.initInactivityListener( frame );
                
                frame.setContentPane(applet.getContentPane());
                frame.addWindowListener(applet.getCustomWindowListener());

                GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                GraphicsConfiguration gc = d.getDefaultConfiguration();
                Rectangle r = gc.getBounds();
                frame.setSize(r.width, r.height);
                        
                frame.setVisible(true);
                
                applet.start();
            }
        });
    }
    
    public void init( Window o ) {
        fxContainer = new JFXPanel();
        add( fxContainer );
        
        final Window owner = o;
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createVirtualKeyboard();
                initQRScanner( owner );
                createProgressOverlay();
                createGlassOverlay();
                createScene();
                loadContent();
//                createSuspendDialog();
            }
        });
    }
    
    public void initQRScanner( Window owner ) {
        createQRScanner( owner );
    }
    
    public void initInactivityListener( JFrame frame ) {
        if (inactivityListener == null) {
            Action action = new AbstractAction() {

                public void actionPerformed(java.awt.event.ActionEvent e) {

                    Platform.runLater(new Runnable() {
                        public void run() {
                            inactivityListener.stop();
                            Session.getSession().setUser( null );
                            hideVirtualKeyboardAndRemoveTarget();
//                            hideVirtualKeyboard();
                            if (handler.getValidateQrCode() == true) {
                                showQRScanner();
                            }
                        }
                    });
                }
            };
            inactivityListener = new InactivityListener(frame, action, getIdleTimeout());
        }
//        inactivityListener = new InactivityListener(frame, suspendAction, 10);
        
    }
    
    private void addTopOverlay( EventHandler<MouseEvent> mouseClickHandler ) {
        addTopOverlay( mouseClickHandler, "" );
    }
    
    private void addTopOverlay( EventHandler<MouseEvent> mouseClickHandler, String style ) {
        if (root != null) {
            if (style == null) style = "";
            topGlassOverlay.setStyle( style );
            
            if (mouseClickHandler != null) {
                topGlassOverlay.setOnMouseClicked( mouseClickHandler );
            }
            
            int size = root.getChildren().size();
            BorderPane main = (BorderPane) root.getChildren().get( size-1 );
            if (main != null) {
                StackPane sp = (StackPane) main.getTop();
                if (sp != null && !sp.getChildren().contains( topGlassOverlay )) {
                    sp.getChildren().add( topGlassOverlay );
                }
            }
        }
    }
    
    private void addContentOverlay( EventHandler<MouseEvent> mouseClickHandler ) {
        addContentOverlay( mouseClickHandler, null );
    }
    
    private void addContentOverlay( EventHandler<MouseEvent> mouseClickHandler, String style ) {
        if (root != null) {
            if (style == null) style = "";
            contentGlassOverlay.setStyle( style );
            
            if (mouseClickHandler != null) {
                contentGlassOverlay.setOnMouseClicked( mouseClickHandler );
            }
            
            BorderPane main = (BorderPane) root.getChildren().get(0);
            if (main != null) {
                StackPane sp = (StackPane) main.getCenter();
                if (sp != null && !sp.getChildren().contains( contentGlassOverlay )) {
                    sp.getChildren().add( contentGlassOverlay );
                }
            }
        }
    }
    
    private void addBottomOverlay( EventHandler<MouseEvent> mouseClickHandler ) {
        addBottomOverlay( mouseClickHandler, "" );
    }
    
    private void addBottomOverlay( EventHandler<MouseEvent> mouseClickHandler, String style ) {
        if (root != null) {
            if (style == null) style = "";
            bottomGlassOverlay.setStyle( style );
            
            if (mouseClickHandler != null) {
                bottomGlassOverlay.setOnMouseClicked( mouseClickHandler );
            }
            
            BorderPane main = (BorderPane) root.getChildren().get(0);
            if (main != null) {
                StackPane sp = (StackPane) main.getBottom();
                if (sp != null && !sp.getChildren().contains( bottomGlassOverlay )) {
                    sp.getChildren().add( bottomGlassOverlay );
                }
            }
        }
    }
    
    private void removeOverlays() {
        removeTopOverlay();
        removeContentOverlay();
        removeBottomOverlay();
    }
    
    private void removeTopOverlay() {
        if (topGlassOverlay != null) {
            topGlassOverlay.setOnMouseClicked( null );
        }
        
        if (root != null) {
            BorderPane main = (BorderPane) root.getChildren().get(0);
            if (main != null) {
                StackPane sp = (StackPane) main.getTop();
                if (sp != null && sp.getChildren().contains( topGlassOverlay )) {
                    sp.getChildren().remove( topGlassOverlay );
                }
            }
        }
    }
    
    private void removeContentOverlay() {
        if (contentGlassOverlay != null) {
            contentGlassOverlay.setOnMouseClicked( null );
        }
        
        if (root != null) {
            BorderPane main = (BorderPane) root.getChildren().get(0);
            if (main != null) {
                StackPane sp = (StackPane) main.getCenter();
                if (sp != null && sp.getChildren().contains( contentGlassOverlay )) {
                    sp.getChildren().remove( contentGlassOverlay );
                }
            }
        }
    }
    
    private void removeBottomOverlay() {
        if (bottomGlassOverlay != null) {
            bottomGlassOverlay.setOnMouseClicked( null );
        }
        
        if (root != null) {
            BorderPane main = (BorderPane) root.getChildren().get(0);
            if (main != null) {
                StackPane sp = (StackPane) main.getBottom();
                if (sp != null && sp.getChildren().contains( bottomGlassOverlay )) {
                    sp.getChildren().remove( bottomGlassOverlay );
                }
            }
        }
    }
    
    private void createVirtualKeyboard() {
        if (virtualKeyboard == null) {
            virtualKeyboard = new VirtualKeyboard( RamesesKiosk.this, new VirtualKeyboardCallbackImpl() );
        }
    }
    
    public void showVirtualKeyboard( HTMLElement target ) {
        if (virtualKeyboard == null) return;
        if (target == null) return;
                
        virtualKeyboard.setTarget( target );
        setCurrentTarget(target);
                
        virtualKeyboard.show();
    }
    
    public void hideVirtualKeyboard() {
        if (virtualKeyboard == null) return;
        
        virtualKeyboard.hide();
    }
    
    public void hideVirtualKeyboardAndRemoveTarget() {
        hideVirtualKeyboard();
        removeFocus( currentTarget );
//        if (currentTarget != null) {            
//            if (currentTarget instanceof HTMLInputElement) {
//                ((HTMLInputElement) currentTarget).blur();
//            } else if (currentTarget instanceof HTMLTextAreaElement) {
//                ((HTMLTextAreaElement) currentTarget).blur();
//            }
//        }
        
        virtualKeyboard.setTarget( null );
        setCurrentTarget( null );
        
    }
    
    private void createQRScanner( Window owner ) {
        if (qrScanner == null) {
            qrScanner = new QRScanner( new QRCallbackImpl() );
        }
    }
    
    private void showQRScanner() {
        Map user = Session.getSession().getUser();
        if (user != null) return;
        if (qrScanner == null) return;
        
        qrScanner.show();
        
    }
    
    private void hideQRScanner() {
        if (qrScanner == null) return;
        
        qrScanner.hide();
    }
    
    private String getQRDelimiter() {
        
        String delimiter = props.getProperty("qr.delimiter");
        if (delimiter == null || delimiter.trim().length() == 0) {
            delimiter = ";";
        }
        
        return delimiter.trim();
    }
    
    private int getIdleTimeout() {
        int timeoutInMillis = 10000;
        
        String val = props.getProperty( "idle.timeout" );
        if (val != null && val.trim().length() != 0) {
            try {
                timeoutInMillis = Integer.parseInt( val );
            } catch (Exception e) { }
        }
        
        return timeoutInMillis;
    }
    /*
    private int getSessionTimeout() {
        int timeoutInMinutes = 2;
        
        String val = props.getProperty("session.timeout");
        if (val != null && val.trim().length() != 0) {
            try {
                timeoutInMinutes = Integer.parseInt(val);
            } catch (Exception e) {
                
            }
        }
        
        return timeoutInMinutes;
    }
    */
    
    private int getQRScannerCountDown() {
        int countdown = 15000;
        
        String val = props.getProperty("qr.countdown");
        if (val != null && val.trim().length() != 0) {
            try {
                countdown = Integer.parseInt( val );
            } catch (Exception e) {
                
            }
        }
        
        return countdown;
    }
    
    private String getHost() {        
        String host = props.getProperty("app.host");
        if (host == null || host.trim().length() == 0) {
//            host = "192.168.254.218:8580";
//            host = "localhost:8080/index";
            host = "localhost:8580";
//            host = "www.google.com";
        }
        
        return host.trim();
    }
    
    private boolean getContextMenu() {
        boolean flag = true;
        
        String str = props.getProperty("contextmenu.enabled");
        if (str != null && str.trim().length() > 0) {
            try {
                flag = Boolean.valueOf(str);
            } catch (Exception e) {
                flag = true;
            }
        }
        
        return flag;
    }
    
    private void createScene() {
        
//        Properties props = new Properties();
        
        String portName = props.getProperty("port");
        if (portName == null || portName.trim().length() == 0) {
            portName = "COM1";
        }
//        println("port name " + portName);
        
        try {
            
//            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
//
//            println("has more elements: " + portList.hasMoreElements());
//
//            CommPortIdentifier portIdentifier;
//            while (portList.hasMoreElements()) {
//                portIdentifier = (CommPortIdentifier) portList.nextElement();
//
//                println("port name: " + portIdentifier.getName());
//                println("port type: " + portIdentifier.getPortType());
//                println("port owner: " + portIdentifier.getCurrentOwner());
//                println("");
//            }
            
            
            Map settings = new HashMap();        
            Device d = DeviceManager.getInstance().register(portName, settings);
            d.setListener(new MyDeviceListener());
            DeviceManager.getInstance().start();
        } catch (Exception e) {
            e.printStackTrace();
//            println("exception " + e.getMessage());
        }
        
        root = new StackPane();
        root.setId("root");
        
        BorderPane rootContent = new BorderPane();
        root.getChildren().add(rootContent);
//        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent e) {
//                println("location-> " + e.getScreenX() + ", " + e.getScreenY());
//            }
//        });
        
        rootContent.setTop( createHeader() );
        rootContent.setCenter( createContent() );
        rootContent.setBottom( createFooter() );
        
        Scene scene = new Scene( root );
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/kiosk-theme.css").toExternalForm());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/progress-overlay.css").toExternalForm());
          
        
        
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            public void handle( KeyEvent t ) {
//                KeyCode.
//            }
//        });

        /*
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
//                println("key released");
                String val = t.getText();
                if (val != null) {
                    if (t.getCode().equals(KeyCode.ENTER)) {
                        println("qr value-> " + sb.toString());
                        sb = new StringBuffer();
                    } else {
                        sb.append(val);
                    }
                }
                
            }
        });
        */
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            public void handle(KeyEvent t) {
////                System.out.println("keycode " + t.getCode());
//                println("key pressed");
////                String val = t.getText();
////                println("code->" + t.getCode() + " val->" + val);
////                if (val != null) {
////                    if (val.equals(getQRDelimiter())) {
////                        println("qr value-> " + sb.toString());
////                        sb = new StringBuffer();
////                    } else {
////                        sb.append(val);
////                    }
////                }
////                if (t.getCode().equals(KeyCode.ENTER)) {
////                    System.out.println("keypressed-> " + sb.toString());
////                    sb = new StringBuffer();
////                } else if (t.getCode().equals(KeyCode.SPACE)) {
////                    sb.append(" ");
////                } else {
////                    sb.append(t.getText());
////                }
////                System.out.println("keycode " + t.getCode());
//            }
//        });
        
        
        fxContainer.setScene(scene);
    }
    
    private void loadContent() {
        webEngine.load("http://" + getHost());
//        webEngine.load("http://192.168.254.23:8580");
//        webEngine.load("http://" + getHost() + "/binrpt");
//        webEngine.load("file:///D:/Web/Sample.html");
    }
    
    private void showProgress() {
        if (root != null && progressOverlay != null && !root.getChildren().contains(progressOverlay)) {
            root.getChildren().add(progressOverlay);
            
        }
    }
    
    private void hideProgress() {
        if (root != null && progressOverlay != null && root.getChildren().contains(progressOverlay)) {
            root.getChildren().remove(progressOverlay);

        }
    }
    
    private void createProgressOverlay() {
//        glassOverlay = new AnchorPane();
        progressOverlay = new BorderPane();
        progressOverlay.setId("progress-overlay");
        
        progress = new ProgressIndicator();
        progress.setMinSize(150, 150);
//        progress.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
//        progress = new ProgressBar();
        
        Button btnCancelProgress = new Button("Cancel");
        btnCancelProgress.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                
                Worker worker = webEngine.getLoadWorker();
                boolean flag = worker.cancel();
                if (flag == true) {
                    hideProgress();
                }
            }
        });
        
        VBox progressContainer = new VBox(10);
        progressOverlay.setCenter( progressContainer );
        
        progressContainer.setAlignment( Pos.CENTER );
        
//        ImageView logo = new ImageView(new Image(getClass().getClassLoader().getResource("resources/images/progress.gif").toExternalForm()));
        progressContainer.getChildren().addAll(progress, btnCancelProgress);
//        AnchorPane.setTopAnchor(logo, 30.0);
//        AnchorPane.setLeftAnchor(logo, 30.0);
//        AnchorPane.setBottomAnchor(logo, 30.0);
//        AnchorPane.setRightAnchor(logo, 30.0);
//        
//        glassOverlay.getChildren().add(logo);
//        progressOverlay.setCenter(logo);
        
//        root.getChildren().add(glassOverlay);
    }
    
    private void createGlassOverlay() {
//        String style = "-fx-background-color: orange;";
        
        topGlassOverlay = new Pane();
//        topGlassOverlay.setStyle( style );
        
        contentGlassOverlay = new Pane();
//        contentGlassOverlay.setStyle( style );
        
        bottomGlassOverlay = new Pane();
//        bottomGlassOverlay.setStyle( style );
    }
    
    /*
    private void createSuspendDialog() {
        if (suspendDialog == null) {
            EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {

                public void handle(ActionEvent t) {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            if (suspendDialog != null) suspendDialog.setActive(false);
                            hideSuspendDialog();
                            webEngine.load("http://" + getHost());
                            inactivityListener.start();
                        }
                    });
                }
            
            
            };
            suspendDialog = new SuspendDialog(e);
        }
    }
    
    private void showSuspendDialog() {
        if (suspendGlassOverlay == null) suspendGlassOverlay = new Pane();
        if (suspendDialog != null) {
            if (root != null && !root.getChildren().contains(suspendGlassOverlay)) {
                root.getChildren().add(suspendGlassOverlay);
            }
            suspendDialog.show();
        }
    }
    
    private void hideSuspendDialog() {
        if (root != null && root.getChildren().contains(suspendGlassOverlay)) {
            root.getChildren().remove(suspendGlassOverlay);
        }
        
        if (suspendDialog != null) {
            suspendDialog.hide();
        }
    }
    */
    
    private final EventHandler returnToHomeAction = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent t) {
            webEngine.load("http://" + getHost());
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    };
    
    private String getRedirectIP() {
        String redirect = props.getProperty("redirect.ip");
        if (redirect == null || redirect.trim().length() == 0) {
//            host = "192.168.254.21:8580/index";
            redirect = "localhost";
        }
        
        return redirect.trim();
    }
    
    private Boolean getAllowRedirect() {
        boolean flag = false;
        
        String redirect = props.getProperty("redirect.allow");
        if (redirect != null && redirect.trim().length() > 0) {
//            host = "192.168.254.21:8580/index";
            try {
                flag = Boolean.valueOf(redirect);
            } catch (Exception e) {
                flag = false;
            }
        }
        
        return flag;
    }
    
    private StackPane createContent() {
        StackPane content = new StackPane();
//        webEngine.documentProperty().addListener(new WebDocumentListener(webEngine));
        
        WebView browser = new WebView();
        content.getChildren().add(browser);
        
        browser.setContextMenuEnabled( getContextMenu() );
        
        
        webEngine = browser.getEngine();
        history = webEngine.getHistory();
        
        if (btnBack != null) {
            btnBack.disableProperty().bind(history.currentIndexProperty().lessThanOrEqualTo(0));
        }
        
//        history.currentIndexProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number oldIndex, Number newIndex) {
////                println("old idx-> " + oldIndex + " new idx-> " + newIndex);
////                int val = 0;
////                if (newIndex != null) {
////                    val = newIndex.intValue();
////                }
////                switch (val) {
////                    case 0: hideBackButton(); break;
////                    default: addBackButton(); break;
////                }
////                if (val > 0) {
////                    addBackButton();
////                } els
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//            
//        });
//        history.getEntries().addListener(new ListChangeListener<Entry>() {
//            public void onChanged(ListChangeListener.Change<? extends Entry> change) {
//                println("change history: current index-> " + history.getCurrentIndex());
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        
//        });
        
//        webEngine.onVisibilityChangedProperty().addListener(new ChangeListener<EventHandler<WebEvent<Boolean>>>() {
//
//            public void changed(ObservableValue<? extends EventHandler<WebEvent<Boolean>>> ov, EventHandler<WebEvent<Boolean>> t, EventHandler<WebEvent<Boolean>> t1) {
//                println("new event " + t1 + " old event " + t);
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        
//        });
        
        /*
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldLoc, String newLoc) {
//                println("old loc " + oldLoc + " new loc " + newLoc);
//                println("location changed");
                hideVirtualKeyboard();
                
                JSObject jsobj = (JSObject) webEngine.executeScript("window");
                jsobj.setMember("handler", handler);
                
//                Object member = jsobj.getMember("handler");
////                println("member " + member + " " + (member == null) + " " + (member.equals("undefined")));
//                if (member == null || member.equals("undefined")) {
//                    jsobj.setMember("handler", handler);
//                }
                
//                println("new location " + newLoc);
                
                //re-direct localhost
                if (getAllowRedirect() == true) {
                    if (newLoc.contains("://localhost")) {
                        String redirect = getRedirectIP();
                        newLoc = newLoc.replace("://localhost", "://" + redirect);
                        webEngine.load(newLoc);
                    } 
                }
//                if (newLoc.contains("/" + getHost())) {
//                    hideBackButton();
//                    
//                }
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        
        });
       */
                
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            
            public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document newDoc) {

//                JSObject jsobj = (JSObject) webEngine.executeScript("window");
//                jsobj.setMember("handler", handler);
                
                if (newDoc != null) {
//                    addDocumentListener( newDoc );
                    
                } else if (newDoc == null) {
//                     try{
//                        // Use reflection to retrieve the WebEngine's private 'page' field. 
//                        Field f = webEngine.getClass().getDeclaredField("page"); 
//                        f.setAccessible(true);
//                        com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);  
//                        page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB()); 
//
//                    }catch(Exception ex){
//                        ex.printStackTrace();
//                    }
                }
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        
        });

        
        
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {

            @Override
            public void handle(WebEvent<String> t) {
                println("alert " + t);
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
                
        /*
        webEngine.setOnError(new EventHandler<WebErrorEvent>() {

            @Override
            public void handle(WebErrorEvent t) {
                println("error " + t);
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        */
        final Worker worker = webEngine.getLoadWorker();
        
        worker.exceptionProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                println("old->" + t.toString() + " new->" + t1.toString());
            }
        });
        
        worker.progressProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> ov, Number oldProgress, Number newProgress) {
                double value = newProgress.doubleValue();
                if (value >= 1 || worker.getState() == Worker.State.SUCCEEDED) {
                    hideProgress();
                } else {
                    showProgress();
                }
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        worker.stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State ot, Worker.State nt) {
                if (handler != null) {
                    handler.setAllowBarcodeScanning( false );
                    handler.setValidateQrCode( false );
                }
                
                
                hideVirtualKeyboard();
                hideQRScanner();
                
                if (nt == Worker.State.SUCCEEDED) {
                    addDocumentListener( webEngine.getDocument() );
//                    addHtmlDocumentListener(webEngine.getDocument());
//                    showAutofocusData();
                    
//                    JSObject jsobj = (JSObject) webEngine.executeScript("window");
//                    println("window object " + jsobj);
                    
                    JSObject jsobj = (JSObject) webEngine.executeScript("WebViewUtil.getWindowObject()");
                    if (jsobj != null) {
                        jsobj.setMember("handler", handler);
                    }
                    webEngine.executeScript("WebViewUtil.fireWebViewInitialization()");
                    
//                    webEngine.executeScript("WebViewUtil.addBridgeHandler(" + params + ")");
                    //JSObject xjsobj = (JSObject) webEngine.executeScript("WebViewUtil.getPageName()");;
                    //println("xjsobj-> " + xjsobj.getMember("pageName"));
                    hideProgress();
                    
                    Map user = Session.getSession().getUser();
//                    println("user->" + user + " validateqr->" + handler.getValidateQrCode() + " hasautofocus->" + hasAutofocus);
                    if (user == null && handler.getValidateQrCode() == true) {
                        showQRScanner();
                        hasAutofocus = false;
                    }
                    if (handler.getValidateQrCode() == false) {
                        Session.getSession().setUser( null );
                        if (inactivityListener != null) {
                            inactivityListener.stop();
                        }
                    }
                    
//                    println("user-> " + user + " hasautofocus " + hasAutofocus);
                    
                    if (user != null && hasAutofocus == true) {                       
                        
                        setFocus( currentTarget );
//                        if (currentTarget != null) {
//                            
//                            if (currentTarget instanceof HTMLInputElement) {
//                                ((HTMLInputElement) currentTarget).focus();
//                            } else if (currentTarget instanceof HTMLTextAreaElement) {
//                                ((HTMLTextAreaElement) currentTarget).focus();
//                            }
//                            
//                            
////                            try {
////                                Document d = webEngine.getDocument();
////                                EventTarget et = (EventTarget) currentTarget;
////                                
////                                
////                                DocumentEvent de = (DocumentEvent) d;
////                                Event evt = de.createEvent("HTMLEvents");
////                                evt.initEvent("focusin", false, false);
////                                et.dispatchEvent( evt );
////                                println("after event dispatch");
////                            } catch (Throwable t) {
////                                t.printStackTrace();
////                            }
//                        }
                        
                        hasAutofocus = false;
                    } else {  
                        
                        removeFocus( currentTarget );
//                        if (currentTarget != null) {
//                            
//                            if (currentTarget instanceof HTMLInputElement) {
//                                ((HTMLInputElement) currentTarget).blur();
//                            } else if (currentTarget instanceof HTMLTextAreaElement) {
//                                ((HTMLTextAreaElement) currentTarget).blur();
//                            }
//                        }
                        
//                        setCurrentTarget( null );
//                        try {
//                            Document d = webEngine.getDocument();
//                            EventTarget et = (EventTarget) d;
//
//
//                            DocumentEvent de = (DocumentEvent) d;
//                            Event evt = de.createEvent("HTMLEvents");
//                            evt.initEvent("focusin", false, false);
//                            et.dispatchEvent( evt );
//                            println("after event dispatch");
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        }
                    }
                    
                    setUserToWebViewContent( user );
                } else if (nt == Worker.State.FAILED || nt == Worker.State.CANCELLED) {
                    //prompt error
                    println("error");
                    hideProgress();
                } else {
                    showProgress();
                }
            }
        
        });
                
        return content;
    }
    
    
    private ArrayList<javafx.scene.Node> getDefaultHeaderButtons() {
        ArrayList<javafx.scene.Node> list = new ArrayList<javafx.scene.Node>();
        
        
        btnBack = new Button("Back");
        btnBack.setMinSize(20, 20);
//        btnBack.getStyleClass().add("toolbar-button");
//        btnBack.disableProperty().bind(history.currentIndexProperty().lessThanOrEqualTo(0));
//                 backButton.disableProperty().bind(history.currentIndexProperty().lessThanOrEqualTo(0));
        btnBack.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                if (history != null && history.getCurrentIndex() > 0) {
                    history.go(-1);
                }
            }
        });
        list.add( btnBack );
//        hideBackButton();
        
        
        Button btnReload = new Button("Reload Page");
        btnReload.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (webEngine != null) {
//                    println("location " + webEngine.getLocation());
//                    webEngine.reload();
                    webEngine.executeScript("WebViewUtil.reload()");
                }
            }
        });
//        list.add( btnReload );
        
        Button btnCheckJS = new Button("Check JS");
        btnCheckJS.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (webEngine != null) {
                    webEngine.executeScript("WebViewUtil.getPageInfo()");
                }
            }
        });
//        list.add( btnCheckJS );
        
        Button btnCurrentIndex = new Button("Show Current Index");
        btnCurrentIndex.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent e) {
               int idx = history.getCurrentIndex();
               println("current index-> " + idx);
           }
        });
//        list.add(btnCurrentIndex);
        
        Button btnShowHistory = new Button("Show History");
        btnShowHistory.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                
//                BorderPane main = (BorderPane) root.getChildren().get(0);
//                StackPane top = main.getTop();
//                
//                javafx.scene.Node bottom = main.getBottom();g
//                println("top " + top.getClass() + " bottom " + bottom.getClass());
//                println("root first child " + root.getChildren().get(0).getClass());
                
                ObservableList<Entry> list = history.getEntries();
//                println("history-> " + list.size());
                for (int i=0; i<list.size(); i++) {
                    println(list.get(i).toString());
                }
//                println("current index " + history.getCurrentIndex());
//                println("");
            }
        });
        
        Button btnShowQr = new Button("Show QR Scanner");
        btnShowQr.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showQRScanner();
            }
        });
//        list.add( btnShowQr );
        
        Button btnRemoveUserData = new Button("Remove User Data");
        btnRemoveUserData.setOnAction(new EventHandler<ActionEvent>() {
            
            public void handle( ActionEvent event ) {
                Session.getSession().setUser( null );
            }            
        });
//        list.add( btnRemoveUserData );
        
        Button btnAddBottomOverlay = new Button("Add Bottom Overlay");
        btnAddBottomOverlay.setOnAction(new EventHandler<ActionEvent>() {
            public void handle( ActionEvent ae ) {
                addBottomOverlay( null );
            }
        });
//        list.add( btnAddBottomOverlay );
//        Button btnShowSession = new Button("Show session");
//        btnShowSession.setMinSize(20, 20);
//        btnShowSession.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        showSuspendDialog();
//                    }
//                });
//            }
//        });
//        list.add(btnShowSession);
//        
//        Button btnHideSession = new Button("Hide session");
//        btnHideSession.setMinSize(20,20);
//        btnHideSession.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        hideSuspendDialog();
//                    }
//                });
//            }
//        });
//        list.add(btnHideSession);
//        list.add(btnShowHistory);
        
        Button btnSetUser = new Button("Set User Info");
        btnSetUser.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (webEngine != null) {
                    webEngine.executeScript("WebViewUtil.setUserInfo({firstname:'CARL LOUIE', lastname:'PERNIT'})");
                }
            }
        });
//        list.add( btnSetUser );
        
        Button btnGetUser = new Button("Get User Info");
        btnGetUser.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (webEngine != null) {
                    JSObject result = (JSObject) webEngine.executeScript("WebViewUtil.getUserInfo()");
                    if (result != null) {
                        System.out.println("firstname->" + result.getMember("firstname") + " lastname->" + result.getMember("lastname"));
                    }
                }
            }
        });
//        list.add( btnGetUser );
        
        return list;
    }
    
//    private void showBackButton() {
//        if (btnBack != null && btnBack.isVisible()==false) {
//            btnBack.setVisible(true);
//        }
//    }
//    
//    private void hideBackButton() {
//        if (btnBack != null && btnBack.isVisible()==true) {
//            btnBack.setVisible(false);
//        }
//    }
    
    private StackPane createHeader() {
//        BorderPane header = new BorderPane();
//        Pane header = new Pane();
        StackPane container = new StackPane();
        VBox headerContainer = new VBox();
        container.getChildren().add( headerContainer );
        
        HBox header = new HBox();
        header.setId( "header" );
        headerContainer.getChildren().add( header );
        header.setAlignment( Pos.BOTTOM_LEFT );
//        header.setMinHeight( 300 );
        header.setMinHeight( 150 );
        
        /*
        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getClassLoader().getResource("resources/images/bg.jpg").toExternalForm()),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          BackgroundSize.DEFAULT);
        container.setBackground(new Background(myBI));
        */
        
//        header.setStyle("-fx-background-color: red;");
//        println(getClass().getClassLoader().getResource("resources/images/logo.png").toExternalForm());
        
        ImageView logo = new ImageView(new Image(getClass().getClassLoader().getResource("resources/images/kiosk-logo.png").toExternalForm()));
        logo.setFitHeight( 150d );
        logo.setFitWidth( 600d );
        Hyperlink home = new Hyperlink("", logo);
//        home.setBorder();
//        home.setBorder( Border.EMPTY );
        home.setFocusTraversable( false );
        home.setOnAction( returnToHomeAction );
//        home.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                System.out.println("home header");
//            }
//        });
        
        header.getChildren().addAll( home );

//        HBox toolbar = new HBox();
//        toolbar.setId( "header-toolbar" );
//        headerContainer.getChildren().add( toolbar );
//        toolbar.setSpacing( 5 );
//        toolbar.getChildren().addAll( getDefaultHeaderButtons() );
//        header.setCenter(new Label("HEADER"));
        
        return container;
    }
    
    private StackPane createFooter() {
        StackPane container = new StackPane();
        
        VBox footerContainer = new VBox();
        container.getChildren().add( footerContainer );
        footerContainer.setId( "footer" );
        
        HBox footer = new HBox();
        footerContainer.getChildren().add( footer );
//        footer.setPadding(new Insets(0, 60, 0, 60));
        footer.setAlignment( Pos.TOP_RIGHT );
//        footer.setMinHeight( 300 );
        footer.setMinHeight( 150 );
        
        /*
        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getClassLoader().getResource("resources/images/bg.jpg").toExternalForm()),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          BackgroundSize.DEFAULT);
        container.setBackground(new Background(myBI));
        */
//        footer.setStyle("-fx-background-color: red; -fx-padding: 30px 60px 30px 60px;");
        
        ImageView logo = new ImageView(new Image(getClass().getClassLoader().getResource("resources/images/logo.png").toExternalForm()));
        logo.setFitHeight( 50d );
        logo.setFitWidth( 100d );
        Hyperlink home = new Hyperlink("", logo);
//        home.setGraphic(logo);
//        home.setBorder( Border.EMPTY );
        home.setFocusTraversable( false );
        home.setOnAction( returnToHomeAction );
//        home.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                System.out.println("home footer");
//            }
//        });
        
//        Button home = new Button("Home");
        
        footer.getChildren().addAll( home );
        
        return container;
    }
    
    private void addDocumentListener( Document d ) {
        if (d == null) return;
        
        EventTarget et = (EventTarget) d;
//        et.addEventListener("focus", focusListener, false);
//        et.addEventListener("focusin", focusListener, false);
        et.addEventListener("load", loadListener, false);
//        et.addEventListener("click", clickListener, true);
//        synchronized (d) {
//            d.notifyAll();
//        }
        
        hasAutofocus = false;
        HTMLElement e = null;
        String[] strArray = {"input", "textarea"};
        for (String tagname : strArray) {
            e = null;
            
            NodeList list = d.getElementsByTagName( tagname );
            if (list != null) {
                addListenerToElements( list );
                removeFocusToElements( list );
                e = getElementWithAutofocus( list );
            }
            if (e != null) {
                hasAutofocus = true;
                break;
                /*
                NamedNodeMap attribs = e.getAttributes();                                
                Node n = attribs.getNamedItem("disablevirtualkeyboard");
                if (n == null) {
                    if (e instanceof HTMLInputElement) {
                        ((HTMLInputElement) e).focus();
                    } else if (e instanceof HTMLTextAreaElement) {
                        ((HTMLTextAreaElement) e).focus();
                    }
                    hasAutofocus = true;
                    break;
                }
                */
            }
        }
        
//        println("validateqrcode " + handler.getValidateQrCode() + " hasautofocus " + hasAutofocus);
        setCurrentTarget( null );
        if (handler.getValidateQrCode() == false && hasAutofocus == true) {
            setCurrentTarget( e );
        }
    }    
    
    private void addListenerToElements( NodeList list ) {
        for (int i=0; i<list.getLength(); i++) {
            EventTarget et = (EventTarget) list.item( i );
            if (et instanceof HTMLInputElement || 
                    et instanceof HTMLTextAreaElement) {
                et.addEventListener("focusin", focusListener, false);
            }
//            et.addEventListener("focus", focusListener, false);
            
//            et.addEventListener("focusout", focusoutListener, false);
        }
    }
    
    private void removeFocusToElements( NodeList list ) {
        for (int i=0; i<list.getLength(); i++) {
            EventTarget et = (EventTarget) list.item( i );
            if (et instanceof HTMLInputElement) {
                ((HTMLInputElement) et).blur();
            } else if (et instanceof HTMLTextAreaElement) {
                ((HTMLTextAreaElement) et).blur();
            }
        }
    }
    
    private HTMLElement getElementWithAutofocus(NodeList list) {
        HTMLElement e = null;
                
        for (int i=0; i<list.getLength(); i++) {
            HTMLElement he = (HTMLElement) list.item(i);
            if (he.getAttribute("autofocus") != null) {
                e = he;
                break;
            }
        }
        
        return e;
    }
    
    private void handleInput( HTMLElement i ) {
//        println("handle input");
        //HTMLButtonElement b;
        if (i instanceof HTMLInputElement) {
            NamedNodeMap attribs = i.getAttributes();
            Node node = attribs.getNamedItem("type");
            if (node != null && !node.getNodeValue().toLowerCase().equals("text")) {
                return;
            }
        }
        
        ((EventTarget) i).addEventListener("focusout", focusoutListener, false);
        showVirtualKeyboard( i );
    }
    
    private final EventListener focusListener = new EventListener() {

        @Override
        public void handleEvent(Event evt) {
//            println("focus listener");
            EventTarget et = evt.getTarget();
            
            if (!(et instanceof HTMLElement)) return;
            
            HTMLElement he = (HTMLElement) et;
            NamedNodeMap attribs = he.getAttributes();
            Node node = attribs.getNamedItem("disabled");
            if (node != null && node.getNodeValue().toLowerCase().equals("true")) {
                return;
            }
            
            node = attribs.getNamedItem("readonly");
            if (node != null && node.getNodeValue().toLowerCase().equals("true")) {
                return;
            }
            
            /*
            node = attribs.getNamedItem("disablevirtualkeyboard");
            if (node != null) {
                return;
            }
            */
            
            if (et instanceof HTMLInputElement || et instanceof HTMLTextAreaElement) {
                handleInput( (HTMLElement) et );
            }
            
        }
    };
    
    private final EventListener focusoutListener = new EventListener() {
        @Override
        public void handleEvent(Event evt) {
            EventTarget et = evt.getTarget();
            
            if (et instanceof HTMLElement) {
                removeFocus( (HTMLElement) et );
            }
            
            et.removeEventListener("focusout", focusoutListener, false);
            hideVirtualKeyboardAndRemoveTarget();
        }
    };
    
    private final EventListener loadListener = new EventListener() {
        @Override
        public void handleEvent(Event evt) {
            EventTarget et = evt.getTarget();
            
            if (et instanceof HTMLIFrameElement) {
                HTMLIFrameElement iframe = (HTMLIFrameElement) et;
                Document d = iframe.getContentDocument();
                if (d != null) {
                    addDocumentListener( d );
                }
            }
        }
    };
    
    private void setFocus( HTMLElement target ) {
        if (target == null) return;
        
        if (target instanceof HTMLInputElement) {
            ((HTMLInputElement) target).focus();
        } else if (target instanceof HTMLTextAreaElement) {
            ((HTMLTextAreaElement) target).focus();
        }
    }
    
    private void removeFocus( HTMLElement target ) {
        if (target == null) return;
        
        if (target instanceof HTMLInputElement) {
            ((HTMLInputElement) target).blur();
        } else if (target instanceof HTMLTextAreaElement) {
            ((HTMLTextAreaElement) target).blur();
        }
    }
    
    private void setUserToWebViewContent( Map user ) {
        if (webEngine != null) {
            webEngine.executeScript("WebViewUtil.setUserInfo(" + KioskUtil.convertToJSONObject( user ) + ")");
        }
    }
    
    private static void println( Object msg ) {
        System.out.println( msg.toString() );
    }
    
    private String getBarcodeURL() {
    
        String url = props.getProperty("app.barcode.url");
        if (url == null || url.trim().length() == 0) {
//            url = "192.168.254.21:8080/index/billing";
//            url = "192.168.253.102:8580/index/barcodeloader";
            url = "localhost:8080/index/barcodeloader";
        }
//        println("host " + host);
        
        return url;
    }
    
    private class QRCallbackImpl implements QRCallback {
        private StringBuilder sb;
        
        public void process( String qrcode ) {
            
//            println("qrcode " + qrcode);            
            Session sess = Session.getSession();
            try {
                Map data = (Map) new Base64Cipher().decode( qrcode );
                println("data-> " + data);
                sess.setUser( data );
                setUserToWebViewContent( data );
                getInactivityListener().start();
//                if (webEngine != null) {
//                    webEngine.executeScript("WebViewUtil.reload()");
//                }
            } catch (Throwable t) {
                t.printStackTrace();
                sess.setUser( null );
                getInactivityListener().stop();
                throw new RuntimeException("Invalid QR Code.");
//                println("Error: " + t.getMessage());
            }
            
        }
        
        public void show() {
            
            addTopOverlay( null );
            addContentOverlay( null );
            addBottomOverlay( null );
                        
            if (fxContainer != null) {
                sb = new StringBuilder();
                Scene scene = fxContainer.getScene();
                scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
                    public void handle( KeyEvent t ) {
                        String val = t.getCharacter();
                        if (val != null) {
                            sb.append( val );
                        }
                    }
                });
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent t) {
                        if (t.getCode().equals( KeyCode.ENTER )) {
                            try {
                                qrScanner.process( sb.toString() );
//                                if (hasAutofocus == true) {
//                                    setFocus( getCurrentTarget() );                                        
//                                }
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                            sb = new StringBuilder();
                        }
                    }
                });
//                scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
//                    public void handle( KeyEvent t ) {
//                        
//                        String val = t.getText();
////                        println("released val->" + val + " keycode->" + t.getCode());
//                        
////                        println("shiftdown->" + t.isShiftDown() + " altdown->" + t.isAltDown() + " ctrldown->" + t.isControlDown() + " shortcutdown->" + t.isShortcutDown() + " metadown->" + t.isMetaDown());
//                        if (t.getCode().equals( KeyCode.ENTER )) {
//                            try {
//                                    qrScanner.process( sb.toString() );
//                                    if (hasAutofocus == true) {
//                                        setFocus( getCurrentTarget() );                                        
//                                    }
//                                } catch (Throwable th) {
//                                    th.printStackTrace();
//                                }
//                                sb = new StringBuilder();
//                        } else {
//                            
////                            println("val " + val + " isShiftDown " + t.isShiftDown());
//                            if (val != null) {
//                                sb.append( val );
//                            }
//                        }
//
//                        t.consume();
//                    }
//                });
            }
            
        }
        
        public void hide() {
            removeOverlays();
            
            if (fxContainer != null) {
                Scene scene = fxContainer.getScene();
                scene.setOnKeyTyped( null );
                scene.setOnKeyPressed( null );
                scene.setOnKeyReleased( null );
            }
        }
        
        public void back() {
            if (history != null && history.getCurrentIndex() > 0) {
                history.go( -1 );
            }
        }
        
        public void backToMain() {
            if (history != null) {
                int goToIdx = 0;
//                println("current index " + history.getCurrentIndex());
                if (history.getCurrentIndex() > 0) {
                    goToIdx = history.getCurrentIndex() * -1;
                }
                
                history.go( goToIdx );
            }
        }
    }
    
    private class VirtualKeyboardCallbackImpl implements VirtualKeyboardCallback {
    
        public void show() {
            EventHandler mouseClickedHandler = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    hideVirtualKeyboardAndRemoveTarget();
                    
                }
            };
            addTopOverlay( mouseClickedHandler );
            addBottomOverlay( mouseClickedHandler );
        }
        
        public void hide() {
            removeOverlays();
        }
    }
    
    public class MyDeviceListener extends DeviceListener {
        public void handle(Object msg) {
            final String barcodeStr = msg.toString();
            Platform.runLater(new Runnable() {
                public void run() {
                    boolean allowBarcodeScanning = handler.getAllowBarcodeScanning();
//                    println("allow barcode scanning " + allowBarcodeScanning);
                    if (allowBarcodeScanning) {
                        String host = "http://" + getBarcodeURL() + "?barcode=" + barcodeStr;
//                        println("barcode host " + host);
                        webEngine.load( host );
                    }
                }
            });
        }
    }
    
    public class BridgeHandler {

        private boolean allowBarcodeScanning = false;
        private boolean validateQrCode = false;
        
        void println(String str) {
            System.out.println(str);
        }
        
        String getReport(String propname) {
            if (propname == null || propname.equals("")) {
                propname = "report.queue";
            }
            
//            println("propname " + propname);
            
            String report = props.getProperty(propname);
            if (report == null || report.trim().length() == 0) {
                report = "c:/report/queue_receipt.jasper";
            }
//            println("report " + report);

            return report;
        }

        public void print(Object obj, String handler) {
            JSObject args = (JSObject) obj;
            JSObject keys = (JSObject)args.eval("Object.keys(this)");
            
            List<String> keyList = new ArrayList<String>();
            boolean flag = true;
            int idx = 0;
            String val = "";
            while (flag == true) {
                try {
                    val = keys.getSlot(idx).toString();
//                    keyList.add(keys.getSlot(idx).toString());
                } catch (JSException e) {
                    flag = false;
                }
                if ("undefined".equalsIgnoreCase(val)) {
                    flag = false;
                } else {
                    keyList.add(val);
                }
//                println("flag " + flag);
                idx++;
            }
            
            Map reportdata = new HashMap();
            idx = 0;
            String key = "";
            Object value = null;
            for (idx=0; idx<keyList.size(); idx++) {
                key = keyList.get(idx).toString();
                value = args.getMember(key);
                reportdata.put(key, value);
            }
            
            try {
//                println("report data " + reportdata);
                ReportDataSource reportds = new ReportDataSource(reportdata);
//                println("pass");  
                
                String report = "report." + handler;
                println("report " + report);
                
                JasperPrint jp = JasperFillManager.fillReport(getReport(report), new HashMap(), reportds);
//                println("pass 1");
                JasperPrintManager.printReport(jp, false);
//                println("pass 2");
            } catch (Exception e) {
                println("error " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        public void setValidateQrCode( boolean validateQrCode ) {
            this.validateQrCode = validateQrCode;
        }
        
        public void setAllowBarcodeScanning(boolean allowBarcodeScanning) {
            this.allowBarcodeScanning = allowBarcodeScanning;
//            println("allow barcode scanning " + allowBarcodeScanning);
        }
        
        public boolean getValidateQrCode() {
            return this.validateQrCode;
        }
        
        public boolean getAllowBarcodeScanning() {
            return this.allowBarcodeScanning;
        }
        
        public void onpageload(String pagename) {
//            println("pagename " + pagename);
//            addHtmlDocumentListener(webEngine.getDocument());
            
        }
        
        
    }
    
}
