/*
 * SuspendTimer.java
 *
 * Created on February 5, 2014, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

/**
 *
 * @author wflores 
 */
public class SuspendTimer 
{
    private final static Object LOCKED = new Object();
    private final static int DEFAULT_TIMEOUT = 3;
    
    private UIApplication app;
    private Calendar calendar;
    private Date dtstarted; 
    private Date dtexpiry; 
    private TaskImpl task;
    private Timer timer;
    private boolean paused;
    
    public SuspendTimer(UIApplication app) {
        this.app = app;
        timer = new Timer();
    }
    
    void restart() {
        synchronized (LOCKED) { 
            int timeout = DEFAULT_TIMEOUT; 
            try { 
                timeout = app.getAppSettings().getSessionTimeout();
            } catch(Throwable t) {;} 

            paused = true;
            if (calendar == null) calendar = new GregorianCalendar(); 
            calendar.setTimeInMillis(System.currentTimeMillis()); 
            long timemillis = calendar.getTimeInMillis(); 
            calendar.add(Calendar.MINUTE, timeout);
            dtexpiry = calendar.getTime();
            calendar.setTimeInMillis(timemillis);        
            paused = false;
            if (task == null) { 
                task = new TaskImpl(); 
                timer.schedule(task, 0, 1000);  
            } 
        } 
    }

    void pause() { 
        synchronized (LOCKED) { 
            paused = true; 
        } 
    } 
    
    void resume() {
        synchronized (LOCKED) { 
            if (task == null) return;

            int timeout = DEFAULT_TIMEOUT; 
            try { 
                timeout = app.getAppSettings().getSessionTimeout();
            } catch(Throwable t) {;} 

            if (calendar == null) calendar = new GregorianCalendar();        
            long timemillis = calendar.getTimeInMillis();        
            calendar.add(Calendar.MINUTE, timeout);
            dtexpiry = calendar.getTime();
            calendar.setTimeInMillis(timemillis); 
            paused = false;
        } 
    }
    
    void reset() {
        synchronized (LOCKED) { 
            int timeout = DEFAULT_TIMEOUT; 
            try { 
                timeout = app.getAppSettings().getSessionTimeout();
            } catch(Throwable t) {;} 

            if (calendar == null) calendar = new GregorianCalendar();        
            long timemillis = calendar.getTimeInMillis();        
            calendar.add(Calendar.MINUTE, timeout);
            dtexpiry = calendar.getTime();
            calendar.setTimeInMillis(timemillis); 
            paused = false;
        } 
    }
    
    void suspend() {
        synchronized (LOCKED) { 
            paused = true; 
            timer.schedule(new SuspendActionProcess(), 0); 
        } 
    }
    
    private class TaskImpl extends Task 
    {
        SuspendTimer root = SuspendTimer.this;
        
        public void run() {
            try {
                execute();
            } catch(Throwable t) {
                t.printStackTrace(); 
            } 
        }
        
        private void execute() {
            synchronized (root.LOCKED) { 
                if (root.calendar == null) return;

                root.calendar.add(Calendar.SECOND, 1);    
                if (root.paused || root.dtexpiry == null) return;

                Date date = root.calendar.getTime();
                boolean valid = date.before(root.dtexpiry);
                if (valid) return; 

                System.out.println("run suspend action process");
                root.timer.schedule(new SuspendActionProcess(), 0);
                root.paused = true;
            } 
        }
    }
    
    private class SuspendActionProcess extends Task 
    {
        SuspendTimer root = SuspendTimer.this;
        
        public void run() {
            try {
                root.app.suspend(); 
            } catch(Throwable t) {
                t.printStackTrace();
            }
        } 
    }
    
}
