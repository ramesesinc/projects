package com.rameses.waterworks.gps;

public interface GPSTracker {
    
    public double getLatitude();
    
    public double getLongitude();
    
    public String getCountry();
    
    public String getLocality();
    
    public String getPostalCode();
    
    public String getAddressLine();
    
}
