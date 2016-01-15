package com.rameses.waterworks.log;

public class AndroidLog implements Log{

    @Override
    public void verbose(String s1, String s2) {
        android.util.Log.v(s1, s2);
    }

    @Override
    public void debug(String s1, String s2) {
        android.util.Log.d(s1, s2);
    }

    @Override
    public void info(String s1, String s2) {
        android.util.Log.i(s1, s2);
    }

    @Override
    public void warning(String s1, String s2) {
        android.util.Log.w(s1, s2);
    }

    @Override
    public void error(String s1, String s2) {
        android.util.Log.e(s1, s2);
    }
    
}
