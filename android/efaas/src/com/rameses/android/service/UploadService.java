package com.rameses.android.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import android.util.Base64;
import com.rameses.android.db.ExaminationDB;
import com.rameses.android.db.FaasDB;
import com.rameses.android.db.ImageDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.efaas.UploadActivity;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.util.ChunkHandler;
import com.rameses.android.efaas.util.DataBuilder;
import com.rameses.android.efaas.util.FileChunker;
import com.rameses.client.services.AbstractService;

public class UploadService extends AbstractService {
	
	Properties prop = null;

	@Override
	public String getServiceName() {
		return "EfaasUploadService";
	}
	
	public Map upload(String faasid){
		Map faas = null;
		try{
			FaasDB faasdb = new FaasDB();
			LandDetailDB landdetaildb = new LandDetailDB();
			ExaminationDB  examdb = new ExaminationDB();
			ImageDB imagedb = new ImageDB();
			
			faas = (Map) faasdb.find(faasid);
			
			List<Map> landdetails = landdetaildb.getLandDetails(faas.get("rpu_objid").toString());
			
			Map param2 = new HashMap();
			param2.put("parent_objid", faasid);
			List<Map> examinations = new ArrayList<Map>(); 
			List<Map> list = examdb.getList(param2);
			for(Map m : list){
				Map param3 = new HashMap();
				param3.put("examinationid", m.get("objid"));
				m.put("images", imagedb.getList(param3));
				examinations.add(m);
			}
			faas = DataBuilder.buildMap(faas);
			Map rpu = (Map) faas.get("rpu");
			rpu.put("landdetails", DataBuilder.buildList(landdetails));
			faas.put("examinations", examinations);
		}catch(final Throwable t){
			UploadActivity.activity.runOnUiThread(new Runnable() {
			  public void run() {
				  new ErrorDialog(UploadActivity.activity,t).show();
			  }
			});
			return null;
		}
		if(faas == null){
			UploadActivity.activity.runOnUiThread(new Runnable() {
			   public void run() {
				   new InfoDialog(UploadActivity.activity,"Faas record cannot be null!").show();
			   }
			});
			return null;
		}
		return (Map) invoke("upload",faas);
	}
	
	public void uploadImage(String imageid, final String faasid){
		byte[] bytes = null; 
		ImageDB db = new ImageDB();
		String title = "unknown";
		
		try{
			prop = db.find(imageid);
		}catch(final Throwable t){
			UploadActivity.activity.runOnUiThread(new Runnable() {
			  public void run() {
				  new ErrorDialog(UploadActivity.activity,t).show();
			  }
			});
		}
		
		if(prop != null){
			String img = prop.getProperty("image");
			bytes = Base64.decode(img, Base64.DEFAULT);
		}
		
        FileChunker fc = new FileChunker( bytes, title + ".png", "image/png" );
        fc.setChunkSize(50);
        fc.parse(new ChunkHandler() {

            @Override
            public void start() {
                Map header = new HashMap();
                header.put("objid", UUID.randomUUID().toString());
                header.put("refid", faasid);
                header.put("title", prop.getProperty("title"));
                header.put("filesize", null);
                header.put("extension", "jpg");
                
                DBImageService svc = new DBImageService();
                svc.saveHeader(header);
            }

            @Override
            public void end() {
                
            }

            @Override
            public void handle(int i, byte[] bytes) {
                Map item = new HashMap();
                item.put("objid", UUID.randomUUID().toString());
                item.put("parentid", prop.getProperty("objid"));
                item.put("fileno", i);
                item.put("byte", bytes);
                
                DBImageService svc = new DBImageService();
                svc.saveItem(item);
            }
        });
	}

}
