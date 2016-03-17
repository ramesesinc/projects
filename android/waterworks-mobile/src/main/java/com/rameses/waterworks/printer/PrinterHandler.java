package com.rameses.waterworks.printer;

import com.rameses.waterworks.bean.Account;

public interface PrinterHandler {
    
    public String getName();
    
    public String getData(Account a);
    
    public String getScriptCode();
    
    public String getError();
    
}
