package com.rameses.android.efaas.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class InfoDialog {
	
	private AlertDialog alertDialog;
	
	public InfoDialog(Context ctx, String message){
		alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setCancelable(false);
		alertDialog.setTitle("Information");
		alertDialog.setMessage(message);
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
