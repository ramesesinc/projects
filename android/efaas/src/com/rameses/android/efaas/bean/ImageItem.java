package com.rameses.android.efaas.bean;

import com.rameses.android.efaas.util.DbBitmapUtility;

import android.graphics.Bitmap;

public class ImageItem {
	
	private String objid, examinationid, title;
	private byte[] image;
	
	public ImageItem(String objid, String examinationid, String title, byte[] image){
		this.objid = objid;
		this.examinationid = examinationid;
		this.title = title;
		this.image = image;
	}
	
	public String getObjid(){ return objid; }
	public String getExaminationId(){ return examinationid; }
	public String getTitle(){ return title; }
	public Bitmap getImage(){ return DbBitmapUtility.getImage(image); }

}
