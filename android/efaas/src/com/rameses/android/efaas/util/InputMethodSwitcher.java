package com.rameses.android.efaas.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.rameses.android.efaas.dialog.*;

public class InputMethodSwitcher {
	
	private Activity activity;
	
	public InputMethodSwitcher(Activity a){
		this.activity = a;
	}
	
	public void switchKeyboard(){
		try{
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			final IBinder token = activity.getWindow().getAttributes().token;
		    imm.switchToLastInputMethod(token);
//			List<InputMethodInfo> inputMethods = imm.getEnabledInputMethodList();
//			for(InputMethodInfo method : inputMethods){
//				String name = method.loadLabel(activity.getPackageManager()).toString();
//				new InfoDialog(activity,name).show();
//				//imm.setInputMethod(token, id);
//			}
		}catch(Throwable t){
			new ErrorDialog(activity,t).show();
		}
	}

}
