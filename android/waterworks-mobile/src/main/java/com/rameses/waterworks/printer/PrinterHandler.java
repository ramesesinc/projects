package com.rameses.waterworks.printer;

import bsh.EvalError;
import bsh.Interpreter;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.util.SystemPlatformFactory;

public class PrinterHandler{
    
    String data;
    
    public PrinterHandler(Account a){
        String expr = SystemPlatformFactory.getPlatform().getSystem().getReportData();
        try{
            Interpreter i = new Interpreter();
            i.set("a", a);
            i.set("userfullname",SystemPlatformFactory.getPlatform().getSystem().getFullName());
            i.set("datetime",SystemPlatformFactory.getPlatform().getSystem().getDate()+" "+SystemPlatformFactory.getPlatform().getSystem().getTime());
            Object o = i.eval(expr);
            data = o.toString();
        }catch(EvalError e){
            Dialog.showError("Error in Report Data: " + e.toString());
        }
    }

    public String getData() {
        return data;
    }
    
}
