package com.rameses.android.efaas.adapter;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.UploadItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class UploadMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<UploadItem> data;
	UploadItem item;
	
	public UploadMenuAdapter(Activity activity, List<UploadItem> data){
		ctx = activity;
		this.data = data;
		inflater = ( LayoutInflater )ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(final int pos, View view, ViewGroup vgroup) {
		View rowView = inflater.inflate(R.layout.list_menu_upload, null);
		
		final CheckBox cb = (CheckBox) rowView.findViewById(R.id.list_menu_upload_checkbox);
		TextView pin = (TextView) rowView.findViewById(R.id.list_menu_upload_pin);
		TextView tdno = (TextView) rowView.findViewById(R.id.list_menu_upload_tdno);
		TextView name = (TextView) rowView.findViewById(R.id.list_menu_upload_name);
		
		item = data.get(pos);
		if(item != null){
			cb.setChecked(item.isChecked());
			pin.setText(item.getPin());
			name.setText(item.getName());
			tdno.setText(item.getTdNo());
		}
		
		cb.setOnClickListener( new View.OnClickListener() {  
            public void onClick(View v) {  
                data.get(pos).setCheck(cb.isChecked());
            }  
        }); 
		
		return rowView;
	}
	
	public UploadItem getListItem(int pos){
		return data.get(pos);
	}
	
	public List<UploadItem> getItemList(){
		return data;
	}

}
