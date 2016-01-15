package com.rameses.waterworks.bluetooth;

import java.util.List;

public interface BluetoothPort {
    
    List<String> findDevices();
    
    void setPrinter(String name);
    
    String getPrinter();
    
    void print(String message);
    
    String getError();
    
    void openBT();
    
    void closeBT();
    
}
