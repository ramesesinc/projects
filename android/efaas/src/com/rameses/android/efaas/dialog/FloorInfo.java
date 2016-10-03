package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.rameses.android.R;
import com.rameses.android.efaas.adapter.AdjustmentMenuAdapter;
import com.rameses.android.efaas.adapter.ParameterMenuAdapter;
import com.rameses.android.efaas.bean.AdjustmentItem;
import com.rameses.android.efaas.bean.AdjustmentListItem;
import com.rameses.android.efaas.bean.FloorItem;
import com.rameses.android.efaas.bean.ParameterItem;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FloorInfo extends AlertDialog.Builder{
	
	public boolean CANCELLED = false;
	private static Context ctx;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Throwable error = null;
	private String objid, bldguseid, bldgrpuid;
	private EditText floorno, floorarea;
	private Button add;
	private static ListView listview;
	private static List<AdjustmentListItem> data_adjustment;
	
	public FloorInfo(Context c, String objid, String bldguseid, String bldgrpuid){
		super(c);
		this.ctx = c;
		this.objid = objid;
		this.bldguseid = bldguseid;
		this.bldgrpuid = bldgrpuid;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal_bldg_floor, null);
		
		floorno = (EditText) view.findViewById(R.id.floor_floorno);
		floorarea = (EditText) view.findViewById(R.id.floor_floorarea);
		listview = (ListView) view.findViewById(R.id.adjustment_list);
		add = (Button) view.findViewById(R.id.adjustment_add);
		add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	new AdjustmentTypeInfo(ctx, null, -1).show();
            }
        });
		
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Floor Info");
		builder.setView(view);
		builder.setNegativeButton("Cancel", null);
		if(this.objid == null){
			builder.setPositiveButton("Save", null);
		}else{
			builder.setPositiveButton("Update", null);
		}
		
		dialog = builder.create();
		data_adjustment = new ArrayList<AdjustmentListItem>();
	}
	
	private static void loadAdjustmentData(){
		try{
			listview.setAdapter(new AdjustmentMenuAdapter(ctx, data_adjustment));
			listview.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					AdjustmentMenuAdapter a = (AdjustmentMenuAdapter) adapter.getAdapter();
					AdjustmentListItem item = a.getListItem(pos);
					if(item != null) new AdjustmentTypeInfo(ctx,item,pos).show();
				}	
			});
		}catch(Throwable t){
			new ErrorDialog(ctx, t).show();
		}
	}
	
	public static void addAdjustment(AdjustmentListItem item){
		if(item != null) data_adjustment.add(item);
		loadAdjustmentData();
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String data_floorno = floorno.getText().toString();
				String data_floorarea = floorarea.getText().toString();
				
				if(data_floorno.isEmpty()){
					new InfoDialog(ctx,"Floor No. is required!").show();
					return;
				}
				
				if(data_floorarea.isEmpty()){
					new InfoDialog(ctx,"Floor Area is required!").show();
					return;
				}
				
				FloorItem item = new FloorItem(UUID.randomUUID().toString(), data_floorno, data_floorarea);
				ActualUseInfo.data_floor.add(item);
				ActualUseInfo.loadListData();
				dialog.dismiss();
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
