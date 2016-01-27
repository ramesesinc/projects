package com.rameses.waterworks.gps;

import com.rameses.Main;

public class GPSTrackerPlatformFactory {
    
    public static GPSTrackerPlatform getPlatform(){
        try{
            return (GPSTrackerPlatform) Class.forName(getPlatformClassName()).newInstance();
        }catch(Exception ex){
            Main.LOG.error("DatabasePlatformFactory Error: ", ex.toString());
            return null;
        }
    }
    
    public static String getPlatformClassName(){
        switch ( System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "com.rameses.waterworks.gps.AndroidGPSTrackerPlatform";
            default : return "com.rameses.waterworks.gps.AndroidGPSTrackerPlatform";
        }
    }
    
}
