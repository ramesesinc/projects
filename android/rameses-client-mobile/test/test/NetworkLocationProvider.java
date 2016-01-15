/*
 * NetworkLocationProvider.java
 *
 * Created on January 30, 2014, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.rameses.client.android.UIApplication;
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

    private Timer timer;
    private Location location; 
    private LocationFetcher fetcher; 
    
    private NetworkLocationProvider() {
        timer = new Timer(); 
    }

    protected void finalize() throws Throwable {
        disabled();         
        super.finalize();
    }
    
    private void enabled() { 
        timer.schedule(new LocationFetcher(), 0, 2000); 
    } 
    
    private void disabled() {
        try { timer.cancel(); } catch(Throwable t) {;} 
        try { timer.purge(); } catch(Throwable t) {;} 
    }
    
    
    private class LocationFetcher extends TimerTask 
    {
        private LocationManager locationMgr; 
        private DefaultLocationListener locationListener; 
        
        LocationFetcher() {
            Context uictx = null; //UIApplication.getInstance();
            locationMgr = (LocationManager) uictx.getSystemService(Context.LOCATION_SERVICE); 
            locationListener = new DefaultLocationListener(); 
        }
        
        public void run() {
            if (locationMgr == null) {
                System.out.println("LocationManager is not set");
                cancel(); 
                return; 
            }
            
            boolean netEnabled = false;
            boolean gpsEnabled = false;
            try { 
                netEnabled = locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
            } catch(Throwable t) {;}
            try { 
                gpsEnabled = locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER); 
            } catch(Throwable t) {;} 
            
            if (!gpsEnabled && !netEnabled) return;
            
            Location netLoc = null;
            Location gpsLoc = null;
            if (netEnabled) {
                locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener); 
                netLoc = locationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } 
            if (gpsEnabled) {
                locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                gpsLoc = locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } 
            if (netLoc == null && gpsLoc == null) return;
            
            
        }
    }
    
    private class DefaultLocationListener implements LocationListener 
    {
        public void onLocationChanged(Location location) {}
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    } 
}
