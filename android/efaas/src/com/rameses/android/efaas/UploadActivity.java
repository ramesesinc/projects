package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.db.FaasDB;
import com.rameses.android.efaas.adapter.UploadMenuAdapter;
import com.rameses.android.efaas.bean.UploadItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.service.UploadService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIDialog;

public class UploadActivity extends SettingsMenuActivity {
	
	private ProgressDialog progressDialog;
	private ListView list;
	private Button upload;
	List<UploadItem> data;
	public static Activity activity;
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_uploadlist);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
		
		list = (ListView) findViewById(R.id.upload_list);
		
		upload = (Button) findViewById(R.id.upload_btn);
		upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	UploadMenuAdapter adapter = (UploadMenuAdapter) list.getAdapter();
            	List<UploadItem> list = adapter.getItemList();
            	List<UploadItem> listForUpload = new ArrayList<UploadItem>();
            	for(UploadItem item : list){
            		System.err.println(item.toString() + " : " + item.isChecked());
            		if(item.isChecked() == true){
            			listForUpload.add(item);
            		}
            	}
            	if(listForUpload.isEmpty()){
            		new InfoDialog(activity,"Select items to upload!").show();
            		return;
            	}
            	uploadData(listForUpload);
            }
        });
		
		loadData("");
		
		ApplicationUtil.changeTitle(this, "Upload");
		activity = this;
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	private void loadData(String searchtext){
		data = new ArrayList<UploadItem>();
		try{
			List<Map> listData = new FaasDB().getList(new HashMap());
			Iterator<Map> i = listData.iterator();
			while(i.hasNext()){
				Map m = i.next();
				String faasid = m.get("objid").toString();
				String pin = m.get("fullpin").toString();
				String name = m.get("owner_name").toString();
				String tdno = m.get("tdno").toString();
				data.add(new UploadItem(faasid, pin, tdno, name, false));
			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationUtil.showShortMsg(e.toString());
		}
		list.setAdapter(new UploadMenuAdapter(this,data));
		list.setBackgroundResource(0);
		if(data.isEmpty()) list.setBackgroundResource(R.drawable.empty);
	}
	
	private void uploadData(final List<UploadItem> list){
		UIDialog dialog = new UIDialog(activity) {
			
			public void onApprove() {
				progressDialog.setMessage("Uploading FAAS... Please wait...");
				if (!progressDialog.isShowing()) progressDialog.show();
				
				Platform.runAsync(new ActionProcess(list));
			}
		};
		dialog.confirm("Do you want to upload this record?");
	}
	
	private Handler errorhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {	
			try { 
				Bundle data = msg.getData();			
				Throwable exception = (Throwable) data.getSerializable("response");
				new ErrorDialog(activity, exception).show();
			} finally { 
				if (progressDialog.isShowing()) progressDialog.dismiss(); 
			}
		}
	}; 
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss(); 
			UIDialog dialog = new UIDialog(activity) {
				public void onApprove() {
					loadData("");
				}
			};
			dialog.alert("The data has been uploaded!");
		}
	};
	
	private class ActionProcess implements Runnable {
		
		private List<UploadItem> list;
		
		ActionProcess(List<UploadItem> list){
			this.list = list;
		}

		@Override
		public void run() {
			for(UploadItem item : list){
				String faasid = item.getObjid();
				try
				{
					List<String> ids = new FaasDB().getFaasImageIds(faasid);
					System.err.println("IMAGE IDS : " + ids.toString());
					for(String imageid : ids){
						UploadActivity.this.runOnUiThread(new Runnable(){
							@Override
							public void run() {
								progressDialog.setMessage("Uploading Image... Please wait...");
							}
						});
						new UploadService().uploadImage(imageid, faasid);
					}
					
					UploadActivity.this.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							progressDialog.setMessage("Uploading FAAS... Please wait...");
						}
					});
					
		            UploadService svc = new UploadService();
		            Map result = svc.upload(item.getObjid());
		            if(result != null){
		            	Bundle data = new Bundle();
		    			data.putString("response", "success");
		    			
		    			Message msg = successhandler.obtainMessage();
		    			msg.setData(data);
		    			successhandler.sendMessage(msg);
		    			
		    			FaasDB faasDB = new FaasDB();
		    			faasDB.deleteFaas(item.getObjid());
		            }
				}catch(Throwable t){
					t.printStackTrace();
					
					Bundle data = new Bundle();
					data.putSerializable("response", t);
					
					Message msg = errorhandler.obtainMessage();
					msg.setData(data);
					
					errorhandler.sendMessage(msg);
				}
			}
		}
	}

}
