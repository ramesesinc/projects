package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.rameses.android.efaas.adapter.ExaminationMenuAdapter;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.dialog.LandAppraisalInfo;

public class FaasLandActivity extends ControlActivity {
	
	private static TextView tdno, pin, owner, address, year, classification;
	private static TextView cadastrallotno, blockno, surveyno, totalarea;
	private static TextView street, purok, north, south, west, east;
	private static Button appraisal_btn, examination_add;
	private static ListView appraisal_list, examination_list;
	
	public static Activity activity;
	private static Properties faas = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static List<ExaminationListItem> data_examination;
	private static int ctxMenuId;
	
	private ListView menu;
	private ScrollView contentView;
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		setContentView(R.layout.activity_faas);
		faasid = getIntent().getExtras().getString("faasid");
		
		contentView = (ScrollView) findViewById(R.id.faas_contentview);
		menu = (ListView) findViewById(R.id.faas_menu);
		
		final List<String> data_menu = new ArrayList<String>();
		data_menu.add("General");
		data_menu.add("Appraisal");
		data_menu.add("Examination");
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data_menu);
        menu.setAdapter(arrayAdapter); 
        menu.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos, long arg3) {
				contentView.removeAllViews();
				if(pos == 0){
					loadGeneralInfo();
				}
				if(pos == 1){
					loadAppraisalInfo();
				}
				if(pos == 2){
					loadExaminationInfo();
				}
			}	
		});
        
        loadGeneralInfo();
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	private void loadGeneralInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_land_general, contentView, false);
	    contentView.addView(view);
	    
		tdno = (TextView) view.findViewById(R.id.faas_tdno);
		pin = (TextView) view.findViewById(R.id.faas_pin);
		owner = (TextView) view.findViewById(R.id.faas_owner);
		address = (TextView) view.findViewById(R.id.faas_address);
		year = (TextView) view.findViewById(R.id.faas_ry);
		classification = (TextView) view.findViewById(R.id.faas_classification);
		cadastrallotno = (TextView) view.findViewById(R.id.faas_cadastrallotno);
		blockno = (TextView) view.findViewById(R.id.faas_blockno);
		surveyno = (TextView) view.findViewById(R.id.faas_surveyno);
		street = (TextView) view.findViewById(R.id.faas_street);
		purok = (TextView) view.findViewById(R.id.faas_purok);
		north = (TextView) view.findViewById(R.id.faas_north);
		south = (TextView) view.findViewById(R.id.faas_south);
		west = (TextView) view.findViewById(R.id.faas_west);
		east = (TextView) view.findViewById(R.id.faas_east);
		
		try{
			FaasDB db = new FaasDB();
			faas = db.find(faasid);
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		if(faas != null){
			ApplicationUtil.changeTitle(activity, faas.getProperty("owner_name"));
			
			rpuid = faas.getProperty("rpuid");
			
			tdno.setText(faas.getProperty("tdno"));
			pin.setText(faas.getProperty("fullpin"));
			owner.setText(faas.getProperty("owner_name"));
			address.setText(faas.getProperty("owner_address"));
			year.setText(faas.getProperty("rpu_ry"));
			classification.setText(faas.getProperty("rpu_classification_objid"));
			cadastrallotno.setText(faas.getProperty("rp_cadastrallotno"));
			blockno.setText(faas.getProperty("rp_blockno"));
			surveyno.setText(faas.getProperty("rp_surveyno"));
			street.setText(faas.getProperty("rp_street"));
			purok.setText(faas.getProperty("rp_purok"));
			north.setText(faas.getProperty("rp_north"));
			south.setText(faas.getProperty("rp_south"));
			west.setText(faas.getProperty("rp_west"));
			east.setText(faas.getProperty("rp_east"));
		}else{
			new InfoDialog(activity, "Record not found!").show();
		}
	}
	
	private void loadAppraisalInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_land_appraisal, contentView, false);
	    contentView.addView(view);
	    
	    totalarea = (TextView) view.findViewById(R.id.appraisal_totalarea);
	    
		appraisal_list = (ListView) view.findViewById(R.id.appraisal_list);
		registerForContextMenu(appraisal_list);
		
		appraisal_btn = (Button) view.findViewById(R.id.appraisal_button);
		appraisal_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LandAppraisalInfo appraisal = new LandAppraisalInfo(activity,null,rpuid);
                appraisal.show();
            }
        });
		
		loadAppraisalData();
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
			
			appraisal_list.setAdapter(new AppraisalMenuAdapter(activity, data_appraisal));
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
	
	private void loadExaminationInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_land_examination, contentView, false);
	    contentView.addView(view);
	    
	    examination_list = (ListView) view.findViewById(R.id.examination_list);
		registerForContextMenu(examination_list);
		
		examination_add = (Button) view.findViewById(R.id.examination_add);
		examination_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, ExaminationActivity.class);
  			  	myIntent.putExtra("faasid", faasid);
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

}
