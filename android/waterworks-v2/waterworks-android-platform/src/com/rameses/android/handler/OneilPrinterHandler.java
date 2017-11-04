package com.rameses.android.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import android.content.Context;
import bsh.EvalError;
import bsh.Interpreter;

import com.rameses.android.R;
import com.rameses.android.bean.Account;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.interfaces.UserProfile;

public class OneilPrinterHandler implements PrinterHandler {

	private StringBuilder templateWithData = new StringBuilder();
	private StringBuilder template = new StringBuilder();
	
	public OneilPrinterHandler(Context ctx) {
		templateWithData = new StringBuilder();
		template = new StringBuilder();
		InputStream in = null;
		try {
			in = ctx.getResources().openRawResource(R.raw.oneil);
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
		return "ONEIL";
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
            UserProfile prof = SessionContext.getProfile();
            String name = (prof != null? prof.getFullName() : "");
            i.set("userfullname", name);
            i.set("datetime", Platform.getApplication().getServerDate());
            i.set("data", "INVALID DATA");
            i.set("items", a.getItemList());
            i.set("y", 0);
            i.set("textgap",30);
            i.eval(templateWithData.toString());
            data = i.get("data").toString();
		} catch (EvalError e) {
			e.printStackTrace();
		}

        return data;
	}
}
