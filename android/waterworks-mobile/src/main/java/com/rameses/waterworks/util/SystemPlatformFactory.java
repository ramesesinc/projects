package com.rameses.waterworks.util;

import com.rameses.waterworks.log.LogPlatform;
import com.rameses.waterworks.log.LogPlatformFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemPlatformFactory {
    
    public static SystemPlatform getPlatform(){
        try{
            return (SystemPlatform) Class.forName(getPlatformClassName()).newInstance();
        }catch(Exception ex){
            Logger.getLogger(LogPlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static String getPlatformClassName(){
        switch ( java.lang.System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "com.rameses.waterworks.util.AndroidSystemPlatform";
            default : return "com.rameses.waterworks.util.AndroidSystemPlatform";
        }
    }
    
}
