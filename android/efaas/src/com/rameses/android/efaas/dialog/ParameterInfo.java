package com.rameses.android.efaas.dialog;

import com.rameses.android.R;
import com.rameses.android.db.BldgAdditionalItemDB;
import com.rameses.android.db.ParameterDB;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.adapter.ParameterMenuAdapter;
import com.rameses.android.efaas.bean.AdjustmentItem;
import com.rameses.android.efaas.bean.DefaultItem;
import com.rameses.android.efaas.bean.ParameterItem;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import java.util.*;

public class ParameterInfo extends AlertDialog.Builder {
	
	public boolean CANCELLED = false;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Context ctx;
	private String objid;
	private boolean done = false;
	private Throwable error = null;
	private EditText value;
	private ParameterItem parameter;
	private int position;
	
	public ParameterInfo(Context ctx, ParameterItem parameter, int position){
		super(ctx);
		this.ctx = ctx;
		this.parameter = parameter;
		this.position = position;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal_bldg_adjustment_parameter, null);
		
		value = (EditText) view.findViewById(R.id.parameter_value);
		value.setText(String.valueOf(parameter.getValue()));
		
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle(parameter.getParameter());
		builder.setView(view);
		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("Update", null);
		
		dialog = builder.create();
		loadData();
	}
	
	void loadData(){
		try{
			
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(value.getText().toString().isEmpty()){
					new InfoDialog(ctx,"Value is required!").show();
					return;
				}
				double val = Double.parseDouble(value.getText().toString());
				AdjustmentTypeInfo.data_parameter.set(position, new ParameterItem(parameter.getParameter(),val));
				AdjustmentTypeInfo.refreshList();
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
