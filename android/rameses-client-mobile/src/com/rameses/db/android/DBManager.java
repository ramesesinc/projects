/*
 * DBManager.java
 *
 * Created on January 28, 2014, 10:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.db.android;

import com.rameses.util.Base64Cipher;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @author wflores
 */
public final class DBManager 
{
    private static DBManager instance;
    private static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager(); 
        }
        return instance; 
    }
    
    static synchronized void bind(String name, AbstractDB db) {
        Map map = getInstance().databases;
        if (map.containsKey(name)) return;
        
        map.put(name, db); 
    }
    
    public static synchronized AbstractDB get(String name) {
        return getInstance().databases.get(name);
    }
    
    
    public static synchronized void setLogFileName(String name) {
        getInstance().loggername = name; 
    }
    
    static synchronized Logger getLogger() {
        return getInstance().logger; 
    }
    
    
    public static interface Logger {
        public void write(Map data);
    }    
    
    private Map<String, AbstractDB> databases; 
    private LoggerImpl logger;
    private String loggername;
    private Timer timer;
    
    private DBManager() {
        databases = new HashMap(); 
        logger = new LoggerImpl();        
        timer = new Timer();
        timer.schedule(new LoggerTask(logger), 0, 1000); 
    }
    
    private class LoggerImpl implements Logger, Runnable {
        
        private Vector<String> messages = new Vector(); 
        
        @Override
        public void write(Map data) {
            String encstr = new Base64Cipher().encode(data); 
            messages.addElement(encstr); 
        }

        @Override
        public void run() {
            /*
            FileWriter writer = null;
            try {
                String datetime = new java.sql.Timestamp(System.currentTimeMillis()).toString().replaceAll("-|:|\\.", ""); 
                String loggername = DBManager.getInstance().loggername; 
                if (loggername == null) { loggername = "dblog"; }
                
                String filename = loggername +"_"+ datetime.replaceAll(" ", ""); 
                writer = new FileWriter(filename, true); 
                while (!messages.isEmpty()) {
                    writer.write(messages.remove(0)); 
                    writer.write("\n"); 
                } 
                writer.flush(); 
            } catch(RuntimeException re) {
                throw re;
            } catch(Exception e) {
                throw new RuntimeException(e.getMessage(), e); 
            } finally {
                try { writer.close(); }catch(Throwable t){;} 
            } 
            * */
        }
    }
    
    private class LoggerTask extends TimerTask {
        private Runnable callback;
        
        LoggerTask(Runnable callback) {
            this.callback = callback;
        }
        
        @Override
        public void run() {
            callback.run(); 
        }
    }
}
