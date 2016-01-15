package com.rameses.waterworks.database;

import com.rameses.Main;

public class DatabasePlatformFactory {
    
    public static DatabasePlatform getPlatform(){
        try{
            return (DatabasePlatform) Class.forName(getPlatformClassName()).newInstance();
        }catch(Exception ex){
            Main.LOG.error("DatabasePlatformFactory Error: ", ex.toString());
            return null;
        }
    }
    
    public static String getPlatformClassName(){
        switch ( System.getProperty("javafx.platform", "desktop") ) {
            case "android": return "com.rameses.waterworks.database.AndroidDatabasePlatform";
            default : return "com.rameses.waterworks.database.AndroidDatabasePlatform";
        }
    }
    
}
