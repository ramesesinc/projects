package com.rameses.android.efaas.dialog;

import java.util.UUID;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.FloorItem;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class FloorInfo extends AlertDialog.Builder{
	
	public boolean CANCELLED = false;
	private Context ctx;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Throwable error = null;
	private String objid, bldguseid, bldgrpuid;
	private EditText floorno, floorarea;
	
	public FloorInfo(Context ctx, String objid, String bldguseid, String bldgrpuid){
		super(ctx);
		this.objid = objid;
		this.bldguseid = bldguseid;
		this.bldgrpuid = bldgrpuid;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal_bldg_floor, null);
		
		floorno = (EditText) view.findViewById(R.id.floor_floorno);
		floorarea = (EditText) view.findViewById(R.id.floor_floorarea);
		
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
