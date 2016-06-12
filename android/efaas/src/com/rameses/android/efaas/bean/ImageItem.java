package com.rameses.android.efaas.bean;

import com.rameses.android.efaas.util.DbBitmapUtility;

import android.graphics.Bitmap;

public class ImageItem {
	
	private String faasid, title;
	private byte[] image;
	
	public ImageItem(String faasid, String title, byte[] image){
		this.faasid = faasid;
		this.title = title;
		this.image = image;
	}
	
	public String getFaasId(){ return faasid; }
	public String getTitle(){ return title; }
	public Bitmap getImage(){ return DbBitmapUtility.getImage(image); }

}
