package com.rameses.android.efaas.dialog;

import com.rameses.android.R;
import com.rameses.android.db.BldgAdditionalItemDB;
import com.rameses.android.db.ParameterDB;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.adapter.ParameterMenuAdapter;
import com.rameses.android.efaas.bean.AdjustmentItem;
import com.rameses.android.efaas.bean.AdjustmentListItem;
import com.rameses.android.efaas.bean.DefaultItem;
import com.rameses.android.efaas.bean.ParameterItem;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import java.util.*;

public class AdjustmentTypeInfo extends AlertDialog.Builder {
	
	public boolean CANCELLED = false;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private static Context ctx;
	private String objid;
	private boolean done = false;
	private Throwable error = null;
	private Spinner adjustment;
	private TextView unit;
	private static ListView listview;
	private List<Map> data_adjustment;
	private Map data;
	public static List<ParameterItem> data_parameter;
	private AdjustmentListItem listitem;
	private int position;
	
	public AdjustmentTypeInfo(Context ctx, AdjustmentListItem listitem, int position){
		super(ctx);
		this.ctx = ctx;
		this.listitem = listitem;
		this.position = position;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal_bldg_adjustment, null);
		
		adjustment = (Spinner) view.findViewById(R.id.adjustment_adjustment);
		unit = (TextView) view.findViewById(R.id.adjustment_unit);
		listview = (ListView) view.findViewById(R.id.parameter_list);
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Adjustment Type Info");
		builder.setView(view);
		builder.setNegativeButton("Cancel", null);
		if(listitem == null){
			builder.setPositiveButton("Add", null);
		}else{
			builder.setPositiveButton("Update", null);
		}
		
		dialog = builder.create();
		loadData();
	}
	
	void loadData(){
		try{
			data_adjustment = new BldgAdditionalItemDB().getList(new HashMap());
			adjustment.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_adjustment,"code","name")));
			adjustment.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
            		data = find(item, data_adjustment);
            		if(data != null){
            			String data_unit = data.get("unit") != null ? data.get("unit").toString() : "";
    	            	String data_expr = data.get("expr") != null ? data.get("expr").toString() : "";
    	            	unit.setText("");
    	            	unit.setText(data_unit);
    	            	
    	            	//add parameter to the listview
    	            	data_parameter = new ArrayList<ParameterItem>();
    	            	List<Map> parameters = null; 
    	            	try{
    	            		parameters = new ParameterDB().getList(new HashMap());
    	            	}catch(Exception e){ e.printStackTrace(); };
    	            	for(Map p : parameters){
    	            		String name = p.get("name") != null ? p.get("name").toString() : null;
    	            		double minvalue = p.get("minvalue") != null ? Double.parseDouble(p.get("minvalue").toString()) : 0.00;
    	            		if(data_expr.contains(name)){
    	            			if(!name.startsWith("SYS")) data_parameter.add(new ParameterItem(name,minvalue));
    	            		}
    	            	}
    	            	if(listitem != null){
    	            		if(data.get("objid").toString().equals(listitem.getItem().getObjid())) data_parameter = listitem.getList();
    	            	}
    	            	listview.setAdapter(new ParameterMenuAdapter(ctx,data_parameter));
            		}
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> adapter) {  }
	        });
			
			if(listitem != null){
				int index = 1;
				for(Map data : data_adjustment){
					if(data.get("objid").toString().equals(listitem.getItem().getObjid())) adjustment.setSelection(index);
					index++;
				}
			}
			
			listview.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					ParameterMenuAdapter a = (ParameterMenuAdapter) adapter.getAdapter();
					ParameterItem item = a.getListItem(pos);
					if(item != null) new ParameterInfo(ctx,item,pos).show();
				}	
			});
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	public static void refreshList(){
		listview.setAdapter(new ParameterMenuAdapter(ctx,data_parameter));
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
	
	private Map find(DefaultItem item, List<Map> data){
		Map m = null;
		for(Map map : data){
			String id = map.get("objid") != null ? map.get("objid").toString() : "";
			if(item.getObjid().equals(id)){
				return map;
			}
		}
		return m;
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(data == null){
					new InfoDialog(ctx,"Adjustment Type is required!").show();
					return;
				}
				String objid = data.get("objid") != null ? data.get("objid").toString() : null;
				String code = data.get("code") != null ? data.get("code").toString() : null;
				String name = data.get("name") != null ? data.get("name").toString() : null;
				String unit = data.get("unit") != null ? data.get("unit").toString() : null;
				String expr = data.get("expr") != null ? data.get("expr").toString() : null;
				
				AdjustmentItem item = new AdjustmentItem(objid,code,name,unit,expr);
				
				if(listitem == null){
					FloorInfo.addAdjustment(new AdjustmentListItem(item, data_parameter));
				}else{
					FloorInfo.data_adjustment.set(position, new AdjustmentListItem(item, data_parameter));
					FloorInfo.loadAdjustmentData();
				}
				done = true;
				if(done){
					dialog.dismiss();
				}
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
