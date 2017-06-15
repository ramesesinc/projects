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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.ExaminationDB;
import com.rameses.android.db.FaasDB;
import com.rameses.android.db.ImageDB;
import com.rameses.android.db.LandAssessLevelDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.db.LcuvSpecificClassDB;
import com.rameses.android.db.LcuvStrippingDB;
import com.rameses.android.db.LcuvSubClassDB;
import com.rameses.android.efaas.adapter.AppraisalMenuAdapter;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.LandAppraisalInfo;

public class FaasLandAppraisalActivity extends ControlActivity{
	
	private static Button appraisal_btn;
	private static ListView appraisal_list;
	private static TextView totalarea;
	
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
		setContentView(R.layout.activity_faas_land_appraisal);
		
		faasid = getIntent().getExtras().getString("faasid");
		
		try{
			FaasDB db = new FaasDB();
			faas = db.find(faasid);
			
			if(faas != null) rpuid = faas.getProperty("rpuid");
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		appraisal_list = (ListView) findViewById(R.id.appraisal_list);
		registerForContextMenu(appraisal_list);
		
		appraisal_btn = (Button) findViewById(R.id.appraisal_button);
		appraisal_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LandAppraisalInfo appraisal = new LandAppraisalInfo(activity,null,rpuid);
                appraisal.show();
            }
        });
		
		totalarea = (TextView) findViewById(R.id.appraisal_totalarea);
		
		loadAppraisalData();
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
				  loadAppraisalData();
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
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  return true;
	}
	
	public static void loadAppraisalData(){
		data_appraisal = new ArrayList<AppraisalListItem>();
		try{
			Map params = new HashMap();
			params.put("landrpuid", rpuid);
			LandDetailDB db = new LandDetailDB();
			List<Map> list = db.getList(params);
			for(Map m : list){
				String objid = m.get("objid") != null ? m.get("objid").toString() : "";
				String landrpuid = m.get("landrpuid") != null ? m.get("landrpuid").toString() : "";
				String subclass_objid = m.get("subclass_objid") != null ? m.get("subclass_objid").toString() : "";
				String specificclass_objid = m.get("specificclass_objid") != null ? m.get("specificclass_objid").toString() : "";
				String actualuse_objid = m.get("actualuse_objid") != null ? m.get("actualuse_objid").toString() : "";
				String stripping_objid = m.get("stripping_objid") != null ? m.get("stripping_objid").toString() : "";
				String striprate = m.get("striprate") != null ? m.get("striprate").toString() : "";
				String areatype = m.get("areatype") != null ? m.get("areatype").toString() : "";
				String area = m.get("area") != null ? m.get("area").toString() : "";
				String areasqm = m.get("areasqm") != null ? m.get("areasqm").toString() : "";
				String areaha = m.get("areaha") != null ? m.get("areaha").toString() : "";
				
				Properties data_subclass = new LcuvSubClassDB().find(subclass_objid);
				Properties data_specificclass = new LcuvSpecificClassDB().find(specificclass_objid);
				Properties data_actualuse = new LandAssessLevelDB().find(actualuse_objid);
				Properties data_strip = new LcuvStrippingDB().find(stripping_objid);
				
				String stripping = " - ", subclass = " - ", specificclass = " - ", actualuse = " - ";
				if(data_subclass != null) subclass = data_subclass.getProperty("code") + " - " + data_subclass.getProperty("name");
				if(data_specificclass != null) specificclass = data_specificclass.getProperty("name");
				if(data_actualuse != null) actualuse = data_actualuse.getProperty("name");
				if(data_strip != null) stripping = data_strip.getProperty("classification_objid") + " - " + data_strip.getProperty("rate");
				
				data_appraisal.add(new AppraisalListItem(
						objid,
						subclass,
						specificclass,
						actualuse,
						stripping,
						areasqm
				));
			}
			appraisal_list.setAdapter(new AppraisalMenuAdapter(activity, data_appraisal));
			LayoutParams layout = (LayoutParams) appraisal_list.getLayoutParams();
			layout.height = (68 * data_appraisal.size());
			appraisal_list.setLayoutParams(layout);
			
			appraisal_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					AppraisalMenuAdapter a = (AppraisalMenuAdapter) adapter.getAdapter();
					AppraisalListItem item = a.getListItem(pos);
					LandAppraisalInfo appraisal = new LandAppraisalInfo(activity,item.getObjid(),rpuid);
		            appraisal.show();
				}	
			});
			
			db = new LandDetailDB();
			totalarea.setText("TOTAL AREA (SQM) : " + String.valueOf(db.getTotalAreaSqm(rpuid)));
		}catch(Throwable t){
			t.printStackTrace();
			new ErrorDialog(activity, t).show();
		}
	}

}
