package com.rameses.waterworks.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxports.android.FXActivity;

public class AndroidBluetooth implements BluetoothPort
{
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    Set<BluetoothDevice> pairedDevices;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    
    String printerName;
    String ERROR = "";

    @Override
    public List<String> findDevices() {
        List<String> list = new ArrayList<String>();
        try{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null){
                android.util.Log.e("Bluetooth Printer", "No Bluetooth Adapter available!");
            }
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                FXActivity.getInstance().startActivityForResult(enableBluetooth, 0);
            }
            pairedDevices = mBluetoothAdapter.getBondedDevices();
            for(BluetoothDevice device : pairedDevices){
                list.add(device.getName());
            }
        }catch(Exception e){
            Logger.getLogger(AndroidBluetooth.class.getName()).log(Level.SEVERE, null, e);
            ERROR = e.toString();
        }
        return list;
    }

    @Override
    public void setPrinter(String name) {
        printerName = name;
    }

    @Override
    public String getPrinter() {
        return printerName;
    }

    @Override
    public void print(String message) {
        try{
            mmOutputStream.write(message.getBytes());
        }catch(Exception e){
            Logger.getLogger(AndroidBluetooth.class.getName()).log(Level.SEVERE, null, e);
            ERROR = e.toString();
        }
    }
    
    @Override
    public String getError(){
        return ERROR;
    }
    
    void beginListenForData() {
        try {
            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            Logger.getLogger(AndroidBluetooth.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(AndroidBluetooth.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @Override
    public void openBT(){
        if(pairedDevices == null) findDevices();
        for(BluetoothDevice device : pairedDevices){
            if(device.getName().equals(printerName)){
                mmDevice = device;
            }
        }
        try{
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
        }catch(Exception e){
            Logger.getLogger(AndroidBluetooth.class.getName()).log(Level.SEVERE, null, e);
            ERROR = e.toString();
        }
    }
    
    @Override
     public void closeBT() {
        try{
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        }catch(Exception e){
            ERROR = e.toString();
            Logger.getLogger(AndroidBluetooth.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void setError(String e) {
        ERROR = e;
    }

}
