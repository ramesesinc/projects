package com.rameses.android.efaas.bean;

import com.rameses.android.efaas.util.DbBitmapUtility;

import android.graphics.Bitmap;

public class ImageItem {
	
	private String objid, faasid, title;
	private byte[] image;
	
	public ImageItem(String objid, String faasid, String title, byte[] image){
		this.objid = objid;
		this.faasid = faasid;
		this.title = title;
		this.image = image;
	}
	
	public String getObjid(){ return objid; }
	public String getFaasId(){ return faasid; }
	public String getTitle(){ return title; }
	public Bitmap getImage(){ return DbBitmapUtility.getImage(image); }

}
