/*
 * NetworkLocationProvider.java
 *
 * Created on January 30, 2014, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author wflores 
 */
public final class NetworkLocationProvider 
{
    private static Object LOCKED = new Object();
    private static NetworkLocationProvider instance; 
    
    static {
        synchronized (LOCKED) {
            if (instance == null) {
                instance = new NetworkLocationProvider();  
            } 
        }
    }
        
    public static synchronized void setEnabled(boolean enabled) {
        if (enabled) {
            instance.enabled();
        } else {
            instance.disabled();
        }
    }
    
    public static synchronized Location getLocation() {
        return instance.location; 
    }
    
    public static synchronized void setDebug(boolean debug) {
        instance.debug = debug; 
    }
    
    private Timer timer;
    private Location location;     
    private LocationManager locationMgr;
    private LocationFetcher locationFetcher;
    private boolean debug;
    
    private NetworkLocationProvider() {
        timer = new Timer();
    }

    protected void finalize() throws Throwable {
        disabled();         
        super.finalize();
    }
    
    private void enabled() {
        if (locationFetcher == null) { 
            locationFetcher = new LocationFetcher(); 
            timer.schedule(locationFetcher, 0, 5000);  
        } 
    } 
    
    private void disabled() {
        try { locationFetcher.cancel(); } catch(Throwable t) {;} 
        try { locationFetcher.close(); } catch(Throwable t) {;} 
        try { timer.purge(); } catch(Throwable t) {;}  

        locationFetcher = null; 
    }
        
    private LocationManager getLocationManager() {
        if (locationMgr == null) {
            UIApplication uiapp = Platform.getApplication();
            if (uiapp == null) return null;

            locationMgr = (LocationManager) uiapp.getSystemService(Context.LOCATION_SERVICE); 
        } 
        return locationMgr; 
    }
    
    private void setLocation(Location location) {
        synchronized (LOCKED) { 
            //dump("[NetworkLocationProvider] location-> " + location); 
            this.location = location; 
        } 
    }
    
    private void dump(String message) {
        if (!debug) return;
        
        Logger logger = Platform.getLogger();
        if (logger == null) {
            System.out.println(message);    
        } else { 
            logger.log(message); 
        }
    }
    
    
    private class LocationFetcher extends TimerTask 
    {
        NetworkLocationProvider root = NetworkLocationProvider.this; 
        
        NetworkHandler nethandler;
        GPSHandler gpshandler;
        
        public void run() {
            try {
                dump("[LocationFetcher] started...");  
                /*UIMain uimain = Platform.getMainActivity(); 
                uimain.getHandler().post(new Runnable() {
                    public void run() { 
                        try { 
                            execute();  
                        } catch(Throwable t) {
                            if (root.debug) t.printStackTrace(); 
                        } 
                    } 
                }); */
                UIActionBarMain uimain = Platform.getActionBarMainActivity();
                if (uimain != null) {
                    uimain.getHandler().post(new Runnable() {
                        public void run() {
                            try { 
                                execute();  
                            } catch(Throwable t) {
                                t.printStackTrace();
                                //if (root.debug) t.printStackTrace(); 
                            } 
                        }
                    }); 
                }
            } catch(Throwable t) {
                t.printStackTrace();
                //if (root.debug) t.printStackTrace();
                
            } finally {
                dump("[NetLocationFetcher] ended...");  
            }            
        }
        
        private void close() { 
            try { nethandler.disconnect(); } catch(Throwable t) {;} 
            try { gpshandler.disconnect(); } catch(Throwable t) {;} 
        } 
        
        private void execute() throws Exception {
            LocationManager lm = root.getLocationManager();
            if (lm == null) {
                dump("LocationManager is not set");
                return; 
            }
             
            boolean netEnabled = isProviderEnabled(lm, LocationManager.NETWORK_PROVIDER); 
            boolean gpsEnabled = isProviderEnabled(lm, LocationManager.GPS_PROVIDER); 
            
            
            if (netEnabled) {
                if (nethandler == null) {
                    nethandler = new NetworkHandler(); 
                    try { 
                        nethandler.connect(); 
                    } catch(Throwable t) {
                        //if (root.debug) t.printStackTrace();
                        t.printStackTrace();
                        nethandler = null;                    
                    }                     
                } 
                //do not process below
                return;
            } else if (nethandler != null) {
                try { 
                    nethandler.disconnect(); 
                } catch(Throwable t) {;} 
                
                nethandler = null; 
            } 
            
            if (gpsEnabled) {
                if (gpshandler == null) {
                    gpshandler = new GPSHandler(); 
                    try { 
                        gpshandler.connect(); 
                    } catch(Throwable t) {
                        //if (root.debug) t.printStackTrace();
                        t.printStackTrace();
                        
                        gpshandler = null;                    
                    } 
                }             
            } else if (gpshandler != null) {
                try { 
                    gpshandler.disconnect(); 
                } catch(Throwable t) {;} 
                
                gpshandler = null;                 
            }
        }
        
        private boolean isProviderEnabled(LocationManager lm, String provider) {
            try { 
                return lm.isProviderEnabled(provider); 
            } catch(Throwable t) { 
                dump("[NetLocationFetcher] failed to check "+provider+" provider enabled caused by " + t.getMessage()); 
                return false; 
            } 
        }
    }
    
    private static final LocationRequest REQUEST = LocationRequest.create()
                    .setInterval(3500)         // 5 seconds
                    .setFastestInterval(16)    // 16ms = 60fps
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    
    private class NetworkHandler implements ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener 
    {
        NetworkLocationProvider root = NetworkLocationProvider.this; 
        LocationClient locationClient;
        
        void connect() {
            if (locationClient == null) {
                //UIMain uimain = Platform.getMainActivity();
                UIActionBarMain uimain = Platform.getActionBarMainActivity();
                locationClient = new LocationClient(uimain.getApplicationContext(), this, this);
            }
            if (!locationClient.isConnected()) {
                locationClient.connect();
            }
        }
        
        void disconnect() {
            try { 
                locationClient.disconnect(); 
            } catch(Throwable t) {
                //if (root.debug) t.printStackTrace();
                t.printStackTrace();
            }            
        }        

        public void onConnectionFailed(ConnectionResult result) {} 
        
        public void onDisconnected() {} 
        
        public void onConnected(Bundle bundle) { 
            locationClient.requestLocationUpdates(root.REQUEST, this); 
        }

        public void onLocationChanged(Location newloc) {
            root.setLocation(newloc); 
        }
    }
    
    private class GPSHandler implements LocationListener
    {
        NetworkLocationProvider root = NetworkLocationProvider.this; 
        boolean listener_binded;

        void connect() {
            LocationManager lm = root.getLocationManager();
            if (lm == null) {
                dump("LocationManager is not set");
                return; 
            }  
            
            if (listener_binded) {
                //since its binded already, do not process the codes below 
                return;
            }
            
            try {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); 
                listener_binded = true; 
            } catch(Throwable t) { 
                dump("[GPSHandler] failed to request location updates caused by " + t.getMessage());
            } 
        }
        
        void disconnect() {
            try {
                LocationManager lm = root.getLocationManager();
                if (lm != null) lm.removeUpdates(this);
            } catch(Throwable t) {;} 
        } 

        public void onLocationChanged(Location newloc) {            
            root.setLocation(newloc);
        }

        public void onStatusChanged(String string, int i, Bundle bundle) {}
        public void onProviderEnabled(String string) {}
        public void onProviderDisabled(String string) {}
    }    
}
