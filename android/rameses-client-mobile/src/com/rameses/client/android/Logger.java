/*
 * Logger.java
 *
 * Created on January 30, 2014, 9:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.os.Environment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @author wflores 
 */
public class Logger 
{
    private static Logger instance; 
    public static synchronized Logger create(String name) {
        if (instance == null) {
            File dir = Environment.getExternalStorageDirectory();
            instance = new Logger(new File(dir, name)); 
        }         
        return instance; 
    }
    
    public static synchronized Logger getLogger() {
        return instance; 
    }

    //private Timer timer;
    private File file;
    private Vector<String> messages;
    private boolean enabled;
    
    private Logger(File file) {
        this.file = file; 
        messages = new Vector();
        //timer = new Timer(); 
        //timer.schedule(new TaskImpl(), 0, 500); 
    } 
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled; 
    }

    public void log(Object o) {
        java.util.Date time = new java.sql.Timestamp(System.currentTimeMillis()); 
        if (o instanceof Throwable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            ((Throwable) o).printStackTrace(new PrintStream(baos));  
            byte[] bytes = baos.toByteArray();
            try { baos.close(); } catch(Throwable t){;} 
            
            String msg = time + ": " + new String(bytes);
            System.out.println(msg);
            //if (enabled) messages.addElement(msg);
        } else { 
            String msg = time + ": " + o;
            System.out.println(msg);
            //if (enabled) messages.addElement(msg);
        }
    }
    
    protected void finalize() throws Throwable {
        super.finalize(); 
        
//        try { 
//            timer.cancel(); 
//        } catch(Throwable t){;} 
        messages.clear();
//        timer = null;        
        messages = null;
    }
    
    private class TaskImpl extends TimerTask 
    {
        Logger root = Logger.this;
        
        public void run() {
            if (!root.enabled) return;
            
            FileWriter writer = null;
            try {
                writer = new FileWriter(file, true); 
                while (!root.messages.isEmpty()) {
                    writer.write(root.messages.remove(0)); 
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
        }        
    }
}
