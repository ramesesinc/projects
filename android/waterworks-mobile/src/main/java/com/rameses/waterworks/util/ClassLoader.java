package com.rameses.waterworks.util;

import java.io.File;

public interface ClassLoader {
    
    public Class execute(File file, String packge);
    
}
