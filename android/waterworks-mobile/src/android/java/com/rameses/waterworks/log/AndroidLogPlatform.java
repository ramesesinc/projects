package com.rameses.waterworks.log;

public class AndroidLogPlatform extends LogPlatform{

    @Override
    public Log getLog() {
        return new AndroidLog();
    }
    
}
