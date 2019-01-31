package com.rameses.android.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import bsh.EvalError;
import bsh.Interpreter;

import com.rameses.android.R;
import com.rameses.android.bean.Account;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.interfaces.UserProfile;

public class ZebraPrinterHandler implements PrinterHandler {

	private StringBuilder templateWithData = new StringBuilder();
	private StringBuilder template = new StringBuilder();
	
	public ZebraPrinterHandler(Context ctx) {
		templateWithData = new StringBuilder();
		template = new StringBuilder();
		InputStream in = null;
		try {
			in = ctx.getResources().openRawResource(R.raw.zebra);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				templateWithData.append(line);
				template.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	
	
	public String getName() {
		return "ZEBRA";
	}

	public String getTemplate() {
		return template.toString();
	}

	public String getData(Map params) {
		String data = "";
		
        try {
            Interpreter i = new Interpreter();
    		Account a = (Account) params.get("account");			
			i.set("a", a);
			
			Object addrObj = SessionContext.get("report_org_address");
			String addrStr = (addrObj != null? addrObj.toString() : "");
			i.set("address", addrStr);
			
			Object contactnoObj = SessionContext.get("report_org_contactno");
			String contactnoStr = (contactnoObj != null? contactnoObj.toString() : "");
			i.set("contactno", contactnoStr);
			
            UserProfile prof = SessionContext.getProfile();
            String name = (prof != null? prof.getFullName() : "");
            i.set("userfullname", name);
            i.set("datetime", Platform.getApplication().getServerDate());
            i.set("data", "INVALID DATA");
            i.set("items", a.getItemList());
            i.set("y", 0);
            i.set("textgap",40);
            i.eval(templateWithData.toString());
            data = i.get("data").toString();
		} catch (EvalError e) {
			e.printStackTrace();
		}

        return data;
	}
	
	void println(String msg) {
		Log.i("ZebraPrinterHandler", msg);
	}
}
