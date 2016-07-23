package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rameses.android.R;
import com.rameses.android.db.BldgAssessLevelDB;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.adapter.AppraisalMenuAdapter;
import com.rameses.android.efaas.adapter.FloorMenuAdapter;
import com.rameses.android.efaas.bean.DefaultItem;
import com.rameses.android.efaas.bean.FloorItem;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ActualUseInfo extends AlertDialog.Builder{
	
	public boolean CANCELLED = false;
	private String objid, rpuid;
	private boolean done = false;
	private static Context ctx;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private static Throwable error = null;
	private List<Map> data_classification;
	private Spinner classification;
	private Button floor_add;
	private static ListView floor_list;
	public static List<FloorItem> data_floor;
	
	public ActualUseInfo(Context c, String objid, String rpuid){
		super(c);
		this.ctx = c;
		this.objid = objid;
		this.rpuid = rpuid;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal_bldg_actualuse, null);
		
		classification = (Spinner) view.findViewById(R.id.appraisal_classification);
		
		floor_list = (ListView) view.findViewById(R.id.floor_list);
		data_floor = new ArrayList<FloorItem>();
		
		floor_add = (Button) view.findViewById(R.id.floor_add);
		floor_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	FloorInfo info = new FloorInfo(ctx, null, null, null);
            	info.show();
            }
        });
		
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Actual Use / Floor Info");
		builder.setView(view);
		builder.setNegativeButton("Cancel", null);
		if(this.objid == null){
			builder.setPositiveButton("Save", null);
		}else{
			builder.setPositiveButton("Update", null);
		}
		
		dialog = builder.create();
		
		loadData();
	}
	
	private void loadData(){
		try{
			data_classification = new BldgAssessLevelDB().getList(new HashMap());
			classification.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_classification,"code","name")));
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	public static void loadListData(){
		try{
			floor_list.setAdapter(new FloorMenuAdapter(ctx, data_floor));
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	private List<DefaultItem> filter(List<Map> data, String...fieldname ){
		List<DefaultItem> list = new ArrayList<DefaultItem>();
		list.add(new DefaultItem("0","NO ITEM SELECTED"));
		if(data == null) return list;
		for(Map m : data){
			String name = "";
			for(String fn : fieldname){
				for(Object o : m.entrySet()){
					Map.Entry me = (Map.Entry) o;
					if(me.getKey().equals(fn)) name += me.getValue() + "   ";
				}
			}
			String objid = m.get("objid") != null ? m.get("objid").toString() : "";
			if(!name.isEmpty()) list.add(new DefaultItem(objid,name));
		}
		return list;
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}
		});
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				CANCELLED = true;
				dialog.dismiss();
			}
		});
		AlertDialog d =  this.create();
		d.setCancelable(false);
		return d;
	}

}
