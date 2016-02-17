package com.rameses;

import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.layout.Footer;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.log.Log;
import com.rameses.waterworks.log.LogPlatformFactory;
import com.rameses.waterworks.page.Login;
import com.rameses.waterworks.page.PreTerminalRegistration;
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
    public static BluetoothPort PRINTER;

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
        
        Scene scene = new Scene(PAGE, WIDTH, HEIGHT);
        scene.getStylesheets().add("css/Style1.css");
        
        if(Main.HEIGHT > 800){
            scene.getStylesheets().add("css/Style1.css");
        }else{
            scene.getStylesheets().add("css/Style2.css");
        }

        stage.setScene(scene);
        stage.show();
    }
    
    private void loadSysVar(){
        CONNECTION_SETTING = new HashMap();
        USER_SETTING = new HashMap();
        //loadsysvar
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
        }
    }

}
