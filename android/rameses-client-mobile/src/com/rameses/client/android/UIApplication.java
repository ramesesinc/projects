/*
 * UIApplication.java
 *
 * Created on January 29, 2014, 5:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.app.Application;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import com.rameses.client.interfaces.AppLoader;
import com.rameses.client.interfaces.AppLoaderCaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author wflores
 */
public abstract class UIApplication extends Application 
{
    private UIMain uimain;
    private UIActionBarMain uiactionbarmain;
    private Properties appenv;
    private AppSettings appSettings;
    private Logger logger;
    
    private AbstractActivity currentActivity;
    private AbstractActionBarActivity currentActionBarActivity;
    private LocationManager locationMgr;
    private ConnectivityManager connMgr;
    
    private AppLoaderCallerImpl appLoaderCaller;
    private SuspendTimer suspendTimer;
    private TimeTicker timeTicker;
    
    private boolean isDateSync = false;
    
    public UIApplication() {
        super(); 
        appenv = new Properties(); 
        Platform.setApplication(this); 
        //create logger
        logger = createLogger(); 
        //create application settings
        appSettings = createAppSettings();
        if (appSettings == null) { 
            appSettings = new AppSettings.DefaultImpl(); 
        } 
        appSettings.init();
        //initialize this application
        init();
    }

    protected void init() {}
    
    public UIMain getMainActivity() { return uimain; } 
    synchronized void setMainActivity(UIMain uimain) { 
        this.uimain = uimain;  
    } 
    
    public UIActionBarMain getActionBarMainActivity() { return uiactionbarmain; }
    synchronized void setActionBarMainActivity(UIActionBarMain uiactionbarmain) {
        this.uiactionbarmain = uiactionbarmain;
    }
    
    public AbstractActivity getCurrentActivity() { return currentActivity; } 
    synchronized void setCurrentActivity(AbstractActivity currentActivity) {
        AbstractActivity old = this.currentActivity;
        try { 
            if (old != null && currentActivity != null && !old.equals(currentActivity)) {
                old.processActivityChanged(); 
            } 
        } catch(Throwable t) {
            t.printStackTrace(); 
        }
        
        this.currentActivity = currentActivity;
    }
    
    public AbstractActionBarActivity getCurrentActionBarActivity() { return currentActionBarActivity; }
    synchronized void setCurrentActionBarActivity(AbstractActionBarActivity currentActionBarActivity) {
        AbstractActionBarActivity old = this.currentActionBarActivity;
        try {
            if (old != null && currentActionBarActivity != null && !old.equals(currentActionBarActivity)) {
                old.processActivityChanged();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        this.currentActionBarActivity = currentActionBarActivity;
    }
    
    public abstract File getLogFile();
    
    public Map getAppEnv() { return appenv; }    
    public Logger getLogger() { return logger; }
    protected Logger createLogger() { return null; }     

    public AppSettings getAppSettings() { return appSettings; } 
    protected AppSettings createAppSettings() { return null; }
    
    public LocationManager getLocationManager() {
        if (locationMgr == null) {
            locationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);	
        }
        return locationMgr; 
    }
    
    public ConnectivityManager getConnectivityManager() {
        if (connMgr == null) {
            connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);	
        }
        return connMgr; 
    }    
    
    
    protected void onCreateProcess() {}
    
    public final void onCreate() {
        super.onCreate();
        onCreateProcess(); 
    }

    protected void onTerminateProcess() {} 
    
    public final void onTerminate() {
        try { 
            onTerminateProcess(); 
        } catch(Throwable t) { 
            t.printStackTrace(); 
        } 
        super.onTerminate();
    } 

    protected void beforeLoad(Properties appenv) {}    
    protected void afterLoad() {}
        
    public final void load() {
        beforeLoad(appenv); 
        println("appenv: " + appenv);
        
        /*
        SessionContext sess = null;
        if (appenv.containsKey("session")) {
            sess = (SessionContext) appenv.get("session");
            appenv.remove(("session"));
        }
        println("session: " + sess);
        */
        
        //create application context
        AppContextImpl ac = new AppContextImpl(appenv);
        if (appenv.getProperty("readTimeout") == null) {
            appenv.put("readTimeout", "20000");
        } 
        //clean up client context
        ClientContext cctx = new ClientContext(this, ac);
        ClientContext.setCurrentContext(cctx); 
                
        if (suspendTimer == null) { 
            suspendTimer = new SuspendTimer(this); 
        } else { 
            suspendTimer.pause(); 
        } 
        loadAppLoaders(); 
        afterLoad(); 
    } 
    
    private void println(String str) {
        System.out.println("[UIApplication] " + str);
    }
    
    protected void afterLogout() {
    }
    
    public final void logout() {
        ClientContext.setCurrentContext(null);
        if (uimain != null) uimain.unregister(); 
        if (uiactionbarmain != null) uiactionbarmain.unregister();
        Platform.getInstance().disposeAll(); 
        afterLogout();
    } 

    public void suspend() {
    }
    
    public final void pauseSuspendTimer() {
        if (suspendTimer != null) suspendTimer.pause();
    }
    
    public final void resumeSuspendTimer() { 
        if (suspendTimer != null) suspendTimer.resume(); 
    } 
    
    public final void restartSuspendTimer() {
        if (suspendTimer != null) suspendTimer.restart();
    }
    
    public final void suspendSuspendTimer() {
        if (suspendTimer != null) suspendTimer.suspend();
    }
    
    public final void syncServerDate() {
        println("timeticker " + timeTicker);
        if (timeTicker == null) {
            timeTicker = new TimeTicker(this);
            timeTicker.start(); 
        } else { 
            timeTicker.restart(); 
        } 
        if (timeTicker != null) {
            println("date " + timeTicker.getDate());
        }
    } 
    
    protected boolean getIsConnected() { return true; }
    
    public boolean getIsDateSync() { return isDateSync; }
    public void setIsDateSync(boolean isDateSync) {
        this.isDateSync = isDateSync;
    }
    
    protected long getServerTime() { return 0L; }
    
    protected void dateChanged(Date date) {}
    
    public final Date getServerDate() {
        Logger logger = Platform.getLogger();
        if (logger != null) logger.log("[Platform.getServerDate] timeTicker="+timeTicker);
        return (timeTicker == null? null: timeTicker.getDate()); 
    }     
    
    private void loadAppLoaders() {
        List<AppLoader> list = new ArrayList(); 
        Iterator itr = Service.providers(AppLoader.class); 
        while (itr.hasNext()) {
            AppLoader loader = (AppLoader) itr.next(); 
            list.add(loader); 
        }
        //sort according to its index
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                int idx1 = ((AppLoader) o1).getIndex();
                int idx2 = ((AppLoader) o2).getIndex();
                if (idx1 < idx2) return -1;
                else if (idx1 > idx2) return 1;
                else return 0;                
            }
            
            public boolean equals(Object obj) {
                return (obj instanceof AppLoader); 
            }
        }); 
        
        appLoaderCaller = new AppLoaderCallerImpl(list);
        appLoaderCaller.resume();        
    }
    
    
    public void resumeAppLoader() {
        if (appLoaderCaller == null) return;
        
        appLoaderCaller.resume();
    }
    
    private class AppLoaderCallerImpl implements AppLoaderCaller
    {
        UIApplication root = UIApplication.this;
        
        private List<AppLoader> loaders;
        private int index;
        
        public AppLoaderCallerImpl(List<AppLoader> loaders) {
            this.loaders = loaders;
            this.index = 0;
        }
        
        public void resume() { 
            AppLoader current = null;
            try { 
                current = loaders.get(index); 
            } catch(Throwable t) {;} 

            if (current == null) {
                root.appLoaderCaller = null;
                done();
                return;
            }
            
            index += 1;
            current.setCaller(this); 
            current.load();
        }
        
        private void done() {
            
        }
    }    
}
