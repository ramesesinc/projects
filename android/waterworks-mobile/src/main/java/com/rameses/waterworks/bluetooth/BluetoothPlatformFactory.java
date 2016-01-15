package com.rameses.waterworks.bluetooth;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BluetoothPlatformFactory {
    
    public static BluetoothPlatform getPlatform(){
        try{
            return (BluetoothPlatform) Class.forName(getPlatformClassName()).newInstance();
        }catch(Exception e){
            Logger.getLogger(BluetoothPlatformFactory.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public static String getPlatformClassName(){
        switch ( System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "com.rameses.waterworks.bluetooth.AndroidBluetoothPlatform";
            default : return "com.rameses.waterworks.bluetooth.AndroidBluetoothPlatform";
        }
    }
    
}
