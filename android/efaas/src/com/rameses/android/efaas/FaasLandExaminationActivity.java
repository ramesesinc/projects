package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.ExaminationDB;
import com.rameses.android.db.ImageDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.efaas.adapter.ExaminationMenuAdapter;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.LandAppraisalInfo;

public class FaasLandExaminationActivity extends ControlActivity {
	
	private static Button examination_add;
	private static ListView examination_list;
	
	public static Activity activity;
	private static Properties faas = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static List<ExaminationListItem> data_examination;
	private static int ctxMenuId;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this;
		setContentView(R.layout.activity_faas_land_examination);
		
		faasid = getIntent().getExtras().getString("faasid");
	    
	    examination_list = (ListView) findViewById(R.id.examination_list);
		registerForContextMenu(examination_list);
		
		examination_add = (Button) findViewById(R.id.examination_add);
		examination_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, ExaminationActivity.class);
  			  	myIntent.putExtra("faasid", faasid);
  			  	myIntent.putExtra("type", "land");
  			  	startActivity(myIntent);
            }
        });
		loadExaminationData();
	}
	
	public static void loadExaminationData(){
		try{
			data_examination = new ArrayList<ExaminationListItem>();
			Map params = new HashMap();
			params.put("parent_objid", faasid);
			
			ExaminationDB db = new ExaminationDB();
			List<Map> list = db.getList(params);
			for(Map m : list){
				String id = m.get("objid") != null ? m.get("objid").toString() : "";
				String date = m.get("dtinspected") != null ? m.get("dtinspected").toString() : "";
				String findings = m.get("findings") != null ? m.get("findings").toString() : "";
				data_examination.add(new ExaminationListItem(id,findings,date));
			}
			
			examination_list.setAdapter(new ExaminationMenuAdapter(activity,data_examination));
			examination_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					ExaminationMenuAdapter a = (ExaminationMenuAdapter) adapter.getAdapter();
					ExaminationListItem item = a.getListItem(pos);
					Intent myIntent = new Intent(activity, ExaminationActivity.class);
					myIntent.putExtra("objid", item.getObjid());
	  			  	myIntent.putExtra("faasid", faasid);
	  			  	myIntent.putExtra("type", "land");
	  			  	activity.startActivity(myIntent);
				}	
			});
			LayoutParams layout = (LayoutParams) examination_list.getLayoutParams();
			layout.height = (100 * data_examination.size());
			
		}catch(Throwable t){
			t.printStackTrace();
			new ErrorDialog(activity, t).show();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		String[] menuItems = new String[]{"Edit","Delete"};
		for (int i = 0; i<menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
		if(v.getId()==R.id.appraisal_list){
			ctxMenuId = R.id.appraisal_list;
			menu.setHeaderTitle("Land Detail");
		}
		if(v.getId()==R.id.examination_list){
			ctxMenuId = R.id.examination_list;
			menu.setHeaderTitle("Examination");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  
	  if(ctxMenuId == R.id.appraisal_list){
		  AppraisalListItem appraisalItem = data_appraisal.get(info.position);
		  if(menuItemIndex == 0){
			  LandAppraisalInfo appraisal = new LandAppraisalInfo(activity,appraisalItem.getObjid(),rpuid);
              appraisal.show();
		  }
		  if(menuItemIndex == 1){
			  Map params = new HashMap();
			  params.put("objid", appraisalItem.getObjid());
			  
			  LandDetailDB db = new LandDetailDB();
			  try{
				  db.delete(params);
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  
	  if(ctxMenuId == R.id.examination_list){
		  ExaminationListItem examinationItem = data_examination.get(info.position);
		  if(menuItemIndex == 0){
			   Intent myIntent = new Intent(activity, ExaminationActivity.class);
			   myIntent.putExtra("objid", examinationItem.getObjid());
			   myIntent.putExtra("faasid", faasid);
			   activity.startActivity(myIntent);
		  }
		  if(menuItemIndex == 1){
			  Map params1 = new HashMap();
			  params1.put("examinationid", examinationItem.getObjid());
			  
			  Map params2 = new HashMap();
			  params2.put("objid", examinationItem.getObjid());
			  
			  ExaminationDB db = new ExaminationDB();
			  ImageDB imdb = new ImageDB();
			  try{
				  imdb.delete(params1);
				  db.delete(params2);
				  loadExaminationData();
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  return true;
	}

}
