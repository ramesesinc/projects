/*
 * SuspendTimer.java
 *
 * Created on February 5, 2014, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import com.rameses.client.services.CommonService;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Timer;

/**
 *
 * @author wflores 
 */
class TimeTicker 
{
    private final static Object LOCKED = new Object();
    
    private UIApplication app;
    private Calendar calendar;
    private Date date; 
    private TaskImpl task;
    private Timer timer;
    
    public TimeTicker(UIApplication app) {
        this.app = app;
        timer = new Timer();
    }
    
    Date getDate() { return date; } 
    
    void start() {
        synchronized (LOCKED) { 
            Logger logger = Platform.getLogger();
            if (logger != null) logger.log("[TimeTicker] starting..."); 
            
            if (calendar == null) calendar = new GregorianCalendar(); 
            
            AppSettings settings = Platform.getApplication().getAppSettings();
            Map setMap = settings.getAll();
            if (setMap.containsKey("timedifference")) {
                long timediff = Long.parseLong(setMap.get("timedifference").toString());
                long xtimemillis = calendar.getTimeInMillis();
                xtimemillis += timediff;
                calendar.setTimeInMillis(xtimemillis);
            }
            
            timer.schedule(new TimeFetcher(), 0);
            if (task == null) { 
                task = new TaskImpl();
                
                changeCalendar();
                timer.schedule(task, 0, 1000);  
            } 
        } 
    }
    
    void restart() {
        synchronized (LOCKED) { 
            Logger logger = Platform.getLogger();
            if (logger != null) logger.log("[TimeTicker] restarting..."); 
            
            changeCalendar();
            timer.schedule(new TimeFetcher(), 0);
        } 
    }
    
    private void changeCalendar() {
        boolean isConnected = Platform.getApplication().getIsConnected();
        long timemillis = 0;
        System.out.println("[TimeTicker] is date synced " + Platform.getApplication().getIsDateSync());
        if (isConnected == true) {
            timemillis = getServerTime();
            Platform.getApplication().setIsDateSync(true);
        } else {
            timemillis = Platform.getApplication().getServerTime();
        }

        if (timemillis <= 0) { 
            date = null;
        } else {
            calendar.setTimeInMillis(timemillis);
        } 
    }
    
    private void dateChanged() {
        date = new java.sql.Timestamp(calendar.getTimeInMillis()); 
        Platform.getApplication().dateChanged(date);
    }
        
    private long getServerTime() {
        long timemillis = 0;
        try { 
            timemillis = new CommonService().getServerTime(); 
            
            Calendar cal = Calendar.getInstance();
            long xtimemillis = cal.getTimeInMillis();
            
            long diff = timemillis - xtimemillis;
            
            AppSettings settings = Platform.getApplication().getAppSettings();
            settings.put("timedifference", diff);
            settings.put("phonedate", cal.getTime().toString());
            
        } catch(Throwable t) { 
            t.printStackTrace();
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t);          
        } 
        return timemillis; 
    }
    
    private class TaskImpl extends Task 
    {
        TimeTicker root = TimeTicker.this;
        
        public void run() {
            try {
                execute();
            } catch(Throwable t) {
                t.printStackTrace(); 
            } 
        } 
        
//        private void println(String msg) {
//            System.out.println("[TaskImpl] " + msg);
//        }
        
        private void execute() {
            synchronized (root.LOCKED) {
                boolean isDateSync = Platform.getApplication().getIsDateSync();
                if (isDateSync == false) {
                    changeCalendar();
                }
//                boolean isConnected = Platform.getApplication().getIsConnected();
//                long timemillis = 0;
//                if (isConnected == true) {
//                    timemillis = getServerTime();
//                } else {
//                    timemillis = Platform.getApplication().getServerTime();
//                }
//                
////                println("is connected " + isConnected);
////                println("timemillis " + isConnected);
//                
//                if (timemillis <= 0) { 
//                    root.date = null;
//                } else {
//                    root.calendar.setTimeInMillis(timemillis); 
//                    root.dateChanged();
//                } 
                Calendar calendar = root.calendar; 
                calendar.add(Calendar.SECOND, 1); 
                root.dateChanged(); 
//                println("date " + calendar.getTime());
            } 
        }
    }
    
    private class TimeFetcher extends Task 
    {
        TimeTicker root = TimeTicker.this;
        
        public void run() {
            try {
                execute();
            } catch(Throwable t) {
                Logger logger = Platform.getLogger();
                if (logger != null) logger.log(t);  
            }             
        }
        
//        private void println(String msg) {
//            System.out.println("[TimeFetcher] " + msg);
//        }
        
        private void execute() {
            synchronized (root.LOCKED) { 
                boolean isDateSync = Platform.getApplication().getIsDateSync();
                if (isDateSync == false) {
                    changeCalendar();
                }
                
                Calendar calendar = root.calendar; 
                calendar.add(Calendar.SECOND, 1); 
                root.dateChanged(); 
//                boolean isConnected = Platform.getApplication().getIsConnected();
//                long timemillis = 0;
//                if (isConnected == true) {
//                    timemillis = getServerTime();
//                } else {
//                    timemillis = Platform.getApplication().getServerTime();
//                }
//                
////                println("is connected " + isConnected);
////                println("timemillis " + isConnected);
//                
//                if (timemillis <= 0) { 
//                    root.date = null;
//                } else {
//                    root.calendar.setTimeInMillis(timemillis); 
//                    root.dateChanged();
//                }
            } 
        }        
    }
}
