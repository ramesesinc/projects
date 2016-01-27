package com.rameses.waterworks.util;

import com.rameses.waterworks.log.LogPlatformFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassLoaderPlatformFactory {
    
    public static ClassLoaderPlatform getPlatform(){
        try{
            return (ClassLoaderPlatform) Class.forName(getPlatformClassName()).newInstance();
        }catch(Exception ex){
            Logger.getLogger(LogPlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static String getPlatformClassName(){
        switch ( java.lang.System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "com.rameses.waterworks.util.AndroidClassLoaderPlatform";
            default : return "com.rameses.waterworks.util.AndroidClassLoaderPlatform";
        }
    }
    
}
