package com.rameses.android.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import com.rameses.client.android.Platform;
import com.rameses.client.android.UIActionBarActivity;


public class BluetoothPrinterController {

	private BluetoothSocket socket;
	private OutputStream os;
	private InputStream is;

    private byte[] readBuffer;
    private int readBufferPosition;
    
    private UIActionBarActivity activity;
    
    private String printername;
    private boolean isPrinting = false;
    
    public BluetoothPrinterController(UIActionBarActivity activity) { 
    	this.activity = activity;
    }
    
    public List<String> findDevices() throws Exception {
    	List<String> list = new ArrayList<String>();
    	
    	Set<BluetoothDevice> devices = getDevices();
    	for (BluetoothDevice bd : devices) {
    		list.add(bd.getName());
    	}
    	
    	return list;
    }
    
    private Set<BluetoothDevice> getDevices() throws Exception {
    	try {
    		BluetoothAdapter btadapter = BluetoothAdapter.getDefaultAdapter();
    		if (btadapter == null) {
    			throw new RuntimeException("No Bluetooth Adapter available!");
    		}
    		
    		if (!btadapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBluetooth, 0);
    		}
    		
    		return btadapter.getBondedDevices();
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    public void setPrinter(String printername) {
    	this.printername = printername;
    }
   
    public String getPrinter() {
    	return this.printername;
    }
    
    public Boolean getIsPrinting() {
    	return this.isPrinting;
    }
    
	public void print(String message) throws Exception {
		try {
			println("printing data");
			isPrinting = true;
			os.write(message.getBytes());
		} catch (Exception e) {
			throw e;
		}
//		Platform.runAsync(new PrintDataProcess());
	}
	
	public void open() throws Exception {
		try {
			BluetoothDevice device = null;
			
			Set<BluetoothDevice> devices = getDevices();
			for (BluetoothDevice bd : devices) {
				if (bd.getName().equals(printername)) {
					device = bd;
				}
			}

			if (device != null) {
	            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	            socket = device.createRfcommSocketToServiceRecord(uuid);
	            socket.connect();
	            os = socket.getOutputStream();
	            is = socket.getInputStream();
	            Platform.runAsync(new PrintDataProcess());
			} else {
				throw new RuntimeException("Bluetoot Device not found!");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void close() throws Exception {
		try {
			if (os != null) os.close();
			if (is != null) is.close();
			if (socket != null) socket.close();
		} catch (Exception e) {
			throw e;
		}
	}
	
	void println(String msg) {
		Log.i("BluetoothPrinterController", msg);
	}
	
	private class PrintDataProcess implements Runnable {
		
		private byte delimiter = 10;
		
		public PrintDataProcess() {
			readBufferPosition = 0;
			readBuffer = new byte[1024];
		}
		
		public void run() {
			try {
				int available = is.available();
				if (available > 0) {
					byte[] packet = new byte[available];
					is.read(packet);
					println("before printing");
					for (int i = 0; i < available; i++) {
						byte b = packet[i];
                        if (b == delimiter) {
                            byte[] encodedBytes = new byte[readBufferPosition];
                            System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            readBufferPosition = 0;
                        } else {
                            readBuffer[readBufferPosition++] = b;
                        }
					}
					isPrinting = false;
					println("after printing");
				}
			} catch (Exception e) {
				
			}
		}
	}
}
