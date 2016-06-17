package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.*;
import com.rameses.android.efaas.dialog.AppraisalInfo;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.adapter.AppraisalMenuAdapter;
import com.rameses.android.efaas.adapter.ImageItemAdapter;
import com.rameses.android.efaas.bean.*;
import com.rameses.client.android.Platform;

public class FaasActivity extends ControlActivity{
	
	public boolean isCloseable() { return false; }
	
	private static TextView tdno, pin, owner, address, year, classification, area;
	private static TextView totalmv, totalav, cadastrallotno, blockno, surveyno;
	private static TextView street, purok, north, south, west, east;
	private static Button appraisal_btn, examination_btn, images_btn;
	private static EditText findings, recommendations;
	private static ListView appraisal_list, image_list;
	
	private static Activity activity;
	private static Properties faas = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static int ctxMenuId;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		
		setContentView(R.layout.activity_faas);
		
		faasid = getIntent().getExtras().getString("faasid");
		
		tdno = (TextView) findViewById(R.id.faas_tdno);
		pin = (TextView) findViewById(R.id.faas_pin);
		owner = (TextView) findViewById(R.id.faas_owner);
		address = (TextView) findViewById(R.id.faas_address);
		year = (TextView) findViewById(R.id.faas_ry);
		classification = (TextView) findViewById(R.id.faas_classification);
		area = (TextView) findViewById(R.id.faas_area);
		totalmv = (TextView) findViewById(R.id.faas_totalmv);
		totalav = (TextView) findViewById(R.id.faas_totalav);
		cadastrallotno = (TextView) findViewById(R.id.faas_cadastrallotno);
		blockno = (TextView) findViewById(R.id.faas_blockno);
		surveyno = (TextView) findViewById(R.id.faas_surveyno);
		street = (TextView) findViewById(R.id.faas_street);
		purok = (TextView) findViewById(R.id.faas_purok);
		north = (TextView) findViewById(R.id.faas_north);
		south = (TextView) findViewById(R.id.faas_south);
		west = (TextView) findViewById(R.id.faas_west);
		east = (TextView) findViewById(R.id.faas_east);
		
		appraisal_list = (ListView) findViewById(R.id.appraisal_list);
		image_list = (ListView) findViewById(R.id.images_list);
		
		registerForContextMenu(appraisal_list);
		registerForContextMenu(image_list);
		
		findings = (EditText) findViewById(R.id.examination_findings);
		findings.setEnabled(false);
		
		recommendations = (EditText) findViewById(R.id.examination_recommendations);
		recommendations.setEnabled(false);
		
		appraisal_btn = (Button) findViewById(R.id.appraisal_button);
		appraisal_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(rpuid == null){
            		new InfoDialog(activity,rpuid);
            		return;
            	}
                AppraisalInfo appraisal = new AppraisalInfo(activity,null,rpuid);
                appraisal.show();
            }
        });
		
		examination_btn = (Button) findViewById(R.id.examination_button);
		examination_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String text = examination_btn.getText().toString();
            	if(text.equals("Edit")){
            		findings.setEnabled(true);
            		recommendations.setEnabled(true);
            		examination_btn.setText("Save");
            	}
            	if(text.equals("Save")){
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
            			params.put("faasid", faasid);
            			params.put("findings", data_findings);
            			params.put("recommendations", data_recommendations);
            			params.put("date", data_date);
            			
            			ExaminationDB db = new ExaminationDB();
            			if(examination != null){
            				params.put("objid", examination.getProperty("objid"));
            				db.update(params);
            			}else{
            				params.put("objid", UUID.randomUUID().toString());
            				db.create(params);
            			}
            		}catch(Throwable t){
            			new ErrorDialog(activity, t).show();
            			return;
            		}
            		findings.setEnabled(false);
            		recommendations.setEnabled(false);
            		examination_btn.setText("Edit");
            		loadExaminationData();
            	}
            }
        });
		
		images_btn = (Button) findViewById(R.id.images_button);
		images_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(activity, ImageCaptureActivity.class);
				intent.putExtra("faasid", faasid);
				startActivity(intent); 
            }
        });
		
		initData();
		
		ActionBar bar = getActionBar();
	    //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a6e20d")));
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
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
		if(v.getId()==R.id.appraisal_list){
			ctxMenuId = R.id.appraisal_list;
			menu.setHeaderTitle("");
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
			  myIntent.putExtra("faasid", imageItem.getFaasId());
			  startActivity(myIntent);
		  }
		  if(menuItemIndex == 1){
			  Map params = new HashMap();
			  params.put("objid", imageItem.getObjid());
			  try{
				  ImageDB db = new ImageDB();
				  db.delete(params);
				  initData();
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  
	  if(ctxMenuId == R.id.appraisal_list){
		  AppraisalListItem appraisalItem = data_appraisal.get(info.position);
		  if(menuItemIndex == 0){
			  AppraisalInfo appraisal = new AppraisalInfo(activity,appraisalItem.getObjid(),rpuid);
              appraisal.show();
		  }
		  if(menuItemIndex == 1){
			  Map params = new HashMap();
			  params.put("objid", appraisalItem.getObjid());
			  
			  LandDetailDB db = new LandDetailDB();
			  try{
				  db.delete(params);
				  initData();
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  return true;
	}
	
	public static void initData(){
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
			area.setText(faas.getProperty("rpu_totalareasqm"));
			totalmv.setText(faas.getProperty("rpu_totalmv"));
			totalav.setText(faas.getProperty("rpu_totalav"));
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
		
		loadAppraisalData();
		loadExaminationData();
		loadImageData();
	}
	
	private static void loadAppraisalData(){
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
				
				Properties subclass = new LcuvSubClassDB().find(subclass_objid);
				Properties specificclass = new LcuvSpecificClassDB().find(specificclass_objid);
				Properties actualuse = new LandAssessLevelDB().find(actualuse_objid);
				Properties strip = new LcuvStrippingDB().find(stripping_objid);
				
				data_appraisal.add(new AppraisalListItem(
						objid,
						subclass.getProperty("code") + " - " + subclass.getProperty("name"),
						specificclass.getProperty("name"),
						actualuse.getProperty("name"),
						strip.getProperty("classification_objid") + " - " + strip.getProperty("rate"),
						areasqm
				));
			}
			appraisal_list.setAdapter(new AppraisalMenuAdapter(activity, data_appraisal));
			LayoutParams layout = (LayoutParams) appraisal_list.getLayoutParams();
			layout.height = (68 * data_appraisal.size());
			appraisal_list.setLayoutParams(layout);
		}catch(Throwable t){
			t.printStackTrace();
			new ErrorDialog(activity, t).show();
		}
	}
	
	private static void loadExaminationData(){
		try{
			Map params = new HashMap();
			params.put("faasid", faasid);
			
			ExaminationDB db = new ExaminationDB();
			examination = db.find(params);
			if(examination != null){
				findings.setText(examination.getProperty("findings"));
				recommendations.setText(examination.getProperty("recommendations"));
			}
		}catch(Throwable t){
			new ErrorDialog(activity, t).show();
		}
	}
	
	private static void loadImageData(){
		try{
			data_image = new ArrayList<ImageItem>();
			
			Map params = new HashMap();
			params.put("faasid", faasid);
			
			ImageDB db = new ImageDB();
			List<Map> list = db.getList(params);
			
			for(Map m : list){
				String objid = m.get("objid") != null ? m.get("objid").toString() : "";
				String title = m.get("title") != null ? m.get("title").toString() : "";
				String image = m.get("image") != null ? m.get("image").toString() : "";
				
				byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
				
				data_image.add(new ImageItem(objid, faasid, title, decodedBytes));
			}
			
			image_list.setAdapter(new ImageItemAdapter(activity,data_image));
			
			LayoutParams layout = (LayoutParams) image_list.getLayoutParams();
			layout.height = (600 * data_image.size());
			image_list.setLayoutParams(layout);
		}catch(Throwable t){
			new ErrorDialog(activity, t).show();
		}
	}
	
}
