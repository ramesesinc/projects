package com.rameses.waterworks.log;

public interface Log {
    
    public void verbose(String s1, String s2);
    
    public void debug(String s1, String s2);
    
    public void info(String s1, String s2);
    
    public void warning(String s1, String s2);
    
    public void error(String s1, String s2);
    
}
