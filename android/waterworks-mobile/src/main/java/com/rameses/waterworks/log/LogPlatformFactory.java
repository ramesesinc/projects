package com.rameses.waterworks.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogPlatformFactory {
    
    public static LogPlatform getPlatform(){
        try{
            return (LogPlatform) Class.forName(getPlatformClassName()).newInstance();
        }catch(Exception ex){
            Logger.getLogger(LogPlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static String getPlatformClassName(){
        switch ( System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "com.rameses.waterworks.log.AndroidLogPlatform";
            default : return "com.rameses.waterworks.log.AndroidLogPlatform";
        }
    }
    
}
