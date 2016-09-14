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

import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.BldgStructureDB;
import com.rameses.android.db.ExaminationDB;
import com.rameses.android.db.FaasDB;
import com.rameses.android.db.ImageDB;
import com.rameses.android.efaas.adapter.BldgStructureMenuAdapter;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.BldgStructure;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.StructuralMaterialInfo;

public class FaasBuildingMaterialActivity extends ControlActivity{
	
	private static Button material_add;
	private static ListView material_list;
	
	public static Activity activity;
	private static Properties faas = null;
	private static Properties bldgrpu = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static List<ExaminationListItem> data_examination;
	private static List<BldgStructure> data_material;
	private static int ctxMenuId;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		setContentView(R.layout.activity_faas_bldg_materials);
		faasid = getIntent().getExtras().getString("faasid");
		
		try{
			FaasDB db = new FaasDB();
			faas = db.find(faasid);
			if(faas != null) rpuid = faas.getProperty("rpuid");
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
	    
	    material_list = (ListView) findViewById(R.id.material_list);
		activity.registerForContextMenu(material_list);
		
		material_add = (Button) findViewById(R.id.material_add);
		material_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	StructuralMaterialInfo material = new StructuralMaterialInfo(activity,null,rpuid);
            	material.show();
            }
        });
		loadMaterialData();
	}
	
	public static void loadMaterialData(){
		try{
			data_material = new ArrayList<BldgStructure>();
			Map params = new HashMap();
			params.put("bldgrpuid", rpuid);
			
			BldgStructureDB db = new BldgStructureDB();
			List<Map> list = db.getList(params);
			for(Map m : list){
				String objid = m.get("objid") != null ? m.get("objid").toString() : null;
				String bldgrpuid = m.get("bldgrpuid") != null ? m.get("bldgrpuid").toString() : null;
				String structureid = m.get("structure_objid") != null ? m.get("structure_objid").toString() : null;
				String materialid = m.get("material_objid") != null ? m.get("material_objid").toString() : null;
				String floor = m.get("floor") != null ? m.get("floor").toString() : null;
				data_material.add(new BldgStructure(objid, bldgrpuid, structureid, materialid, floor));
			}
			
			material_list.setAdapter(new BldgStructureMenuAdapter(activity,data_material));
			material_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					BldgStructureMenuAdapter a = (BldgStructureMenuAdapter) adapter.getAdapter();
					BldgStructure item = a.getListItem(pos);
					StructuralMaterialInfo info = new StructuralMaterialInfo(activity, item, rpuid);
					info.show();
				}	
			});
			LayoutParams layout = (LayoutParams) material_list.getLayoutParams();
			layout.height = (68 * data_material.size());
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
		if(v.getId()==R.id.material_list){
			ctxMenuId = R.id.material_list;
			menu.setHeaderTitle("Structural Material");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  
	  if(ctxMenuId == R.id.material_list){
		  BldgStructure structure = data_material.get(info.position);
		  if(menuItemIndex == 0){
			   StructuralMaterialInfo material = new StructuralMaterialInfo(activity,structure,rpuid);
          	   material.show();
		  }
		  if(menuItemIndex == 1){
			  Map params = new HashMap();
			  params.put("objid", structure.getObjid());
			  try{
				  BldgStructureDB db = new BldgStructureDB();
				  db.delete(params);
				  loadMaterialData();
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  
	  return true;
	}

}
