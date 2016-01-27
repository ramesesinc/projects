package com.rameses.waterworks.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import com.rameses.Main;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javafxports.android.FXActivity;

public class AndroidSystem implements System{

    @Override
    public String getMacAddress() {
        String macaddress = "";
        try{
            WifiManager manager = (WifiManager) FXActivity.getInstance().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            macaddress = info.getMacAddress();
        }catch(Exception e){
            Main.LOG.error("Get MacAddress", e.toString());
        }
        return macaddress;
    }

    @Override
    public String getUserID() {
        return Main.MYACCOUNT.get("USERID") != null ? Main.MYACCOUNT.get("USERID").toString() : "";
    }

    @Override
    public String getUserName() {
        return Main.MYACCOUNT.get("username") != null ? Main.MYACCOUNT.get("username").toString() : "";
    }

    @Override
    public String getFullName() {
        Map env = (Map) Main.MYACCOUNT.get("env");
        return env.get("FULLNAME") != null ? env.get("FULLNAME").toString() : "";
    }
    
    @Override
    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return  dateFormat.format(date);
    }
    
    @Override
    public String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date date = new Date();
        return  dateFormat.format(date);
    }
    
    @Override
    public String getExternalStorageDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
