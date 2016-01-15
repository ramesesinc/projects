package com.rameses.waterworks.util;

public class AndroidSystemPlatform extends SystemPlatform{

    @Override
    public System getSystem() {
         return new AndroidSystem();
    }
    
}
