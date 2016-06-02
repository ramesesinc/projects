package com.rameses.android.efaas.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ErrorDialog {
	
	private AlertDialog alertDialog;
	
	public ErrorDialog(Activity activity, Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage(sw.toString());
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	}
	
	public void show(){
		alertDialog.show();
	}

}
