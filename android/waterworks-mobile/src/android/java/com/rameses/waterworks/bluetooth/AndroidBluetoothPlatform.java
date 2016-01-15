package com.rameses.waterworks.bluetooth;

import javafxports.android.FXActivity;

public class AndroidBluetoothPlatform extends BluetoothPlatform{
    
    private BluetoothPort printer;

    @Override
    public BluetoothPort getBluetoothPrinter() {
        printer = new AndroidBluetooth();
        return printer;
    }
    
    public String getRootDirectory(){
        return FXActivity.getInstance().getFilesDir().getPath();
    }
    
}
