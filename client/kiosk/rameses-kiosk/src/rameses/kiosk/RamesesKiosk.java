/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk;


import com.rameses.osiris2.reports.ReportDataSource;
import device.Device;
import device.DeviceListener;
import device.DeviceManager;
import java.awt.Dialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
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
import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
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

/**
 *
 * @author louie
 */
public class RamesesKiosk extends JApplet {
    
    private static Properties props = new Properties();
    private static JFXPanel fxContainer;
    private WebEngine webEngine;
    private WebHistory history;
    private Button btnBack;
    private BridgeHandler handler = new BridgeHandler();
    private StackPane root;
//    private AnchorPane glassOverlay;
    private BorderPane progressOverlay;
    private Pane topGlassOverlay, bottomGlassOverlay;
    private ProgressIndicator progress;
//    private ProgressBar progress;
//    private Button btnCancelProgress;
    private Map autofocusData = new HashMap();
//    private AnchorPane root = new AnchorPane();
//    private BorderPane root = new BorderPane();
    
    private JDialog virtualKeyboard;
    private CustomVirtualKeyboard customVkb;
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
                    HTMLElement target = getCurrentTarget();
                    if (target != null) {
                        showVirtualKeyboard(target);
                    }
                }
            });
        }

        public void windowDeactivated(WindowEvent e) {
            Platform.runLater(new Runnable() {
                public void run() {
                    hideVirtualKeyboard();
                }
            });
        }
    };
    
    private int returnToIndex = 0;
    
    public void setReturnToIndex(int returnToIndex) {
        if (returnToIndex < 0) returnToIndex = 0;
        this.returnToIndex = returnToIndex;
    }
    
    public int getReturnToIndex() {
        return this.returnToIndex;
    }
    
    public void setCurrentTarget(HTMLElement currentTarget) {
        this.currentTarget = currentTarget;
    } 
    
    public HTMLElement getCurrentTarget() {
        return this.currentTarget;
    }
    
    public WindowListener getCustomWindowListener() {
        return this.customWindowListener;
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
        } catch (GeneralSecurityException e) {
            
        } 
        
//        Runtime.getRuntime().addShutdownHook(new Runnable() {
//            public void run() {
//            }
//        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                DeviceManager.getInstance().stop();
            }
        });
    }
    
//    public void setShowToolbar(boolean showToolbar) {
//        this.showToolbar = showToolbar;
//    }
//    
//    public Boolean getShowToolbar() {
//        return this.showToolbar;
////        return true;
//    }
    

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
//                try {
//                    Font f = Font.loadFont(CustomWebBrowser.class.getClassLoader().getResource("resources/fonts/chosence.otf").toExternalForm(), 10);
//                    println("font " + f);
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
                
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setUndecorated(true);
                frame.setResizable(false);
                
                RamesesKiosk applet = new RamesesKiosk();
                applet.init();
                             
                
                frame.setContentPane(applet.getContentPane());
                frame.addWindowListener(applet.getCustomWindowListener());
//                
//                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//                Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//                
//                frame.setBounds(0, 0, r.width, r.height);
////                frame.setLocationRelativeTo(null);
//                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                frame.setUndecorated(true);
//                frame.show();
//                frame.setVisible(true);
//                
                GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//                println("full screen supported " + d.isFullScreenSupported());
//                if (d.isFullScreenSupported()) {
//                    d.setFullScreenWindow(frame);
//                    
////                    println("device full screen");
//                    
//                } else {
//                    frame.setVisible(true);
////                    println("frame visible");
//                    
//                }
//                GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                GraphicsConfiguration gc = d.getDefaultConfiguration();
                Rectangle r = gc.getBounds();
                frame.setSize(r.width, r.height);
                        
                frame.setVisible(true);
                
                
                applet.start();
            }
        });
    }
    
    @Override
    public void init() {
        fxContainer = new JFXPanel();
        add(fxContainer);
        
        createVirtualKeyboard();
        
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
                createProgressOverlay();
                createGlassOverlay();
            }
        });
    }
    
//    private void showAutofocusData() {
//        if (autofocusData.containsKey("target")) {
//            HTMLInputElement target = (HTMLInputElement) autofocusData.get("target");
//            target.focus();
//            setCurrentTarget(target);
//            showVirtualKeyboard(target);
//        }
//    }
    
    public void showVirtualKeyboard(HTMLElement target) {
//        createKeyboard();
//        println("show virtual keyboard");
        if (customVkb != null) {
            customVkb.setTarget(target);
        }
        
//        customVkb.view().set
        
//        println("visible " + (keyboard != null? keyboard.isVisible() : "false"));
        if (root != null) {
            BorderPane main = (BorderPane) root.getChildren().get(0);
            StackPane top = (StackPane) main.getTop();
            if (top != null && !top.getChildren().contains(topGlassOverlay)) {
//                println("add top glass overlay");
                top.getChildren().add(topGlassOverlay);
            }

            StackPane bottom = (StackPane) main.getBottom();
            if (bottom != null && !bottom.getChildren().contains(bottomGlassOverlay)) {
//                println("add bottom glass overlay");
                bottom.getChildren().add(bottomGlassOverlay);
            }
//            println("top " + top + " bottom " + bottom);
        }
        
        if (virtualKeyboard == null || virtualKeyboard.isVisible()) return;
        
//        if (root != null && !root.getChildren().contains(glassOverlay)) {
//            root.getChildren().add(glassOverlay);
//        }
        
        GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = d.getDefaultConfiguration();
        Rectangle r = gc.getBounds();
        
        int x = (r.width - virtualKeyboard.getWidth()) / 2; 
        int y = r.height - virtualKeyboard.getHeight() - 30;//(virtualKeyboard.getHeight()/2);
        
        virtualKeyboard.setLocation(x, y);
        virtualKeyboard.setVisible(true);
    }
    
    public void hideVirtualKeyboard() {
        
        if (root != null) {
            BorderPane main = (BorderPane) root.getChildren().get(0);
            StackPane top = (StackPane) main.getTop();
            if (top != null && top.getChildren().contains(topGlassOverlay)) {
                top.getChildren().remove(topGlassOverlay);
            }
            
            StackPane bottom = (StackPane) main.getBottom();
            if (bottom != null && bottom.getChildren().contains(bottomGlassOverlay)) {
                bottom.getChildren().remove(bottomGlassOverlay);
            }
        }
        
        if (virtualKeyboard == null || !virtualKeyboard.isVisible()) return;
        
//        if (root != null && root.getChildren().contains(glassOverlay)) {
//            root.getChildren().remove(glassOverlay);
//        }
//        
//        if (customVkb != null) {
//            HTMLInputElement target = customVkb.getTarget();
//            target.blur();
//        }
        
        virtualKeyboard.setVisible(false);
    }
    
    private void createVirtualKeyboard() {
        customVkb = new CustomVirtualKeyboard();
        
        virtualKeyboard = new JDialog();
//        virtualKeyboard.setTitle("Keyboard");
        virtualKeyboard.setAlwaysOnTop(true);
        virtualKeyboard.setModalityType(Dialog.ModalityType.MODELESS);
        virtualKeyboard.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        virtualKeyboard.setFocusableWindowState(false);
        virtualKeyboard.setFocusable(false);
        virtualKeyboard.setFocusTraversalKeysEnabled(false);
        virtualKeyboard.setResizable(false);
        virtualKeyboard.setSize(900, 600);
        virtualKeyboard.setLocation(300, 200);
        
        
        JApplet xapplet = new JApplet();
        virtualKeyboard.setContentPane(xapplet.getContentPane());
        
        JFXPanel panel = new JFXPanel();
        xapplet.add(panel);
        
        BorderPane container = new BorderPane();
        container.setCenter(customVkb.view());
        
        Scene scene = new Scene(container);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/keyboard-theme.css").toExternalForm());
        
        panel.setScene(scene);
        
    }
    
    
    private String getHost() {
        
        String host = props.getProperty("app.host");
        if (host == null || host.trim().length() == 0) {
//            host = "192.168.254.123:8580";
//            host = "localhost:8080/index";
            host = "192.168.253.102:8580";
//            host = "www.google.com";
        }
        
        return host;
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
        
        rootContent.setTop(createHeader());
        rootContent.setCenter(createContent());
        rootContent.setBottom(createFooter());
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/kiosk-theme.css").toExternalForm());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/css/progress-overlay.css").toExternalForm());
        
        
        fxContainer.setScene(scene);
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
        progressOverlay.setCenter(progressContainer);
        
        progressContainer.setAlignment(Pos.CENTER);
        
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
//        glassOverlay = new BorderPane();
        String style = "-fx-background-color: orange;";
        EventHandler mouseClickedHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                hideVirtualKeyboard();
                HTMLElement target = getCurrentTarget();
                if (target instanceof HTMLInputElement) {
                    ((HTMLInputElement) target).blur();
                } else if (target instanceof HTMLTextAreaElement) {
                    ((HTMLTextAreaElement) target).blur();
                }
//                target.blur();
            }
        };
        
        topGlassOverlay = new Pane();
//        topGlassOverlay.setStyle(style);
        topGlassOverlay.setOnMouseClicked(mouseClickedHandler);
        
        bottomGlassOverlay = new Pane();
//        bottomGlassOverlay.setStyle(style);
        bottomGlassOverlay.setOnMouseClicked(mouseClickedHandler);
//        glassOverlay = new Pane();
//        glassOverlay.setStyle("-fx-background-color: orange;");
//        glassOverlay.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent e) {
//                hideVirtualKeyboard();
//                HTMLInputElement target = getCurrentTarget();
//                target.blur();
//            }
//        });
    }
    
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
        
        return redirect;
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
    
    private javafx.scene.Node createContent() {
        StackPane content = new StackPane();
//        webEngine.documentProperty().addListener(new WebDocumentListener(webEngine));
        
        WebView browser = new WebView();
        content.getChildren().add(browser);
        
        browser.setCache(true);
        browser.setContextMenuEnabled(getContextMenu());
        
        webEngine = browser.getEngine();
        history = webEngine.getHistory();
        
        btnBack.disableProperty().bind(history.currentIndexProperty().lessThanOrEqualTo(0));
        
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
        
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            
            public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document newDoc) {

                JSObject jsobj = (JSObject) webEngine.executeScript("window");
                jsobj.setMember("handler", handler);
                
                if (newDoc != null) {
//                    println("new document");
//                    JSObject doc = (JSObject) webEngine.executeScript("document");
//                    Object member = doc.getMember("handler");
//                    if (member == null || member.equals("undefined")) {
//                        doc.setMember("handler", handler);
//                    }
                    addDocumentListener(newDoc);
//                    synchronized (newDoc) {
//                        newDoc.notifyAll();
//                    }
//                    addHtmlDocumentListener(newDoc);
                    
                } else if (newDoc == null) {
                     try{
                        // Use reflection to retrieve the WebEngine's private 'page' field. 
                        Field f = webEngine.getClass().getDeclaredField("page"); 
                        f.setAccessible(true);
                        com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);  
                        page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB()); 

                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
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
        
        webEngine.setOnError(new EventHandler<WebErrorEvent>() {

            @Override
            public void handle(WebErrorEvent t) {
                println("error " + t);
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        Worker worker = webEngine.getLoadWorker();
        worker.progressProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> ov, Number oldProgress, Number newProgress) {
//                println("old progress " + oldProgress + " new progress " + newProgress);
                double value = newProgress.doubleValue();
//                println("value " + value + " old value " + oldProgress.doubleValue());
//                println("value " + value + " worker state-> " + worker.getState() + " " + !worker.getState().equals(Worker.State.SUCCEEDED));
//                if (value >= 0 && value < 1) {
//                    showProgress();
////                    if (progress != null) {
////                        progress.setProgress(value);
////                    }
//                } else if (value >= 1 || worker.getState().equals(Worker.State.SUCCEEDED)) {
//                    hideProgress();
//                }
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
                int idx = history.getCurrentIndex();
//                println("current idx-> " + idx);
//                println("history-> " + history.getEntries());
//                if (idx > 0) {
//                    addBackButton();
//                } else if (idx <= 0) {
//                    hideBackButton();
//                }
//                println("ot " + ot + " nt " + nt);
                JSObject jsobj = (JSObject) webEngine.executeScript("window");
                jsobj.setMember("handler", handler);
                
//                println("worker status-> " + nt);
                if (nt == Worker.State.SUCCEEDED) {
//                    addHtmlDocumentListener(webEngine.getDocument());
//                    showAutofocusData();
                    hideProgress();
                } else if (nt == Worker.State.FAILED || nt == Worker.State.CANCELLED) {
                    //prompt error
                    hideProgress();
                } else {
                    showProgress();
                }
            }
        
        });
        
        
        webEngine.load("http://" + getHost());
//        webEngine.load("http://192.168.254.23:8580");
//        webEngine.load("http://" + getHost() + "/binrpt");
//        webEngine.load("file:///D:/Web/Sample.html");
        
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
                if (history != null) {
                    history.go(-1);
                }
            }
            
//            public void handle(ActionEvent e) {
//                JSObject history = (JSObject) webEngine.executeScript("history");
//                if (history != null) {
//                    history.call("back");
//                }
//            }
            
//            public void handle(ActionEvent e) {
////                println("event-> " + e);
////                Platform.runLater(new Runnable() {
////                    public void run() {
////                        try {
//////                            int idx = getReturnToIndex();
//////                            int curridx = history.getCurrentIndex();
//////                            println("current index-> " + curridx);
//////                            if (curridx > 0) curridx--;
////                            //final int offset = (curridx - idx) * -1;
////                            //setReturnToIndex(curridx + offset);
////                            //history.go(offset);
//////                            println("current index2 " + curridx);
////                            history.go(-1);
////                            
//////                            history.getEntries().remove(root)
////                            //history.notifyAll();
//////                            history.getEntries().remove(0);
////                            //hideBackButton();
////                        } catch (Throwable t) {
////                            t.printStackTrace();
////                        }
////                    }
////                });
//                
//                
////                Platform.runLater(new Runnable() {
////                    public void run() {
////                        
////                    }
////                });
////                try {
////                    
//////                    ObservableList<Entry> list = history.getEntries();
//////                    println("history size " + list.size());
//////                    for (int i=0; i<list.size(); i++) {
//////                        println("idx-> " + i + " " + list.get(i));
//////                    }
//////                    
//////                    int idx = getReturnToIndex();
////////                    int curridx = history.getCurrentIndex();
//////                    int curridx = history.getEntries().size() - 1;
////////                    int curridx2 = history.getCurrentIndex();
////////                    println("idx-> " + idx + " curridx-> " + curridx);
//////                    final int offset = (curridx - idx) * -1;
////////                    println("offset-> " + offset + " "  + (curridx + offset));
//////                    setReturnToIndex(curridx + offset);
//////                    println("return to index " + getReturnToIndex());
////                    
//////                    Platform.runLater(new Runnable() {
//////                        public void run() {
//////                            history.go(-1);
////////                            hideBackButton();
//////                        }
//////                    });
//////                    println("current idx-> " + history.getCurrentIndex());
////                    
//////                    for (int i=(getReturnToIndex() + 1); i<list.size(); i++) {
////////                        println("idx-> " + i + " " + list.get(i));
//////                        list.remove(i);
//////                    }
////                } catch (Throwable tr) {
////                    tr.printStackTrace();
////                }
//            }
        
        });
        list.add(btnBack);
//        hideBackButton();
        
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
//        list.add(btnShowHistory);
        
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
    
    private javafx.scene.Node createHeader() {
//        BorderPane header = new BorderPane();
//        Pane header = new Pane();
        StackPane container = new StackPane();
        VBox headerContainer = new VBox();
        container.getChildren().add(headerContainer);
        
        HBox header = new HBox();
        header.setId("header");
        headerContainer.getChildren().add(header);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMinHeight(100);
//        header.setStyle("-fx-background-color: red;");
//        println(getClass().getClassLoader().getResource("resources/images/logo.png").toExternalForm());
        
        ImageView logo = new ImageView(new Image(getClass().getClassLoader().getResource("resources/images/kiosk-logo.png").toExternalForm()));
        Hyperlink home = new Hyperlink();
        home.setGraphic(logo);
        home.setBorder(Border.EMPTY);
        home.setOnAction(returnToHomeAction);
        
        header.getChildren().addAll(home);

        HBox toolbar = new HBox();
        toolbar.setId("header-toolbar");
        headerContainer.getChildren().add(toolbar);
        toolbar.setSpacing(5);
        toolbar.getChildren().addAll(getDefaultHeaderButtons());
//        header.setCenter(new Label("HEADER"));
        
        return container;
    }
    
    private javafx.scene.Node createFooter() {
        StackPane container = new StackPane();
        
        VBox footerContainer = new VBox();
        container.getChildren().add(footerContainer);
        footerContainer.setId("footer");
        
        HBox footer = new HBox();
        footerContainer.getChildren().add(footer);
//        footer.setPadding(new Insets(0, 60, 0, 60));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setMinHeight(100);
        
//        footer.setStyle("-fx-background-color: red; -fx-padding: 30px 60px 30px 60px;");
        
        ImageView logo = new ImageView(new Image(getClass().getClassLoader().getResource("resources/images/logo.png").toExternalForm()));
        Hyperlink home = new Hyperlink();
        home.setGraphic(logo);
        home.setBorder(Border.EMPTY);
        home.setOnAction(returnToHomeAction);
        
//        Button home = new Button("Home");
        
        footer.getChildren().addAll(home);
        
        return container;
    }
    
    private void addDocumentListener(Document d) {
        if (d == null) return;
        
        EventTarget et = (EventTarget) d;
        et.addEventListener("focus", focusListener, true);
        et.addEventListener("focusin", focusListener, true);
        et.addEventListener("load", loadListener, true);
//        et.addEventListener("click", clickListener, true);
        synchronized (d) {
            d.notifyAll();
        }
        
        boolean hasAutofocus = false;
        HTMLElement e = null;
        String[] strArray = {"input", "textarea"};
        for (int i=0; i<strArray.length; i++) {
            if (e == null) {
                NodeList list = d.getElementsByTagName(strArray[i]);
                e = getElementWithAutofocus(list);
            } else if (e != null) {
                NamedNodeMap attribs = e.getAttributes();
                
//                for (int ii=0; ii<attribs.getLength(); ii++) {
//                    Node n = attribs.item(ii);
//                    println("name-> " + n.getNodeName() + " val-> " + n.getNodeValue());
//                }
//                println("");
                
                Node n = attribs.getNamedItem("disablevirtualkeyboard");
                if (n == null) {
                    if (e instanceof HTMLInputElement) {
                        ((HTMLInputElement) e).focus();
                    } else if (e instanceof HTMLTextAreaElement) {
                        ((HTMLTextAreaElement) e).focus();
                    }
                    hasAutofocus = true;
                    break;
                } else if (n != null) {
                    e = null;
                }
            }
        }
        
        if (hasAutofocus == true) {
            showVirtualKeyboard(e);
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
    
    private void handleInput(HTMLElement i) {
//        println("handle input");
        //HTMLButtonElement b;
        if (i instanceof HTMLInputElement) {
            NamedNodeMap attribs = i.getAttributes();
            Node node = attribs.getNamedItem("type");
            if (node != null && !node.getNodeValue().toLowerCase().equals("text")) {
                return;
            }
        }
        
        ((EventTarget) i).addEventListener("focusout", focusoutListener, true);
        showVirtualKeyboard(i);
    }
    
     private EventListener focusListener = new EventListener() {

        @Override
        public void handleEvent(Event evt) {
//            println("event " + evt.getCurrentTarget());
//            println("event1 " + evt.getTarget());
//            println("event " + evt.getType());
            EventTarget et = evt.getTarget();
//            println("et " + et);
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
            
            node = attribs.getNamedItem("disablevirtualkeyboard");
            if (node != null) {
                return;
            }
            
            if (et instanceof HTMLInputElement || et instanceof HTMLTextAreaElement) {
                handleInput((HTMLElement) et);
            }
//            evt.
            
        }
    };
    
    private EventListener focusoutListener = new EventListener() {
        @Override
        public void handleEvent(Event evt) {
            EventTarget et = evt.getTarget();
            if (et instanceof HTMLInputElement) {
                ((HTMLInputElement) et).blur();
            } else if (et instanceof HTMLTextAreaElement) {
                ((HTMLTextAreaElement) et).blur();
            }
            hideVirtualKeyboard();
        }
    };
    
    private EventListener loadListener = new EventListener() {
        @Override
        public void handleEvent(Event evt) {
            EventTarget et = evt.getTarget();
            
            if (et instanceof HTMLIFrameElement) {
                HTMLIFrameElement iframe = (HTMLIFrameElement) et;
                Document d = iframe.getContentDocument();
                if (d != null) {
                    addDocumentListener(d);
//                    EventTarget et1 = (EventTarget) d;
//                    et1.addEventListener("focus", focusListener, true);
//                    et1.addEventListener("focusin", focusListener, true);
//                    addIframeLoadListeners(d);
                }
            }
        }
    };
    
    private EventListener clickListener = new EventListener() {
        @Override
        public void handleEvent(Event evt) {
            EventTarget et = evt.getTarget();
            
            //check if allowed to display back button
//            if (et instanceof HTMLButtonElement) {
//                HTMLButtonElement hb = (HTMLButtonElement) et;
//                checkToDisplayBackButton(hb);
//                
//                HTMLFormElement hf = hb.getForm();
//                checkToDisplayBackButton(hf);
//            }
        }
        
        
        private void checkToDisplayBackButton(HTMLElement e) {
            if (e == null) return;
            
            NamedNodeMap attribs = e.getAttributes();
            Node showBackButton = attribs.getNamedItem("showbackbutton");
            
//            if (showBackButton != null) {
//                addBackButton();
////                    et.addEventListener("click", new BackButtonListener(), true);
//            }
        } 
    };
    
//    private void addBackButton() {
//        //int idx = history.getCurrentIndex();
//        //setReturnToIndex(idx);
//        showBackButton();
//    }
    
//    private void xaddHtmlDocumentListener(Document document) {
//        if (document == null) return;
//        
////        println("add html listener");
//        NodeList list = document.getElementsByTagName("input");
////                    println("");
//////            Node node, attr;
//        Node node, type, disabled, readonly, autofocus;
//        NamedNodeMap attribs;
////                            println("list " + list.getLength());
//        boolean hasAutofocus = false;
//        autofocusData = new HashMap();
//        for (int j=0; j<list.getLength(); j++) {
//            node = list.item(j);
//            EventTarget et = (EventTarget) node;
//
//            attribs = node.getAttributes();
//            type = attribs.getNamedItem("type");
//            disabled = attribs.getNamedItem("disabled");
//            readonly = attribs.getNamedItem("readonly");
//            autofocus = attribs.getNamedItem("autofocus");
//            if (autofocus != null && !hasAutofocus) {
//                autofocusData.put("target", (HTMLInputElement) node);
//                hasAutofocus = true;
//            }
//            
//            if (type != null && type.getNodeValue().equals("text")) {
//                if (disabled != null || (readonly != null && readonly.getNodeValue().equals("true"))) {
//                    //do nothing
//                } else {
////                    et.addEventListener("focus", new VirtualKeyboardListener((HTMLInputElement) node), true);
////                    et.addEventListener("focusin", new VirtualKeyboardListener((HTMLInputElement) node), true);
////                    et.addEventListener("focusout", new VirtualKeyboardListener((HTMLInputElement) node), true);
//                }
//            }
//            
////            for (int xj=0; xj<attribs.getLength(); xj++) {
////                Node xn = attribs.item(xj);
////                println("attr " + xn.getNodeName() + " value-> " + xn.getNodeValue());
////            }
//        }
//        
//        list = document.getElementsByTagName("form");
////        println("form " + list.getLength());
//        Node showBackButton;
//        
//        for (int i=0; i<list.getLength(); i++) {
//            node = list.item(i);
//            EventTarget et = (EventTarget) node;
//            
//            attribs = node.getAttributes();
//            showBackButton = attribs.getNamedItem("showbackbutton");
//            
////            println("show back button attr " + showBackButton);
//            if (showBackButton != null) {
////                et.addEventListener("click", new BackButtonListener(), true);
//            }
//        }
//        
//        list = document.getElementsByTagName("button");
//        
//        for (int i=0; i<list.getLength(); i++) {
//            node = list.item(i);
//            EventTarget et = (EventTarget) node;
//            
//            attribs = node.getAttributes();
//            showBackButton = attribs.getNamedItem("showbackbutton");
//            
////            println("show back button attr " + showBackButton);
//            if (showBackButton != null) {
////                et.addEventListener("click", new BackButtonListener(), true);
//            }
//        }
//        
//    }    
    
    private static void println(String str) {
        System.out.println(str);
    }
    
    private String getBarcodeURL() {
    
        String url = props.getProperty("app.barcode.url");
        if (url == null || url.trim().length() == 0) {
//            url = "192.168.254.21:8080/index/billing";
            url = "192.168.253.102:8580/index/barcodeloader";
        }
//        println("host " + host);
        
        return url;
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
                        webEngine.load(host);
                    }
                }
            });
        }
    }
    
    public class BridgeHandler {

        private boolean allowBarcodeScanning = false;
        
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
//            println("print obj " + obj);
//            println("class " + obj.getClass().getCanonicalName());]
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
            for (; idx<keyList.size(); idx++) {
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
        
        public void setAllowBarcodeScanning(boolean allowBarcodeScanning) {
            this.allowBarcodeScanning = allowBarcodeScanning;
            println("allow barcode scanning " + allowBarcodeScanning);
        }
        
        public boolean getAllowBarcodeScanning() {
            return this.allowBarcodeScanning;
        }
        
        public void onpageload(String pagename) {
//            println("pagename " + pagename);
//            addHtmlDocumentListener(webEngine.getDocument());
            
        }
    }
    
//    private class xVirtualKeyboardListener implements EventListener {
//        
//        private HTMLInputElement target;
//        
//        public xVirtualKeyboardListener(HTMLInputElement target) {
//            this.target = target;
//        }
//
//        @Override
//        public void handleEvent(Event evt) {
//            String type = evt.getType();
////            println("event " + type + " node instanceof HTMLInputElement " + (this.target instanceof HTMLInputElement));
//            
////            println("type " + type);
//            if (type.matches("focus|focusin")) {
////                println("type " + type);
//                setCurrentTarget(target);
//                showVirtualKeyboard(target);
//            }
//            
//            if ("focusout".equals(type)) {
//                setCurrentTarget(null);
//                hideVirtualKeyboard();
//            }
////            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }
//    }
    
//    private class BackButtonListener implements EventListener {
//        
//        RamesesKiosk root = RamesesKiosk.this;
//        
//        public void handleEvent(Event e) {
//            String type = e.getType();
//            
////            println("type " + type);
//            if ("click".equals(type)) {
////                println("history size " + history.getEntries().size());
////                int idx = history.getEntries().size() - 1;
//                int idx = history.getCurrentIndex();
//                root.setReturnToIndex(idx);
//                root.showBackButton();
//            }
//        }
//    }
    
}
