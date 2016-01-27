package com.rameses.waterworks.util;

import dalvik.system.DexClassLoader;
import java.io.File;
import javafxports.android.FXActivity;

public class AndroidClassLoader implements ClassLoader {
    
    private Class class1;

    @Override
    public Class execute(File file, String packge) {
        class1 = null;
        try{
            DexClassLoader loader = new DexClassLoader(file.toURI().toURL().toString(), FXActivity.getInstance().getDir("dex", 0).getAbsolutePath(), null, FXActivity.getInstance().getClassLoader());
            class1 = loader.loadClass(packge);
        }catch(Exception e){
            e.printStackTrace();
        }
        return class1;
    }

    
}
