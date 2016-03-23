package com.rameses.waterworks.printer;

import bsh.EvalError;
import bsh.Interpreter;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZebraPrinterHandler implements PrinterHandler{
    
    String data = "";
    String error = "";
    StringBuilder sb;
    StringBuilder code;
    
    public ZebraPrinterHandler(){
        sb = new StringBuilder();
        code = new StringBuilder();
        InputStream in = null;
        try{
            in = getClass().getResourceAsStream("/file/zebra.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
                code.append(line + "\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try { in.close(); } catch(Exception e){ System.err.println(e); }
        }
    }

    @Override
    public String getName() {
        return "ZEBRA";
    }

    @Override
    public String getData(Account a) {
        try{
            Interpreter i = new Interpreter();
            i.set("a", a);
            i.set("userfullname",SystemPlatformFactory.getPlatform().getSystem().getFullName());
            i.set("datetime",SystemPlatformFactory.getPlatform().getSystem().getDate()+" "+SystemPlatformFactory.getPlatform().getSystem().getTime());
            i.set("data", "INVALID DATA");
            i.set("items", a.getItemList());
            i.set("x", 0);
            i.set("textgap",40);
            i.eval(sb.toString());
            data = i.get("data").toString();
        }catch(EvalError e){
            error = "Error in Report Data: " + e.toString();
        }
        return data;
    }

    @Override
    public String getError() {
        return error;
    }
    
    @Override
    public String getScriptCode() {
        return code.toString();
    }
    
}
