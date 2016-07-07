package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.ExaminationDB;
import com.rameses.android.db.ImageDB;
import com.rameses.android.efaas.adapter.ImageItemAdapter;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.util.InputMethodSwitcher;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;

public class ExaminationActivity  extends ControlActivity{
	
	private static List<ImageItem> data_image;
	private static String objid, faasid;
	private EditText findings, recommendations;
	private Button images_btn, examination_save;
	private static ListView image_list;
	private static Activity activity;
	private int ctxMenuId;
	private String STATE = "CREATE";
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		objid = getIntent().getExtras().getString("objid");
		faasid = getIntent().getExtras().getString("faasid");
		if(objid == null){
			objid = UUID.randomUUID().toString();
		}else{
			STATE = "UPDATE";
		}

		setContentView(R.layout.activity_examination);
		
		findings = (EditText) findViewById(R.id.examination_findings);
		recommendations = (EditText) findViewById(R.id.examination_recommendations);
		
		image_list = (ListView) findViewById(R.id.images_list);
		registerForContextMenu(image_list);
		
		images_btn = (Button) findViewById(R.id.images_button);
		images_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(activity, ImageCaptureActivity.class);
				intent.putExtra("examinationid", objid);
				activity.startActivity(intent); 
            }
        });
		
		examination_save = (Button) findViewById(R.id.examination_save);
		examination_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try{
            		String data_findings = findings.getText().toString();
    				String data_recommendations = recommendations.getText().toString();
    				String data_date = Platform.getApplication().getServerDate().toString();
    				
    				if(data_findings.isEmpty()){
    					new InfoDialog(activity,"Findings are required!").show();
    					return;
    				}
    				if(data_recommendations.isEmpty()){
    					new InfoDialog(activity,"Recommendations are required!").show();
    					return;
    				}
    				
    				Map params = new HashMap();
    				params.put("objid", objid);
    				params.put("parent_objid", faasid);
    				params.put("findings", data_findings);
    				params.put("recommendations", data_recommendations);
    				params.put("dtinspected", data_date);
    				params.put("notedby", SessionContext.getProfile().getFullName());
    				params.put("notedbytitle", SessionContext.getProfile().getJobTitle());
    				
    				ExaminationDB db = new ExaminationDB();
    				if(STATE.equals("UPDATE")){
    					db.update(params);
    				}else{
    					db.create(params);
    				}
            	}catch(Throwable t){
            		new ErrorDialog(activity,t).show();
            		return;
            	}
            	disposeMe();
            	FaasLandActivity.loadExaminationData();
            }
        });
		if(STATE.equals("CREATE")) examination_save.setText("SAVE");
		if(STATE.equals("UPDATE")){
			initComponentData();
			examination_save.setText("Update");
		}
		loadImageData();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		String[] menuItems = new String[]{"Edit","Delete"};
		for (int i = 0; i<menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
		if(v.getId()==R.id.images_list) {
			ctxMenuId = R.id.images_list;
			menu.setHeaderTitle(data_image.get(info.position).getTitle());
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		
		if(ctxMenuId == R.id.images_list){
			  ImageItem imageItem = data_image.get(info.position);
			  if(menuItemIndex == 0){
				  Intent myIntent = new Intent(this, ImageCaptureActivity.class);
				  myIntent.putExtra("objid", imageItem.getObjid());
				  myIntent.putExtra("examinationid", imageItem.getExaminationId());
				  startActivity(myIntent);
			  }
			  if(menuItemIndex == 1){
				  Map params = new HashMap();
				  params.put("objid", imageItem.getObjid());
				  try{
					  ImageDB db = new ImageDB();
					  db.delete(params);
					  loadImageData();
				  }catch(Throwable t){
					  new ErrorDialog(this, t).show();
				  }
			  }
		  }
		return true;
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	public static void loadImageData(){
		String examinationid = objid;
		try{
			data_image = new ArrayList<ImageItem>();
			
			Map params = new HashMap();
			params.put("examinationid", examinationid);
			
			ImageDB db = new ImageDB();
			List<Map> list = db.getList(params);
			
			for(Map m : list){
				String objid = m.get("objid") != null ? m.get("objid").toString() : "";
				String title = m.get("title") != null ? m.get("title").toString() : "";
				String image = m.get("image") != null ? m.get("image").toString() : "";
				
				byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
				
				data_image.add(new ImageItem(objid, examinationid, title, decodedBytes));
			}
			
			image_list.setAdapter(new ImageItemAdapter(activity,data_image));
			
			LayoutParams layout = (LayoutParams) image_list.getLayoutParams();
			layout.height = (600 * data_image.size());
			image_list.setLayoutParams(layout);
			
			image_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					ImageItemAdapter a = (ImageItemAdapter) adapter.getAdapter();
					ImageItem item = a.getListItem(pos);
					Intent myIntent = new Intent(activity, ImageCaptureActivity.class);
					myIntent.putExtra("objid", item.getObjid());
					myIntent.putExtra("examinationid", item.getExaminationId());
					activity.startActivity(myIntent);
				}	
			});
		}catch(Throwable t){
			new ErrorDialog(activity, t).show();
		}
	}
	
	private void initComponentData(){
		try{
			ExaminationDB db = new ExaminationDB();
			Properties prop = db.find(objid);
			findings.setText(prop.getProperty("findings"));
			recommendations.setText(prop.getProperty("recommendations"));
		}catch(Throwable t){
			new ErrorDialog(activity, t).show();
		}
	}

}
