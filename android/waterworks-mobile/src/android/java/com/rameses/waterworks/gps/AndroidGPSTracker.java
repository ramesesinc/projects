package com.rameses.waterworks.gps;

import android.content.Context;
import javafxports.android.FXActivity;

public class AndroidGPSTracker implements GPSTracker {

    @Override
    public double getLatitude() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        GPSTrackerService tracker = new GPSTrackerService(ctx);
        return tracker.latitude;
    }

    @Override
    public double getLongitude() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        GPSTrackerService tracker = new GPSTrackerService(ctx);
        return tracker.longitude;
    }

    @Override
    public String getCountry() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        GPSTrackerService tracker = new GPSTrackerService(ctx);
        return tracker.getCountryName(ctx);
    }

    @Override
    public String getLocality() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        GPSTrackerService tracker = new GPSTrackerService(ctx);
        return tracker.getLocality(ctx);
    }

    @Override
    public String getPostalCode() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        GPSTrackerService tracker = new GPSTrackerService(ctx);
        return tracker.getPostalCode(ctx);
    }

    @Override
    public String getAddressLine() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        GPSTrackerService tracker = new GPSTrackerService(ctx);
        return tracker.getAddressLine(ctx);
    }
    
}
