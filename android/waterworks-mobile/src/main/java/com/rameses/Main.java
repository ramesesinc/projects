package com.rameses;

import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.layout.Footer;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.log.Log;
import com.rameses.waterworks.log.LogPlatformFactory;
import com.rameses.waterworks.page.Login;
import com.rameses.waterworks.page.PreTerminalRegistration;
import com.rameses.waterworks.printer.OneilPrinterHandler;
import com.rameses.waterworks.printer.PrinterHandler;
import com.rameses.waterworks.printer.ZebraPrinterHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static double WIDTH, HEIGHT;
    public static Map CONNECTION_SETTING,USER_SETTING;
    public static BorderPane ROOT;
    public static Node prevScreen;
    public static String prevTitle;
    public static Log LOG;
    public static StackPane PAGE;
    public static Map MYACCOUNT;
    public static String PRINTERNAME = "";
    public static PrinterHandler PRINTERHANDLER = null;
    public static BluetoothPort PRINTER;
    public static List<Rule> RULES;

    @Override
    public void start(Stage stage) {
        LOG = LogPlatformFactory.getPlatform().getLog();
        
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = visualBounds.getWidth();
        HEIGHT = visualBounds.getHeight();
        
        PAGE = new StackPane();
        PAGE.setAlignment(Pos.CENTER);
        
        ROOT = new BorderPane();
        ROOT.setId("ROOT");
        ROOT.setTop(new Header().getLayout());
        ROOT.setBottom(new Footer().getLayout());
        ROOT.setCenter(new PreTerminalRegistration().getLayout());
        PAGE.getChildren().add(ROOT);
     
        loadSysVar();
        loadRules();
        
        System.out.println("Width: " + WIDTH + "   Height : " + HEIGHT);
        
        Scene scene = new Scene(PAGE, WIDTH, HEIGHT);
        scene.getStylesheets().add(Main.HEIGHT > 700 ? "css/Style1.css" : "css/Style2.css");

        stage.setScene(scene);
        stage.show();
    }
    
    private void loadSysVar(){
        CONNECTION_SETTING = new HashMap();
        USER_SETTING = new HashMap();
        
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        List<Setting> settings = db.getAllSettings();
        Iterator<Setting> it = settings.iterator();
        while(it.hasNext()){
            Setting setting = it.next();
            if(setting.getName().equals("ip")) CONNECTION_SETTING.put("ip", setting.getValue());
            if(setting.getName().equals("port")) CONNECTION_SETTING.put("port", setting.getValue());
            if(setting.getName().equals("timeout")) CONNECTION_SETTING.put("timeout", setting.getValue());
            if(setting.getName().equals("printer")) PRINTERNAME = setting.getValue();
            if(setting.getName().equals("registered")) ROOT.setCenter(new Login().getLayout());
            if(setting.getName().equals("handler")){
                String h = setting.getValue();
                if(h.equals("ZEBRA")) PRINTERHANDLER = new ZebraPrinterHandler();
                if(h.equals("ONEIL")) PRINTERHANDLER = new OneilPrinterHandler();
            }
        }
    }
    
    public static void loadRules(){
        Database db = DatabasePlatformFactory.getPlatform().getDatabase();
        RULES = db.getRules();
    }

}
