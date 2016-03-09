package com.rameses.waterworks.printer;

import bsh.EvalError;
import bsh.Interpreter;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrinterHandler{
    
    String data;
    String error = "";
    
    public PrinterHandler(Account a){
        String rdata = SystemPlatformFactory.getPlatform().getSystem().getReportData();
        try{
            Interpreter i = new Interpreter();
            i.set("a", a);
            i.set("userfullname",SystemPlatformFactory.getPlatform().getSystem().getFullName());
            i.set("datetime",SystemPlatformFactory.getPlatform().getSystem().getDate()+" "+SystemPlatformFactory.getPlatform().getSystem().getTime());
            Object o = i.eval(rdata);
            data = o.toString();
        }catch(EvalError e){
            error = "Error in Report Data: " + e.toString();
        }
    }

    public String getData() {
        return data;
    }
    
    public String getError(){
        return error;
    }
    
}
