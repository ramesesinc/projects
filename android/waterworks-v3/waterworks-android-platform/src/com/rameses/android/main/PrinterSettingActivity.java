package com.rameses.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.android.AppSettingsImpl;
import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.controller.BluetoothPrinterController;
import com.rameses.android.handler.PrinterHandler;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIApplication;
import com.rameses.client.android.UIDialog;

public class PrinterSettingActivity extends UserInfoMenuActivity {

	private AppSettingsImpl sets;
	private ListView lv_printer;
	private RadioButton rb_zebra, rb_oneil;
	private Button btn_set_printer;
	private TextView tv_reportdata;
	private int prevpos = 0;
	private Context contex;
	private String printerHandler = "", printerName = "";
	
	protected void onCreateProcess(Bundle savedInstanceState) {

		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("System Setting");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_printer_setting, rl_container, true);
		
		lv_printer = (ListView) findViewById(R.id.lv_printer);
		rb_zebra = (RadioButton) findViewById(R.id.rb_zebra);
		rb_oneil = (RadioButton) findViewById(R.id.rb_oneil);
		
		contex = this;
		sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();

		lv_printer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				// TODO Auto-generated method stub
//				println("name " + v.getTag(R.string.name_tag).toString());
				Object obj = v.getTag(R.string.name_tag);
				if (obj != null) {
					printerName = obj.toString(); 
				}
				println("printer name " + printerName);
				
				View v2 = lv_printer.getChildAt(prevpos);
				if (v2 != null) {
					v2.setBackgroundColor(getResources().getColor(android.R.color.background_light));
				}
				prevpos = pos;
				v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			}
		});
		
		rb_zebra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked == true) {
					printerHandler = "ZEBRA";
					rb_oneil.setChecked(false);
				}
			}
		});
		
		rb_oneil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked == true) {
					printerHandler = "ONEIL";
					rb_zebra.setChecked(false);
				}
			}
		});
		
		btn_set_printer = (Button) findViewById(R.id.btn_set_printer);
		btn_set_printer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map map = new HashMap();
				map.put("printer_name", printerName);
				map.put("printer_handler", printerHandler);
				
				UIApplication app = Platform.getApplication();
				app.getAppSettings().putAll(map);
				
				loadReportData();
			}
		});

		tv_reportdata = (TextView) findViewById(R.id.tv_reportdata);
		PrinterHandler ph = sets.getPrinterHandler(this);
		if (ph != null) {
			printerHandler = ph.getName();
			if ("ZEBRA".equals(printerHandler)) { 
				rb_oneil.setChecked(false);
				rb_zebra.setChecked(true);
			} else if ("ONEIL".equals(printerHandler)) {
				rb_zebra.setChecked(false);
				rb_oneil.setChecked(true);
			}
		}
		
		loadPrinterData();
		loadReportData();
	}
	
	void loadReportData() {
		PrinterHandler ph = sets.getPrinterHandler(this);
		if (ph != null) 
			tv_reportdata.setText(ph.getTemplate());
	}
	
	void loadPrinterData() {
		
		BluetoothPrinterController btcontroller = new BluetoothPrinterController(PrinterSettingActivity.this);
		try {
			List<String> devices = btcontroller.findDevices();
			List<Map> list = new ArrayList<Map>();
			
			println("printer name " + sets.getPrinterName());
			
	  	    String device = "";
	  	    Map item;
			for (int i = 0; i < devices.size(); i++) {
				item = new HashMap();
				
				device = devices.get(i);
				item.put("name", device);
				item.put("selected", false);
				if (device.equals(sets.getPrinterName())) {
					prevpos = i;
					item.put("selected", true);
				}
				
				list.add(item);
			}
			
			lv_printer.setAdapter(new PrinterListAdapter(contex, list));
			
//	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//	  	          android.R.layout.simple_list_item_1, android.R.id.text1, devices);
//	  			
//	  	    lv_printer.setAdapter(adapter);
//	  	    String device = "";
//			for (int i = 0; i < devices.size(); i++) {
//				device = devices.get(i);
//				println("device " + device + " idx: " + i);
//				if (device.equals(sets.getPrinterName())) {
//					View v = lv_printer.getChildAt(i);
//					println("view " + v);
//					if (v != null) 
//						v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
//					
//					break;
//				}
//			}
		} catch (Exception e) {
			UIDialog.showMessage(e, PrinterSettingActivity.this);
		}
	}
	
	void println(String str) {
		Log.i("PrinterSettingActivity", str);
	}
}
