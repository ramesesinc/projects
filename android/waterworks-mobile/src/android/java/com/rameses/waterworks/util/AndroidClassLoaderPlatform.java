package com.rameses.waterworks.util;

public class AndroidClassLoaderPlatform extends ClassLoaderPlatform{

    @Override
    public ClassLoader getClassLoader() {
        return new AndroidClassLoader();
    }
    
}
