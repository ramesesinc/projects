package com.rameses.waterworks.gps;

public class AndroidGPSTrackerPlatform implements GPSTrackerPlatform {

    @Override
    public GPSTracker getGPSTracker() {
        return new AndroidGPSTracker();
    }
    
}
